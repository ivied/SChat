package ivied.p001astreamchat.Sites.Twitch;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.AsyncDownloadJson;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.VideoViewSetter;
import ivied.p001astreamchat.VideoView.HTML5WebView;

/**
 * Created by Serv on 12.06.13.
 */
public class TwitchVideoSetter extends VideoViewSetter implements AsyncDownloadJson.GetJson {
    private final static String CRYPT_KEY =  "Wd75Yj9sS26Lmhve";
    private final static String TOKEN_ADDRESS_1 = "http://usher.twitch.tv/stream/iphone_token/";
    private final static String TOKEN_ADDRESS_2 = ".json?type=iphone&allow_cdn=true";
    private final static String MULTI_PLAYLIST_1 = "http://usher.twitch.tv/stream/multi_playlist/";
    private final static String MULTI_PLAYLIST_2 = ".m3u8";
    private final static String MULTI_PLAYLIST_3 = "?allow_cdn=true&token=";
    private final static String MULTI_PLAYLIST_4 = "&hd=true";
    HTML5WebView mWebView;
    String channel;

    /*public TwitchVideoSetter(Context context, SetVideoView setVideoView) {
        super(context, setVideoView);
    }*/


    @Override
    public void getVideoView(String channel, Context context, SetVideoView setVideoView) {
        this.channel = channel.toLowerCase();
        AsyncDownloadJson.CustomDownloadJson downloadJson =
                new AsyncDownloadJson.CustomDownloadJson(TOKEN_ADDRESS_1 +channel.toLowerCase() + TOKEN_ADDRESS_2 , this);
        AsyncDownloadJson asyncDownloadJson = new AsyncDownloadJson();
        asyncDownloadJson.execute(downloadJson);
        this.setVideoView = setVideoView;
        this.context = context;



    }

    @Override
    public Drawable getLogo() {
        return MyApp.getContext().getResources().getDrawable(R.drawable.twitch);
    }

    @Override
    public FragmentAddChannelStandard getFragmentAddChannel() {
        return new FragmentFindChannelTwitch();
    }


    public static String hmacSha1(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterGetJson(String json) {
        try {
            json = json.replace("[", "").replace("]","");
            JSONObject jsonObj = new JSONObject(json);
            String token = jsonObj.getString("token");
            String hmacSha1 =hmacSha1(token, CRYPT_KEY);
            String query = URLEncoder.encode(hmacSha1 + ":" + token, "utf-8");
            String address = MULTI_PLAYLIST_1 + channel +MULTI_PLAYLIST_2 + MULTI_PLAYLIST_3+ query + MULTI_PLAYLIST_4;

            String html= "<video x-webkit-airplay=\"allow\" controls=\"\" alt=\"Live Stream\" width=\"100%\" height=\"100%\" src=\""+ address +"\"></video>";
            mWebView = new HTML5WebView(context);
            mWebView. loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            setVideoView.setVideoView(mWebView, channel, getEnum());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override

    public FactoryVideoViewSetter.VideoSiteName getEnum() {
        return FactoryVideoViewSetter.VideoSiteName.TWITCHStream;
    }
}
