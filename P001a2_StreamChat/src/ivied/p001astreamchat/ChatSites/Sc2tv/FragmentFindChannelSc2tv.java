package ivied.p001astreamchat.ChatSites.Sc2tv;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.R;

public class FragmentFindChannelSc2tv  extends FragmentAddChannelStandard implements OnClickListener {
	DialogFragment dlgShowApi;
	DialogFragment dlgCheckName;
	final static String USER_STREAMS = "user_streams";
	final static String MAIN_PAGE = "online";
    EditText channel;

	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_find_site_sc2tv, null);
		    
		    Button btnFindFromApi = (Button) v.findViewById(R.id.btnFromApiSc2tv);
		    btnFindFromApi.setOnClickListener(this);
		    btnFindFromApi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); 
		    Button btnFindFromMain = (Button) v.findViewById(R.id.btnShowMainPageSc2tv);
		    btnFindFromMain.setOnClickListener(this);
		    btnFindFromMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14); 
		    Button btnCheckName = (Button) v.findViewById(R.id.btnByNameSc2tv);
		    btnCheckName.setOnClickListener(this);
		    btnCheckName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            channel = (EditText) v.findViewById(R.id.editChannelNumberSc2tv);
		    return v;
		  }

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btnFromApiSc2tv:
			dlgShowApi = new DialogShowSc2tvApi().newInstace(USER_STREAMS);
			dlgShowApi.show(getFragmentManager(), "Show api");
			break;
		case R.id.btnShowMainPageSc2tv:
			dlgShowApi = new DialogShowSc2tvApi().newInstace(MAIN_PAGE);
			dlgShowApi.show(getFragmentManager(), "Show api");
			break;
		case R.id.btnByNameSc2tv:
			dlgCheckName = new DialogCheckNameSc2tv();
			dlgCheckName.show(getFragmentManager(), "Check name");
			break;
		}
	}

    @Override
    public EditText getEditTextChannel() {
        return channel;
    }
}
