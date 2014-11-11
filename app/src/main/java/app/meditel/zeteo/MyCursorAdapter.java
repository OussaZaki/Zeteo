package app.meditel.zeteo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by FOla Yinka on 1/17/14.
 */

public class MyCursorAdapter extends CursorAdapter {
    int[] ids;
    int layoutID;


    public MyCursorAdapter(Context context, MyMatrixCursor c, int flags, int[] ids, int layoutID) {
        super(context, c, flags);
        this.ids = ids;
        this.layoutID = layoutID;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutID, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setId(cursor.getInt(0));
        for (int i = 1; i < ids.length; i++) {
            if (cursor.getColumnName(i).equals("time_edited")) {
                ((TextView) view.findViewById(ids[i])).setText(Constants.timeDiff(cursor.getString(i)));
            } else ((TextView) view.findViewById(ids[i])).setText(cursor.getString(i));
        }
    }


}
