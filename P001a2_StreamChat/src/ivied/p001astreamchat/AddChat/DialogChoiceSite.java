package ivied.p001astreamchat.AddChat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;

public class DialogChoiceSite extends DialogFragment implements  OnClickListener {
	 public static final String SITE = "site";
	 public static final String FOR = "for";

       private static final int ID_ADD_VIDEO = 25;
    public static final int TEXT_VIEW_TOP_MARGINS = 20;
     FactorySite mFactorySite = new FactorySite();
    public static DialogChoiceSite newInstance(ArrayList<AddChatChannel> channels) {
        int flag = 0;
        DialogChoiceSite dlg = new DialogChoiceSite();
        Bundle args = new Bundle();
        for (AddChatChannel channel : channels){
            for (FactoryVideoViewSetter.VideoSiteName siteName : FactoryVideoViewSetter.VideoSiteName.values()){
                if (channel.site.equalsIgnoreCase(siteName.name())) flag = 1;
            }
        }
        args.putInt("flag", flag);
        dlg.setArguments(args);
        return dlg;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.dialog_title_what_add));



        LinearLayout layout = new LinearLayout(getActivity());

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){


            String text = "Add " + siteName.name() + " chat channel";
            Drawable drawable =  mFactorySite.getSite(siteName).getLogo();
            int id = MainActivity.ID_SITE_SELECT + siteName.ordinal();
            TextView chatChannel =  createTextView(drawable, text, id);
            layout.addView(chatChannel);

        }
        if (getArguments().getInt("flag") == 0){
            TextView videoChannel = createTextView(getResources().getDrawable(R.drawable.default_video_poster), getResources().getString(R.string.dialog_field_add_video), ID_ADD_VIDEO );
            layout.addView(videoChannel);
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
        if (v.getId() == 25) {
            DialogFragment dlgSelectStreamSite = new DialogSelectStreamSite();
            dlgSelectStreamSite.show(getFragmentManager(),"Select stream site");

        }

    }

    private void sendResult (FactorySite.SiteName site){
        Intent intent = new Intent(getActivity(), AddChannel.class);
        intent.putExtra(SITE, site);
        intent.putExtra(FOR, "add");
        getActivity().startActivityForResult(intent, AddChat.TASK_ADD);
    }

    private TextView createTextView (Drawable logo, String text , int id){
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        llp.setMargins(0, TEXT_VIEW_TOP_MARGINS, 0, 0);
        TextView textSite =new TextView(getActivity());
        textSite.setText(text);
        textSite.setCompoundDrawablesWithIntrinsicBounds(logo, null, null, null);
        textSite.setGravity(Gravity.CENTER);
        textSite.setLayoutParams(llp);
        textSite.setId(id);
        textSite.setOnClickListener(this);
        return textSite;
    }
}
