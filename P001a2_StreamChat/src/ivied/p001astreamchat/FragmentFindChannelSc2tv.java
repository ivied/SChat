package ivied.p001astreamchat;

import ivied.p001astreamchat.R.id;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentFindChannelSc2tv  extends SherlockFragment implements OnClickListener {
	DialogFragment dlgShowApi;
	DialogFragment dlgChekName;
	final static String USER_STREAMS = "user_streams";
	final static String MAIN_PAGE = "online";
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_find_site_sc2tv, null);
		    
		    Button btnFindFromApi = (Button) v.findViewById(R.id.btnFromApiSc2tv);
		    btnFindFromApi.setOnClickListener(this);
		    Button btnFindFromMain = (Button) v.findViewById(R.id.btnShowMainPageSc2tv);
		    btnFindFromMain.setOnClickListener(this);
		    Button btnCheckName = (Button) v.findViewById(R.id.btnByNameSc2tv);
		    btnCheckName.setOnClickListener(this);
		    return v;
		  }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
			dlgChekName = new DialogChekNameSc2tv();
			dlgChekName.show(getFragmentManager(), "Chek name");
			break;
		}
	}

}
