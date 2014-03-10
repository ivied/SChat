package ivied.p001astreamchat.Sites;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.MyContentProvider;

/**
 * Created by Serv on 03.06.13.
 */
public class SmileHelper  {

    private static int height =50;
    private static int width =50;

    public  SmileHelper() {

        for (FactorySite.SiteName siteName : FactorySite.SiteName.values()){
            SmileParser smileParser = new SmileParser();
            smileParser.execute(siteName);

        }


    }


    static class SmileParser extends AsyncTask<FactorySite.SiteName, Boolean, Void> {
        FactorySite factorySite = new FactorySite();


        Site site;
        @Override
        protected Void doInBackground(FactorySite.SiteName... params) {

            site = factorySite.getSite(params[0]);

            site.setSmileMaps();
            if(site.getSmileAddress() == null) return null;

            HttpGet httpGet = new HttpGet(site.getSmileAddress());
            HttpResponse response = site.getResponse(httpGet);
            if (response == null) return null;

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (!(statusCode == 200)) return null;

            Header firstHeader = response.getFirstHeader(site.getSmileModifyHeader());
            if (firstHeader == null) return null;
            String header = firstHeader.getValue();
            String selection = "site = ?";
            String headerName = site.getSiteName()+ "header";
            String [] selectionArgs =  new String []{headerName};
            Cursor c = MyApp.getContext().getContentResolver()
                    .query(MyContentProvider.SMILE_INSERT_URI, null, selection, selectionArgs, null );
            if (c.moveToNext()){
                if (c.getString(3).equalsIgnoreCase(header)) {
                    c.close();
                    publishProgress(true);
                    return null;

                }else{
                    MyApp.getContext().getContentResolver().delete(MyContentProvider.SMILE_INSERT_URI, selection, selectionArgs );
                }
            }
            c.close();
            MyApp.getContext().getContentResolver().delete(MyContentProvider.SMILE_INSERT_URI
                    , selection, new String[]{site.getSiteName()});
            site.getSiteSmiles(header);

            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... newSmiles) {
            super.onProgressUpdate(newSmiles);
            if (newSmiles[0]) site.setSmileMaps();
        }



        @Override
        protected void onPostExecute(Void result) {


            super.onPostExecute(result);

        }
    }

    static public byte[] urlToImageBLOB(String url) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            entity = response.getEntity();
        }

        return EntityUtils.toByteArray(entity);
    }

    public static Bitmap getImageFromBLOB(byte[] mBlob, float smileSizeMultiplicator) {

        return getResizedBitmap(BitmapFactory.decodeByteArray(mBlob, 0, mBlob.length), smileSizeMultiplicator);

    }

    public static Bitmap getResizedBitmap(Bitmap bm, float smileSizeMultiplicator) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(smileSizeMultiplicator, smileSizeMultiplicator);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
}
