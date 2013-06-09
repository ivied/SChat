package ivied.p001astreamchat.Sites.Sc2tv;

import android.os.AsyncTask;

import org.apache.http.client.methods.HttpGet;

import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.Twitch.Twitch;

/**
 * Created by Serv on 09.06.13.
 */
public class AsyncDownloadJson extends AsyncTask<String,String,Void>{
    public  interface GetJson{
        abstract  public void getJsonString(String json);
    }
    GetJson mGetJson;
    StringBuilder builderJson;
    @Override
    protected Void doInBackground(String... httpReq) {
        HttpGet httpGet = new HttpGet(httpReq[0]);
        Site site = new Twitch();
        builderJson =  site.jsonRequest(httpGet);


        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        mGetJson.getJsonString(builderJson.toString());
    }
}
