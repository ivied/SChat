package ivied.p001astreamchat.Sites;

import android.os.AsyncTask;

import org.apache.http.client.methods.HttpGet;

import ivied.p001astreamchat.Sites.Twitch.Twitch;

/**
 * Created by Serv on 09.06.13.
 */
public class AsyncDownloadJson extends AsyncTask<AsyncDownloadJson.CustomDownloadJson,String,Void>{
    public  interface GetJson{
        abstract  public void afterGetJson(String json);
    }
    GetJson mGetJson;
    StringBuilder builderJson;

    @Override
    protected Void doInBackground(CustomDownloadJson... downloadJsonClass) {
        HttpGet httpGet = new HttpGet(downloadJsonClass[0].url);
        Site site = new Twitch();
        builderJson =  site.jsonRequest(httpGet);
        mGetJson = downloadJsonClass[0].mGetJson;

        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mGetJson.afterGetJson(builderJson.toString());
    }

    /**
     * Created by Serv on 10.06.13.
     */
    public static class CustomDownloadJson {
       public String url;
       public  GetJson mGetJson;

        public CustomDownloadJson(String url, GetJson mGetJson) {
            this.url = url;
            this.mGetJson = mGetJson;
        }
    }
}
