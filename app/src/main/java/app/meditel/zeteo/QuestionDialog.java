package app.meditel.zeteo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionDialog extends Activity {

    EditText body, tags;
    TextView notif;
    ColorStateList notifColor;
    Integer inserted, LIM, questionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra("_id")) {
            setContentView(R.layout.question_dialog);
            tags = ((EditText) findViewById(R.id.tags));
            LIM = 100;
        } else {
            setContentView(R.layout.answer_dialog);
            questionId = getIntent().getIntExtra("_id", 0);
            LIM = 20;
        }
        notif = ((TextView) findViewById(R.id.rem));
        notifColor = notif.getTextColors();
        notif.setTextColor(Color.RED);
        body = ((EditText) findViewById(R.id.body));
        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                monitorBody();
            }
        });
        body.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    monitorBody();
                }
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        getWindow().setLayout((8 * width) / 10, (5 * height) / 10);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void monitorBody() {
        if (body.getText().length() < LIM) {
            notif.setTextColor(Color.RED);
            notif.setText((LIM - body.getText().length()) + "");
        } else {
            notif.setText("0");
            notif.setTextColor(notifColor);
        }
    }


    public void changeLayout(View view) {
        finish();
        Intent intent = new Intent(this, QuestionDialog.class);
        startActivity(intent);
    }

    public void sendPost(View view) {
        if (tags.getText().length() > 0 &&
                body.getText().length() >= LIM) {
            InputMethodManager inputManager = (InputMethodManager)
                    this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            findViewById(R.id.rootview).setVisibility(View.GONE);
            findViewById(R.id.anim).setVisibility(View.VISIBLE);
            final JsonReadTask task = new JsonReadTask() {
                @Override
                public void ListDrawer() {
                    if (jsonResult != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(jsonResult);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            inserted = jsonObject.optInt("LAST_INSERT_ID()");
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/add_question.php",
                    body.getText().toString(), tags.getText().toString(),
                    Constants.userId.toString());
            new CountDownTimer(60000, 300) {
                int i = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    ((TextView) findViewById(R.id.anim)).setText(
                            "" + Constants.appname.substring(0, i % 3 + 1));
                    if (task.getStatus() == AsyncTask.Status.FINISHED) {
                        findViewById(R.id.rootview).setVisibility(View.VISIBLE);
                        findViewById(R.id.anim).setVisibility(View.GONE);
                        cancel();
                    }
                }

                @Override
                public void onFinish() {
                    findViewById(R.id.rootview).setVisibility(View.VISIBLE);
                    findViewById(R.id.anim).setVisibility(View.GONE);
                    task.cancel(true);
                }
            }.start();
        }
    }

    public void close(View view) {
        finish();
    }

    public void sendAnswer(View view) {
        if (body.getText().length() >= LIM) {
            InputMethodManager inputManager = (InputMethodManager)
                    this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            findViewById(R.id.rootview).setVisibility(View.GONE);
            findViewById(R.id.anim).setVisibility(View.VISIBLE);
            final JsonReadTask task = new JsonReadTask() {
                @Override
                public void ListDrawer() {
                    if (jsonResult != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(jsonResult);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            inserted = jsonObject.optInt("LAST_INSERT_ID()");
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/add_answer.php",
                    body.getText().toString(), questionId.toString(), Constants.userId.toString());
            new CountDownTimer(60000, 300) {
                int i = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    ((TextView) findViewById(R.id.anim)).setText(
                            "" + Constants.appname.substring(0, i % 3 + 1));
                    if (task.getStatus() == AsyncTask.Status.FINISHED) {
                        findViewById(R.id.rootview).setVisibility(View.VISIBLE);
                        findViewById(R.id.anim).setVisibility(View.GONE);
                        cancel();
                    }
                }

                @Override
                public void onFinish() {
                    findViewById(R.id.rootview).setVisibility(View.VISIBLE);
                    findViewById(R.id.anim).setVisibility(View.GONE);
                    task.cancel(true);
                }
            }.start();
        }
    }
}
