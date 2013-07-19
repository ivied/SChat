package ivied.p001astreamchat.Sites.GoodGame;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Sites.Message;

/**
 * Created by Serv on 19.07.13.
 */
public class WebSocketForGG extends WebSocketConnection  {

    private final GoodGame goodGame;
    private  ConnectionHandler webSocketHandler;
    public static String GGNick;
    public static String GGPassWord;
    public WebSocketForGG (GoodGame goodGame) {
        super();
        this.goodGame = goodGame;
    }


    public void connect(String wsUri) throws WebSocketException {
        this.webSocketHandler = getWebSocketHandler();
        super.connect(wsUri, webSocketHandler);
    }

    private WebSocket.ConnectionHandler getWebSocketHandler() {
        return new WebSocket.ConnectionHandler() {
            @Override
            public void onOpen() {

                Log.d(MainActivity.LOG_TAG, "Status: Connected to ");

            }

            @Override
            public void onTextMessage(String message) {
                if(message.equals("o")){
                    setAuthorization();
                    return;
                }

                Log.i(MainActivity.LOG_TAG, "Got echo: " + message);
                messagingProtocol(message);
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
        goodGame.insertMessage(message);

    }

    private void setAuthorization() {
        int _userId = -1;
        String _userToken = null;
        String command = "[\"{\\\"type\\\":\\\"auth\\\",\\\"data\\\":{\\\"user_id\\\":\\\"" + _userId + "\\\",\\\"token\\\":\\\"" + _userToken + "\\\"}}\"]";
        this.sendTextMessage(command);
    }

    private void joinChannel() {
        String command = "[\"{\\\"type\\\":\\\"join\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" + 3435 +  "\\\"}}\"]";
        this.sendTextMessage(command);
    }

    private void getHistory() {

        String command = "[\"{\\\"type\\\":\\\"get_channel_history\\\",\\\"data\\\":{\\\"channel_id\\\":\\\"" + 3435 +  "\\\"}}\"]";
        this.sendTextMessage(command);
    }


}



