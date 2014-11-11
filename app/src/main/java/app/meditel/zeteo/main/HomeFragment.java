package app.meditel.zeteo.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.meditel.zeteo.Constants;
import app.meditel.zeteo.MyMatrixCursor;
import app.meditel.zeteo.R;

/**
 * Created by FOla Yinka on 1/29/14.
 */
public class HomeFragment extends Fragment {

    private Context context;
    ListView questionStream;
    private String url = "http://" + Constants.staticIp + ":8080/Zeteo/android_connect/select_question_link.php";


    public HomeFragment() {
    }


    public HomeFragment(Context context) {
        this.context = context;
        questionStream = new ListView(context);
        questionStream.setId(Constants.listID);
        new MyMatrixCursor(context, questionStream, R.layout.question_link,
                new String[]{"_id", "user_name", "body", "total_votes", "answers", "comments", "time_edited"},
                new int[]{0, R.id.user_name, R.id.body, R.id.votes, R.id.answers, R.id.comments, R.id.time},
                new String[]{url});
        questionStream.setDivider(null);
        questionStream.setVerticalScrollBarEnabled(false);
        questionStream.setHorizontalScrollBarEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return questionStream;
    }

}
