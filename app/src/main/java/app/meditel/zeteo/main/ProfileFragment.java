package app.meditel.zeteo.main;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.meditel.zeteo.Constants;
import app.meditel.zeteo.JsonReadTask;
import app.meditel.zeteo.MyMatrixCursor;
import app.meditel.zeteo.R;

/**
 * Created by FOla Yinka on 1/29/14.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    ListView qList, aList, vList, cList;
    ViewGroup content;
    Context context;


    public ProfileFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences preferences = context.getSharedPreferences("zeteo", Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.profile_page, container, false);
        content = (ViewGroup) rootView;

        ((TextView) rootView.findViewById(R.id.user_questions)).setText("" + preferences.getInt("questions", 0));
        ((TextView) rootView.findViewById(R.id.user_answers)).setText("" + preferences.getInt("answers", 0));
        ((TextView) rootView.findViewById(R.id.user_votes)).setText("" + preferences.getInt("votes", 0));
        ((TextView) rootView.findViewById(R.id.user_comments)).setText("" + preferences.getInt("comments", 0));

        accessWebService("user_questions");

        qList = new ListView(context);
        qList.setId(Constants.listID);

        new MyMatrixCursor(context, qList, R.layout.post_link_profile_questions,
                new String[]{"_id", "user_name", "body", "total_votes", "answers", "comments", "time_edited"},
                new int[]{0, R.id.user_name, R.id.body, R.id.votes, R.id.answers, R.id.comments, R.id.time},
                new String[]{"http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_questions.php", Constants.userId.toString()}
        );

        qList.setVerticalScrollBarEnabled(false);
        qList.setHorizontalScrollBarEnabled(false);
        ((ViewGroup) rootView).addView(qList);

        return rootView;
    }

    public void accessWebService(String url) {
        JsonReadTask task = new JsonReadTask() {
            @Override
            public void ListDrawer() {
                try {
                    JSONArray jsonMainNode = new JSONArray(jsonResult);
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        ((TextView) content.findViewById(R.id.user_questions)).setText(jsonChildNode.optString("questions"));
                        ((TextView) content.findViewById(R.id.user_answers)).setText(jsonChildNode.optString("answers"));
                        ((TextView) content.findViewById(R.id.user_votes)).setText(jsonChildNode.optString("votes"));
                        ((TextView) content.findViewById(R.id.user_comments)).setText(jsonChildNode.optString("comments"));
                        SharedPreferences preferences = context.getSharedPreferences("zeteo", Context.MODE_PRIVATE);
                        preferences.edit().putInt("questions", jsonChildNode.optInt("questions")).commit();
                        preferences.edit().putInt("answers", jsonChildNode.optInt("answers")).commit();
                        preferences.edit().putInt("votes", jsonChildNode.optInt("votes")).commit();
                        preferences.edit().putInt("comments", jsonChildNode.optInt("comments")).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_values.php", Constants.userId.toString());
    }
}
