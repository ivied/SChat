package ivied.p001astreamchat.Sites.Twitch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.Sc2tv.AsyncDownloadJson;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 09.06.13.
 */
public class DialogTwitchTopGames extends SherlockDialogFragment implements DialogInterface.OnClickListener {
    private static final String TWITCH_API_TOP_GAMES = "https://api.twitch.tv/kraken/games/top?limit=";
    private static final String COUNT_OF_GAMES = "20";
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ProgressBar pb;


    public Dialog onCreateDialog(Bundle savedInstanceState) {


        downloadTwitchJson();

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_item, list);
        pb = new ProgressBar(getActivity(),
                null,
                android.R.attr.progressBarStyle);
        pb.setIndeterminate(true);


        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.dialog_title_show_sc2tv_by_api)).setAdapter(adapter, this).setView(pb);



        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SherlockDialogFragment dialogTwitchChannelByGame = DialogTwitchChannelByGame.newInstance(list.get(which));
        dialogTwitchChannelByGame.show(getFragmentManager(), "Show channels by game");

    }

    private void downloadTwitchJson() {
        DownloadTwitchApi downloadApi = new DownloadTwitchApi( );
        downloadApi.execute();
       /* AsyncDownloadJson asyncDownloadJson = new AsyncDownloadJson();
        asyncDownloadJson.execute(TWITCH_API_TOP_GAMES + COUNT_OF_GAMES);*/
    }

/*    @Override
    public void getJsonString(String json) {
        String jsonNew = json;
    }*/

    class DownloadTwitchApi extends AsyncTask <Void,String,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpGet  httpGet = new HttpGet( TWITCH_API_TOP_GAMES + COUNT_OF_GAMES  );
            Site site = new Twitch();
            StringBuilder builderJson =  site.jsonRequest(httpGet);
            try {
                JSONObject jsonObj = new JSONObject(builderJson.toString());
                JSONArray jsonTopGames = jsonObj.getJSONArray("top");
                String [] list = new String[jsonTopGames.length()];
                for (int i = 0;  i < jsonTopGames.length(); i++) {
                    JSONObject game = (JSONObject) jsonTopGames.get(i);
                    JSONObject gameInfo = game.getJSONObject("game");
                    list[i] = gameInfo.getString("name");

                }
                publishProgress(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pb.setVisibility(View.GONE);
            adapter.clear();
            for(String game : values){ adapter.add(game);}
            adapter.notifyDataSetChanged();

        }


    }


}
