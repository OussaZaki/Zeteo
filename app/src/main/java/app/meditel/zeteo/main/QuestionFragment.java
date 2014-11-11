package app.meditel.zeteo.main;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import app.meditel.zeteo.Constants;
import app.meditel.zeteo.HeaderFooter;
import app.meditel.zeteo.JsonReadTask;
import app.meditel.zeteo.MyMatrixCursor;
import app.meditel.zeteo.R;

/**
 * Created by FOla Yinka on 1/29/14.
 */
public class QuestionFragment extends Fragment {

    ListView answers;
    ViewGroup content;
    Context context;
    Integer id;
    public Integer voteType;

    public QuestionFragment() {
    }


    public QuestionFragment(Context context, View view) {
        this.context = context;
        content = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.question, HeaderFooter.getContent(), false);

        content.getChildAt(1).setId(view.getId());
        ((TextView) content.findViewById(R.id.body)).setText(((TextView) view.findViewById(R.id.body)).getText());
        ((TextView) content.findViewById(R.id.comments)).setText(((TextView) view.findViewById(R.id.comments)).getText());
        ((TextView) content.findViewById(R.id.total_votes)).setText(((TextView) view.findViewById(R.id.votes)).getText());
        ((TextView) content.findViewById(R.id.answers)).setText(((TextView) view.findViewById(R.id.answers)).getText());
        ((TextView) content.findViewById(R.id.time)).setText(((TextView) view.findViewById(R.id.time)).getText());
        ((TextView) content.findViewById(R.id.user_name)).setText(((TextView) view.findViewById(R.id.user_name)).getText());
        ((Button) content.findViewById(R.id.buttonDown)).setTag(0);
        ((Button) content.findViewById(R.id.buttonUp)).setTag(0);
        answers = new ListView(context);
        answers.setId(Constants.listID);
        new MyMatrixCursor(context, answers, R.layout.answer,
                new String[]{"_id", "user_name", "body", "total_votes", "comments", "time_edited","good_answer", "user_name", "body", "total_votes", "comments", "time_edited"},
                new int[]{0, R.id.user_name, R.id.body, R.id.total_votes, R.id.comments, R.id.time, R.id.good_answer, R.id.user_name1, R.id.body1, R.id.total_votes1, R.id.comments1, R.id.time1},
                new String[]{"http://" + Constants.staticIp + ":8080/Zeteo/android_connect/select_answers.php", String.valueOf(view.getId())}
        );
        // getting the vote type
        JsonReadTask taskVote = new JsonReadTask(){
            @Override
        public void ListDrawer() {
            if (jsonResult != null && !jsonResult.equals("[]")) {
                try {
                    JSONObject jsonChildNode = (new JSONArray(jsonResult)).getJSONObject(0);
                    voteType = jsonChildNode.optInt("type");
                    if(voteType == 1){
                        ((Button) content.findViewById(R.id.buttonUp)).setBackgroundResource(R.drawable.thumb_up_clicked);
                        ((Button) content.findViewById(R.id.buttonUp)).setTag(R.drawable.thumb_up_clicked);

                    }
                    else if(voteType == -1){
                        ((Button) content.findViewById(R.id.buttonDown)).setBackgroundResource(R.drawable.thumb_down_clicked);
                        ((Button) content.findViewById(R.id.buttonDown)).setTag(R.drawable.thumb_down_clicked);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //TO DO
            }
        }
        };

        taskVote.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_vote.php",
                String.valueOf(view.getId()),
                String.valueOf(Constants.userId));

        //
        answers.setDivider(null);
        answers.setVerticalScrollBarEnabled(false);
        answers.setHorizontalScrollBarEnabled(false);
        content.addView(answers);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return content;
    }


}
