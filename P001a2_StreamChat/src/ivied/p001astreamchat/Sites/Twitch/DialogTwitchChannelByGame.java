package ivied.p001astreamchat.Sites.Twitch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ivied.p001astreamchat.AddChat.SelectedListener;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.AsyncDownloadJson;

/**
 * Created by Serv on 09.06.13.
 */
public class DialogTwitchChannelByGame extends SherlockDialogFragment implements DialogInterface.OnClickListener, AsyncDownloadJson.GetJson {
    SelectedListener mCallback;
    private static final String TWITCH_API_BY_GAME = "https://api.twitch.tv/kraken/streams?game=";
    private static final String HLS_TRUE = "&hls=true";
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ProgressBar pb;
    String game;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (SelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectedListener ");
        }
    }

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
        renewAdapter();

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

        mCallback.pasteChannel(list.get(which));
    }

    private void renewAdapter() {
        AsyncDownloadJson.CustomDownloadJson downloadJson =
                new AsyncDownloadJson.CustomDownloadJson(TWITCH_API_BY_GAME + game+ HLS_TRUE, this);
        AsyncDownloadJson asyncDownloadJson = new AsyncDownloadJson();
        asyncDownloadJson.execute(downloadJson);

    }

    @Override
    public void afterGetJson(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonStreams = jsonObj.getJSONArray("streams");
            String [] list = new String[jsonStreams.length()];
            for (int i = 0;  i < jsonStreams.length(); i++) {
                JSONObject stream = (JSONObject) jsonStreams.get(i);
                JSONObject channel = stream.getJSONObject("channel");
                list[i] = channel.getString("display_name");
                pb.setVisibility(View.GONE);
                adapter.clear();
                for(String game : list){ adapter.add(game);}
                adapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





}

