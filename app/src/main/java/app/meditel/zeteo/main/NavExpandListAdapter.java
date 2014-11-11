package app.meditel.zeteo.main;

/**
 * Created by FOla Yinka on 1/29/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.HashMap;
import java.util.List;

import app.meditel.zeteo.R;

public class NavExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    public NavExpandListAdapter(Context context, List<String> listDataHeader,
                                HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (groupPosition) {
            case 0:
                switch (childPosition) {
                    case 0:
                        convertView = infalInflater.inflate(R.layout.profile_nav, null);
                        convertView.setBackground(null);
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (childPosition) {
                    case 0:
                        convertView = infalInflater.inflate(R.layout.home_nav, null);
                        break;
                    case 1:
                        convertView = infalInflater.inflate(R.layout.notif_nav, null);
                        break;
                    case 2:
                        convertView = infalInflater.inflate(R.layout.explore_nav, null);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.home_nav_home, null);
        switch (groupPosition) {
            case 0:
                convertView.setVisibility(View.GONE);
                break;
            case 1:
                convertView.setMinimumHeight(2);
                break;
            default:
                break;
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}