package app.meditel.zeteo.main;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FOla Yinka on 1/29/14.
 */
public class NavListView {
    Context context;
    ExpandableListView listView;
    NavExpandListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    public NavListView(Context context, ExpandableListView listView) {
        this.context = context;
        this.listView = listView;
    }

    public void initContent() {

        prepareListData();

        listAdapter = new NavExpandListAdapter(context, listDataHeader, listDataChild);
        listView.setAdapter(listAdapter);
        for (int i = 0; i < 2; i++) {
            listView.expandGroup(i);

            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                public boolean onGroupClick(ExpandableListView arg0, View itemView, int itemPosition, long itemId) {
                    listView.expandGroup(itemPosition);
                    return true;
                }
            });
        }
    }

    /*
     * Preparing the list data
     */

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("0");
        listDataHeader.add("1");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("0");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("0");
        nowShowing.add("1");
        nowShowing.add("2");


        listDataChild.put(listDataHeader.get(0), top250);
        listDataChild.put(listDataHeader.get(1), nowShowing);
    }

}
