package ivied.p001astreamchat.AddChat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;

public class DialogChoiceSite extends DialogFragment implements  OnClickListener {
	 public static final String SITE = "site";
	 public static final String FOR = "for";

    public static final int TEXT_VIEW_TOP_MARGINS = 20;
     FactorySite mFactorySite = new FactorySite();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.dialog_select_site));

        LinearLayout layout = new LinearLayout(getActivity());

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            llp.setMargins(0, TEXT_VIEW_TOP_MARGINS, 0, 0);
            TextView textSite =new TextView(getActivity());

            String text = "Add " + siteName.name() + " channel";
            Drawable drawable =  mFactorySite.getSite(siteName).getLogo();
            textSite.setText(text);
            textSite.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            textSite.setGravity(Gravity.CENTER);
            textSite.setLayoutParams(llp);
            textSite.setId(MainActivity.ID_SITE_SELECT + siteName.ordinal());
            textSite.setOnClickListener(this);
            layout.addView(textSite);

        }
        return  layout;
    }



    @Override
	public void onClick(View v) {
		onDismiss(getDialog());
        int id= v.getId() - MainActivity.ID_SITE_SELECT;

        for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
            if (siteName.ordinal() == id) sendResult(siteName);
        }

    }

    private void sendResult (FactorySite.SiteName site){
        Intent intent = new Intent(getActivity(), AddChannel.class);
        intent.putExtra(SITE, site);
        intent.putExtra(FOR, "add");
        getActivity().startActivityForResult(intent, AddChat.TASK_ADD);
    }
}
