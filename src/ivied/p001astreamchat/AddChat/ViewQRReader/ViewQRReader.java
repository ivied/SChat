/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package ivied.p001astreamchat.AddChat.ViewQRReader;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;

import ivied.p001astreamchat.AddChat.Channel;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;

/* Import ZBar Class files */

public class ViewQRReader extends Activity
{
    public static final String CHAT_NAME = "Chat name";
    public static final String CHANNELS = "Channels";
    private static final String CHAT_CHANNEL = "chat";
    private static final String VIDEO_CHANNEL = "video";
    private static final String NEW_STRING_SYMBOLS = "\r\n";
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private String qrEncodingResult;
    public static ArrayList<Channel> channels = new ArrayList<Channel>();
    private Channel channel = null;

    TextView scanText;
    Button scanButton;
    Button confirmButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        channels.clear();
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);

        scanButton = (Button)findViewById(R.id.btnQRRepeatScan);


        confirmButton = (Button) findViewById(R.id.btnQRConfirm);
        confirmButton.setVisibility(View.INVISIBLE);

    }

    public void onClickRepeatScan (View view) {
        if (barcodeScanned) {
            barcodeScanned = false;
            scanText.setText("Scanning...");
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
            confirmButton.setVisibility(View.INVISIBLE);

        }
    }

    public void onClickQRConfirm (View view) {
        String chatName = getChannelsFromScanningResult();
        Intent intent = new Intent();
        intent.putExtra(CHAT_NAME, chatName);

        setResult(RESULT_FIRST_USER, intent);
        finish();
    }

    private  String getChannelsFromScanningResult() {
        int indexOfChatNameEnd = qrEncodingResult.indexOf(NEW_STRING_SYMBOLS);
        String chatName = qrEncodingResult.substring(0, indexOfChatNameEnd);
        qrEncodingResult = qrEncodingResult.substring(indexOfChatNameEnd +NEW_STRING_SYMBOLS.length());
        String [] channelsInfo =qrEncodingResult.split(NEW_STRING_SYMBOLS);

        for (String channelInfo : channelsInfo){

            String [] channelInfoArray = channelInfo.split(" ");
            if (channelInfoArray[0].equalsIgnoreCase(CHAT_CHANNEL)){
                channel= new Channel(channelInfoArray[2], Integer.valueOf(channelInfoArray[3]), channelInfoArray[4], FactorySite.SiteName.valueOf(channelInfoArray[1].toUpperCase()));
            }
            if (channelInfoArray[0].equalsIgnoreCase(VIDEO_CHANNEL)){

                channel= new Channel(channelInfoArray[2], Integer.valueOf(channelInfoArray[3]), channelInfoArray[4], FactoryVideoViewSetter.VideoSiteName.valueOf(channelInfoArray[1].toUpperCase()));
            }
            channels.add(channel);

        }
        return chatName;
    }


    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    qrEncodingResult = sym.getData();
                    scanText.setText("barcode result " + qrEncodingResult);
                    barcodeScanned = true;
                    confirmButton.setVisibility(View.VISIBLE);

                }
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
