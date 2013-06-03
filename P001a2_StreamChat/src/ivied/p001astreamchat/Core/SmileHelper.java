package ivied.p001astreamchat.Core;

import android.os.AsyncTask;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ivied.p001astreamchat.Sites.Sc2tv.Sc2tv;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 03.06.13.
 */
public class SmileHelper  {

    public  SmileHelper() {
        SmileParser smileParser = new SmileParser();
        smileParser.execute();

    }


    class SmileParser extends AsyncTask<Void, Void, Void> {
        private static final String SC2TV_SMILES = "http://chat.sc2tv.ru/js/smiles.js?v=35";


        @Override
        protected Void doInBackground(Void... params) {
            HttpGet httpGet = new HttpGet(SC2TV_SMILES + ".json");
            Site site = new Sc2tv();
            StringBuilder builderJson = site.jsonRequest(httpGet);

            JSONArray jsonArray = new JSONArray();
            try {
                JSONObject jsonObj = new JSONObject(builderJson.toString());


                jsonArray = jsonObj.getJSONArray("smiles");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }
}
