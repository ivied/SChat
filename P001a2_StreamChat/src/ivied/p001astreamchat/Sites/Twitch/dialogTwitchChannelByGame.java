package ivied.p001astreamchat.Sites.Twitch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 09.06.13.
 */
public class DialogTwitchChannelByGame extends SherlockDialogFragment implements DialogInterface.OnClickListener {
    TwitchSelectedListener mCallback;
    public  interface TwitchSelectedListener {
        abstract public void pasteTwitchChannel(String channel);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TwitchSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    private static final String TWITCH_API_BY_GAME = "https://api.twitch.tv/kraken/streams?game=";
    private static final String HLS_TRUE = "&hls=true";
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ProgressBar pb;
    String game;
    public static DialogTwitchChannelByGame newInstance(String game) {
        DialogTwitchChannelByGame dlg = new DialogTwitchChannelByGame();
        Bundle args = new Bundle();
        args.putString("game", game);
        dlg.setArguments(args);
        return dlg;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        game =  getArguments().getString("game");
        game = game.replace(" ", "+");
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

        mCallback.pasteTwitchChannel(list.get(which));
    }

    private void downloadTwitchJson() {
        DownloadTwitchApi downloadApi = new DownloadTwitchApi( );
        downloadApi.execute();

    }


    class DownloadTwitchApi extends AsyncTask<Void,String,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpGet httpGet = new HttpGet( TWITCH_API_BY_GAME + game + HLS_TRUE  );
            Site site = new Twitch();
            StringBuilder builderJson =  site.jsonRequest(httpGet);
            try {
                JSONObject jsonObj = new JSONObject(builderJson.toString());
                JSONArray jsonStreams = jsonObj.getJSONArray("streams");
                String [] list = new String[jsonStreams.length()];
                for (int i = 0;  i < jsonStreams.length(); i++) {
                    JSONObject stream = (JSONObject) jsonStreams.get(i);
                    JSONObject channel = stream.getJSONObject("channel");
                    list[i] = channel.getString("display_name");

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

