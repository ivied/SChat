package ivied.p001astreamchat.Sites.GoodGame;

import android.database.Cursor;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketException;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.Message;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 19.07.13.
 *
 */
public class WebSocketForGG  {

    public static final String GOODGAME_CHANNEL_STATUS_URL = "http://goodgame.ru/api/getchannelstatus?id=";
    public static final String JSON_ID_MESSAGE_ID = "message_id";
    public static final String JSON_ID_TEXT = "text";
    public static final String JSON_ID_TIMESTAMP = "timestamp";
    public static final String JSON_ID_USER_NAME = "user_name";
    private final GoodGame parent;

    private WebSocket connection;
    private Pattern patternForChannelID = Pattern.compile("(stream id=\")([0-9]*)");

    public WebSocketForGG (GoodGame parent, WebSocket webSocket) {
        connection = webSocket;
        this.parent = parent;

    }


    public void connect(String wsUri) throws WebSocketException {

        WebSocket.ConnectionHandler webSocketHandler = getWebSocketHandler();
        connection.connect(wsUri, webSocketHandler);
    }

    private WebSocket.ConnectionHandler getWebSocketHandler() {
        return new WebSocket.ConnectionHandler() {
            @Override
            public void onOpen() {

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
                synchronized (parent){
                    getConnectIDs();
                }
                setAuthorization();
                return null;
            }

            Log.i(MainActivity.LOG_TAG, "Got echo: " + message);
            messagingProtocol(message);
            return null;
        }

    }

    private void getConnectIDs() {
        HttpGet getIDs = new HttpGet(GOODGAME_CHANNEL_STATUS_URL + parent.channel);

        HttpResponse response = parent.getResponse(getIDs);
        parseIDs(response);
    }

    private void parseIDs(HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();

                String line = parent.convertStreamToString(content);
                Matcher lockForChannelID = patternForChannelID.matcher(line);
                if(lockForChannelID.find()){
                    parent.channelID = Integer.parseInt(lockForChannelID.group(2));
                    Log.i(MainActivity.LOG_TAG, "channelID = " + parent.channelID);
                }
                content.close();
            } else {
                parent.sendToast(MyApp.getContext().getResources().getString(R.string.toast_failed_get_goodgame_id) + parent.channel +" ID");
            }
        } catch (IOException e) {
            parent.sendToast(MyApp.getContext().getResources().getString(R.string.toast_failed_get_goodgame_id) + parent.channel +" ID");
            e.printStackTrace();
        }

    }



    /*Pattern p = Pattern
            .compile(SC2_LOGIN_PATTERN );
    Matcher m = p.matcher(line);
    b = m.matches();
    if (b) {
        getCode = m.group().replaceAll("\\D+", "")
                .replaceFirst("2", "");
    }
*/
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
                    case "channel_history":
                        saveChannelHistory(messageData);
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

    private void saveChannelHistory(JSONObject messageData) {
        try {
            JSONArray messages = messageData.getJSONArray("messages");
            int countOfNew = getCountOfNew(messages);
            for (int i = messages.length() - countOfNew ; i < messages.length(); i++) {
                  insertMessage(messages.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertMessage(JSONObject messageData) throws JSONException {
        String user = messageData.getString(JSON_ID_USER_NAME);
        long timeStamp = Long.parseLong(messageData.getString(JSON_ID_TIMESTAMP));
        String text = messageData.getString(JSON_ID_TEXT);
        String messageID = messageData.getString(JSON_ID_MESSAGE_ID);
        Message message = new Message( parent.channel, user, text, messageID, timeStamp);
        parent.insertMessage(message);

    }



    private void setAuthorization() {
        parent.getLogin();


        String command = "[\"{\\\"type\\\":\\\"auth\\\",\\\"data\\\":{\\\"user_id\\\":\\\"" + parent.GGUserID + "\\\",\\\"token\\\":\\\"" + parent.GGUserToken + "\\\"}}\"]";
        connection.sendTextMessage(command);
    }

    private void joinChannel() {
        String command = "[\"{\\\"type\\\":\\\"join\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" + parent.channelID +  "\\\"}}\"]";
        Log.i(MainActivity.LOG_TAG, command);
        connection.sendTextMessage(command);
    }

    private void getHistory() {

        String command = "[\"{\\\"type\\\":\\\"get_channel_history\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" +  parent.channelID +  "\\\"}}\"]";
        connection.sendTextMessage(command);
    }


    private int getCountOfNew(JSONArray jsonArray) throws JSONException {
        int countOfNewMessages = 0;
        Cursor c;
        do{
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - countOfNewMessages - 1);
            String[] selectionArgs = new String[] { parent.getSiteEnum().name() + jsonObject.getString(JSON_ID_MESSAGE_ID), parent.channel };

            c = MyApp.getContext().getContentResolver().query(
                    Site.INSERT_URI, null,
                    "identificator = ? AND channel = ?", selectionArgs, null);
            if (c.getCount() == 0) countOfNewMessages++;
        }while ((c.getCount() == 0) && (jsonArray.length() != countOfNewMessages));
        c.close();

        return countOfNewMessages;
    }

}



