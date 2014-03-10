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

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

/**
 * Created by Serv on 20.07.13.
 */
public class DialogSelectChatSite extends DialogFragment implements View.OnClickListener {
    FactorySite factory = new FactorySite();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.dialog_select_site));
        LinearLayout layout = setLayout();
        return  layout;
    }




    @Override
    public void onClick(View v) {
        onDismiss(getDialog());
        for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
            if (siteName.ordinal() == v.getId()) {
                Intent intent = new Intent(getActivity(), ViewAddChannel.class);
                intent.putExtra(DialogChoiceSite.SITE, siteName);
                intent.putExtra(DialogChoiceSite.FOR, "add");
                getActivity().startActivityForResult(intent, ViewAddChat.TASK_ADD);

            }
        }
    }

    private LinearLayout setLayout() {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
            String text = "Add " + siteName.name() + " chat channel";
            Drawable drawable =  factory.getSite(siteName).getLogo();
            int id =  siteName.ordinal();
            TextView chatChannel =  createTextView(drawable, text, id);
            layout.addView(chatChannel);
        }
        return layout;
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
}
