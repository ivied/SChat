package ivied.p001astreamchat.Sites.Sc2tv;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;

public class DialogShowSc2tvApi extends DialogFragment  {
    final static String  SC2TV_API = "http://sc2tv.ru/api.php";
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> id = new ArrayList<String>();
	DownloadApi downloadApi;
	static String addFrom;
	 public static DialogFragment newInstace(String from) {
		 	addFrom = from; 
	        DialogFragment dialogFragment = new DialogShowSc2tvApi();
	        return dialogFragment;
	    }
	 
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		list.add("Wait response");
		downloadApi = new DownloadApi( );
		downloadApi.execute();
		
		try {
			list = downloadApi.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item, list);
		

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.dialog_title_show_sc2tv_by_api)).setAdapter(adapter, myClickListener);

		return adb.create();
	}

	
	OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {

	    	EditText channel = (EditText) getActivity().findViewById(R.id.editChannelNumberSc2tv);
	    	channel.setText(id.get(which));
	    	Log.d(MainActivity.LOG_TAG, "which = " + id.get(which));
	    }
	  };
	@Override
	public void onResume() {
		super.onResume();

	}



	class DownloadApi extends AsyncTask<Void, Void, ArrayList<String>> {
		private final String apiUrl = SC2TV_API;
		
		@Override
		protected ArrayList<String> doInBackground(Void... arg0) {
			id.clear();
			list.clear();
			
			
			list = parseApi();
			
		
			
			return list;
		}
		
	

		private ArrayList<String> parseApi() {
			ArrayList<String> newList = new ArrayList<String>();
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new URL(apiUrl).openStream());list.clear();
				Log.d(MainActivity.LOG_TAG, "!!!!!!!! ");
				
				// optional, but recommended
				// read this -
				// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				NodeList user = doc.getElementsByTagName(addFrom);
				Element elem = (Element) user.item(0);

				NodeList streamList = elem.getElementsByTagName("stream");

				for (int temp = 0; temp < streamList.getLength(); temp++) {

					Node nNode = streamList.item(temp);

					System.out.println("\nCurrent Element :"
							+ nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						if (addFrom.equalsIgnoreCase(FragmentFindChannelSc2tv.USER_STREAMS)){
								newList.add( eElement.getElementsByTagName("user").item(0).getTextContent() +
								": "+eElement.getElementsByTagName("title").item(0).getTextContent());}
						if (addFrom.equalsIgnoreCase(FragmentFindChannelSc2tv.MAIN_PAGE)){
							newList.add(eElement.getElementsByTagName("title").item(0).getTextContent());}
						id.add(eElement.getElementsByTagName("id").item(0).getTextContent());
					
					}
				}
                
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return newList;
		}
	}

}
