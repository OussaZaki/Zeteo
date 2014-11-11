package app.meditel.zeteo;

import android.content.Context;
import android.database.MatrixCursor;
import android.widget.CursorAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by FOla Yinka on 1/17/14.
 */
public class MyMatrixCursor extends MatrixCursor {
    String[] url;
    JsonReadTask task;
    ListView listView;
    int[] ids;
    int layoutID;
    Context context;

    public MyMatrixCursor(Context context, final ListView listView, int layoutID, String[] names, int[] ids, String[] url) {
        super(names);
        this.url = url;
        this.listView = listView;
        this.ids = ids;
        this.layoutID = layoutID;
        this.context = context;
        task = new JsonReadTask() {
            @Override
            public void ListDrawer() {
                try {
                    JSONArray jsonMainNode = new JSONArray(jsonResult);
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        String[] nextData = new String[getColumnCount()];
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        for (int j = 0; j < getColumnCount(); j++) {
                            nextData[j] = jsonChildNode.optString(getColumnName(j));
                            if(String.valueOf(jsonChildNode.optString("good_answer")) == "1")
                                System.out.println(j+" : "+jsonChildNode.optString("good_answer"));
                        }
                        addRow(nextData);
                        ((CursorAdapter) listView.getAdapter()).notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute(url);
        listView.setAdapter(new MyCursorAdapter(context, this, 0, ids, layoutID));
    }

    public void refresh() {
        new MyMatrixCursor(context, listView, layoutID, getColumnNames(), ids, url);
    }

}
