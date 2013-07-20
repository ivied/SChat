package ivied.p001astreamchat.Sites.GoodGame;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketException;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Sites.Message;

/**
 * Created by Serv on 19.07.13.
 *
 */
public class WebSocketForGG extends GoodGame {

    private final GoodGame parent;
    private WebSocket.ConnectionHandler webSocketHandler;

    private WebSocket connection;


    public WebSocketForGG (GoodGame parent, WebSocket webSocket) {
        connection = webSocket;
        this.parent = parent;

    }


    public void connect(String wsUri) throws WebSocketException {

        this.webSocketHandler = getWebSocketHandler();
        connection.connect(wsUri, webSocketHandler);
    }

    private WebSocket.ConnectionHandler getWebSocketHandler() {
        return new WebSocket.ConnectionHandler() {
            @Override
            public void onOpen() {
                Log.d(MainActivity.LOG_TAG, "Status: Connected to ");

            }

            @Override
            public void onTextMessage(String message) {
                AsyncMessaging messaging = new AsyncMessaging();
                messaging.execute(message);

            }


            @Override
            public void onRawTextMessage(byte[] bytes) {

            }

            @Override
            public void onBinaryMessage(byte[] bytes) {

            }

            @Override
            public void onClose(int code, String reason) {
                // Log.d(TAG, "Connection lost.");
            }
        };
    }

    class AsyncMessaging extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... messages) {
            String message = messages[0];
            if(message.equals("o")){
                getConnectIDs();
                setAuthorization();
                return null;
            }

            Log.i(MainActivity.LOG_TAG, "Got echo: " + message);
            messagingProtocol(message);
            return null;
        }

    }

    private void getConnectIDs() {
        HttpGet getIDs = new HttpGet(parent.CHAT_URL + parent.channel);

        HttpResponse response = getResponse(getIDs);
        parseIDs(response);
    }

    private void parseIDs(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();

                String line = convertStreamToString(content);
               /* DefaultHttpClient mHttpClient = new DefaultHttpClient();
                BasicHttpContext mHttpContext = new BasicHttpContext();
                CookieStore mCookieStore      = new BasicCookieStore();
                mHttpContext.setAttribute(COOKIE_STORE, mCookieStore.addCookie(new BasicClientCookie("name", "value")));*/

                    /*Pattern p = Pattern
                            .compile(SC2_LOGIN_PATTERN );
                    Matcher m = p.matcher(line);
                    b = m.matches();
                    if (b) {
                        getCode = m.group().replaceAll("\\D+", "")
                                .replaceFirst("2", "");
                    }
*/

                content.close();
            } else {
                Log.e(MainActivity.LOG_TAG, "Failed to download file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void messagingProtocol(String message) {
        if (message.contains("a[\"{\\\"type\\\":\\\"")){

            try {

                JSONArray messageAsArray = new JSONArray(message.substring(1));

                JSONObject messageAsJson= new JSONObject( messageAsArray.getString(0));

                String messageType = (String) messageAsJson.get("type");
                JSONObject messageData = (JSONObject) messageAsJson.get("data");
                switch (messageType){
                    case "success_auth":
                        joinChannel();
                        break;
                    case "success_join":
                        getHistory();
                        break;
                    case "message":
                        insertMessage(messageData);
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

        }
    }

    private void insertMessage(JSONObject messageData) throws JSONException {
        String user = messageData.getString("user_name");
        long timeStamp = Long.parseLong(messageData.getString("timestamp"));
        Message message = new Message(null, user, null, null, timeStamp);
        insertMessage(message);

    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void setAuthorization() {
        int _userId = -1;
        String _userToken = null;
        String command = "[\"{\\\"type\\\":\\\"auth\\\",\\\"data\\\":{\\\"user_id\\\":\\\"" + _userId + "\\\",\\\"token\\\":\\\"" + _userToken + "\\\"}}\"]";
        connection.sendTextMessage(command);
    }

    private void joinChannel() {
        String command = "[\"{\\\"type\\\":\\\"join\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" + 3435 +  "\\\"}}\"]";
        connection.sendTextMessage(command);
    }

    private void getHistory() {

        String command = "[\"{\\\"type\\\":\\\"get_channel_history\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" + 3435 +  "\\\"}}\"]";
        connection.sendTextMessage(command);
    }


}



