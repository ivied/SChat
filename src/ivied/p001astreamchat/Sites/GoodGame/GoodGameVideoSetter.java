package ivied.p001astreamchat.Sites.GoodGame;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.AsyncDownloadJson;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.VideoViewSetter;
import ivied.p001astreamchat.VideoView.HTML5WebView;

/**
 * Created by Serv on 13.06.13.
 */
public class GoodGameVideoSetter extends VideoViewSetter implements AsyncDownloadJson.GetJson {
    public final String GG_CHANNEL_STATUS = "http://goodgame.ru/api/getggchannelstatus?id=";
    public final String GG_RESPONSE_FORMAT = "&fmt=json";
    String channel;
    @Override
    public void getVideoView(String channel, Context context, SetVideoView setVideoView) {
        this.context =context;
        this.setVideoView =setVideoView;
        this.channel = channel;
        String url = GG_CHANNEL_STATUS + channel +GG_RESPONSE_FORMAT;
        AsyncDownloadJson.CustomDownloadJson customDownloadJson = new AsyncDownloadJson.CustomDownloadJson(url,this);
        AsyncDownloadJson downloadJson = new AsyncDownloadJson();
        downloadJson.execute(customDownloadJson);

    }

    @Override
    public Drawable getLogo() {
        return MyApp.getContext().getResources().getDrawable(R.drawable.goodgame);
    }

    @Override
    public FragmentAddChannelStandard getFragmentAddChannel() {
        return new FragmentCheckGG();
    }

    @Override
    public FactoryVideoViewSetter.VideoSiteName getEnum() {
        return FactoryVideoViewSetter.VideoSiteName.GOODGAMESTREAM;
    }

    @Override
    public void afterGetJson(String json) {
        try {

            JSONObject jsonObj = new JSONObject(json);
            Iterator iterator=  jsonObj.keys();
            String id = (String) iterator.next();
            JSONObject stream = jsonObj.getJSONObject(id);
            String embed = stream.getString("embed");
            Pattern link = Pattern.compile("(http[^\"]*)");
            Matcher matcher = link.matcher(embed);
            String url = new String();
            if (matcher.find()) {
                 url = matcher.group(1);
            }
            HTML5WebView mWebView = new HTML5WebView(context);
            mWebView.loadUrl(url);
            setVideoView.setVideoView(mWebView, channel, getEnum());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
