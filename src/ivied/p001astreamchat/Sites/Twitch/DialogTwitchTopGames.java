package ivied.p001astreamchat.Sites.Twitch;

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

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.AsyncDownloadJson;

/**
 * Created by Serv on 09.06.13.
 */
public class DialogTwitchTopGames extends SherlockDialogFragment implements DialogInterface.OnClickListener, AsyncDownloadJson.GetJson {
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
                .setTitle(getResources().getString(R.string.dialog_title_show_sc2tv_by_api))
                .setAdapter(adapter, this).setView(pb);



        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SherlockDialogFragment dialogTwitchChannelByGame = DialogTwitchChannelByGame.newInstance(list.get(which));
        dialogTwitchChannelByGame.show(getFragmentManager(), "Show channels by game");

    }

    private void downloadTwitchJson() {

        AsyncDownloadJson.CustomDownloadJson downloadJson =
                new AsyncDownloadJson.CustomDownloadJson(TWITCH_API_TOP_GAMES + COUNT_OF_GAMES, this);
        AsyncDownloadJson asyncDownloadJson = new AsyncDownloadJson();
        asyncDownloadJson.execute(downloadJson);

    }

    @Override
    public void afterGetJson(String json) {

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonTopGames = jsonObj.getJSONArray("top");
            String [] list = new String[jsonTopGames.length()];
            for (int i = 0;  i < jsonTopGames.length(); i++) {
                JSONObject game = (JSONObject) jsonTopGames.get(i);
                JSONObject gameInfo = game.getJSONObject("game");
                list[i] = gameInfo.getString("name");

            }
            pb.setVisibility(View.GONE);
            adapter.clear();
            for(String game : list){ adapter.add(game);}
            adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
