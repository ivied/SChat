package ivied.p001astreamchat.AddChat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;

/**
 * Created by Serv on 13.06.13.
 */
public class DialogSelectStreamSite extends DialogFragment implements View.OnClickListener {
    FactoryVideoViewSetter factorySetter = new FactoryVideoViewSetter();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.dialog_select_site));



        LinearLayout layout = new LinearLayout(getActivity());

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

        for(FactoryVideoViewSetter.VideoSiteName siteName : FactoryVideoViewSetter.VideoSiteName.values()){


            String text = "Add " + siteName.name() + " channel";
            Drawable drawable =  factorySetter.getVideoSite(siteName).getLogo();
            int id = MainActivity.ID_SITE_SELECT + siteName.ordinal();
            TextView chatChannel =  createTextView(drawable, text, id);
            layout.addView(chatChannel);

        }
        return  layout;
    }



    private TextView createTextView (Drawable logo, String text , int id){
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        llp.setMargins(0, DialogChoiceSite.TEXT_VIEW_TOP_MARGINS, 0, 0);
        TextView textSite =new TextView(getActivity());
        textSite.setText(text);
        textSite.setCompoundDrawablesWithIntrinsicBounds(logo, null, null, null);
        textSite.setGravity(Gravity.CENTER);
        textSite.setLayoutParams(llp);
        textSite.setId(id);
        textSite.setOnClickListener(this);
        return textSite;
    }

    @Override
    public void onClick(View v) {
        onDismiss(getDialog());
        int id= v.getId() - MainActivity.ID_SITE_SELECT;
        for(FactoryVideoViewSetter.VideoSiteName siteName : FactoryVideoViewSetter.VideoSiteName.values()){
            if (siteName.ordinal() == id) {

                Intent intent = new Intent(getActivity(), AddVideoStream.class);

                intent.putExtra(DialogChoiceSite.FOR, "add");
                intent.putExtra(DialogChoiceSite.SITE, siteName);
                getActivity().startActivityForResult(intent, AddChat.TASK_ADD_VIDEO);
            }
        }
    }
}
