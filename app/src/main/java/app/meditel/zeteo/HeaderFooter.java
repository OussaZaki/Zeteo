package app.meditel.zeteo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class HeaderFooter extends Activity {
    static ViewGroup content;
    ViewGroup header;
    View actionbar, searchbar;
    LinkedList<FragView> views;
    Integer tab = 0;
    ImageView home, explore, notif, profile;

    public static ViewGroup getContent() {
        return content;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_footer);
        home = (ImageView) findViewById(R.id.home_button);
        explore = (ImageView) findViewById(R.id.explore_button);
        notif = (ImageView) findViewById(R.id.notif_button);
        profile = (ImageView) findViewById(R.id.profile_button);
        content = (ViewGroup) findViewById(R.id.content);
        header = (ViewGroup) findViewById(R.id.header);
        actionbar = LayoutInflater.from(this).inflate(R.layout.action_bar, header, false);
        ((TextView) actionbar.findViewById(R.id.current_user)).setText(Constants.userName);
        searchbar = LayoutInflater.from(this).inflate(R.layout.search_bar, header, false);
        views = new LinkedList<FragView>();
        header.addView(actionbar);
    }

    class FragView {
        View view;
        Integer tab;

        public FragView(View view, Integer tab) {
            this.tab = tab;
            this.view = view;
        }

        public Integer getTab() {
            return tab;
        }

        public View getView() {
            return view;
        }
    }


}