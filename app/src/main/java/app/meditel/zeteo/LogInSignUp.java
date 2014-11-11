package app.meditel.zeteo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

import static app.meditel.zeteo.CommonUtilities.SENDER_ID;
import static app.meditel.zeteo.CommonUtilities.SERVER_URL;

import org.json.JSONArray;
import org.json.JSONObject;

import app.meditel.zeteo.main.MainActivity;

public class LogInSignUp extends Activity {

    SharedPreferences preferences;
//    final Context context = this;
    boolean loggedIn = false;
    boolean isLogin = false;
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Internet detector
    ConnectionDetector cd;
    EditText username, password, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        System.out.println("Inside onCreat");
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(LogInSignUp.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(LogInSignUp.this, "Configuration Error!",
                    "Server URL and GCM Sender ID", false);
            // stop executing code by return
            return;
        }

        new CountDownTimer(3000, 200) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                ((TextView) findViewById(R.id.anim)).setText(
                        "" + Constants.appname.substring(0, i % 3 + 1));
            }

            @Override
            public void onFinish() {
                System.out.println("Inside onFinish, gonna start");
                _start();
            }
        }.start();
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        pass = (EditText) findViewById(R.id.confirm_password1);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().length() == 0)
                    username.setError(null);
                else if (!username.getText().toString().matches("^[a-zA-Z].*$")) {
                    username.setError("Must start with an alphabet");
                } else if (username.getText().length() > 20) {
                    username.setError("" + (20 - username.length()));
                } else if (username.getText().length() < 4) {
                    username.setError("" + (username.length() - 4));
                } else
                    username.setError(null);
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().length() == 0)
                    password.setError(null);
                else if (password.getText().length() > 20)
                    password.setError("" + (20 - password.length()));
                else if (password.getText().length() < 6) {
                    password.setError("" + (password.length() - 6));
                } else
                    password.setError(null);
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pass.getText().length() == 0)
                    pass.setError(null);
                else if (pass.length() >= password.getText().length() &&
                        !password.getText().toString().equals(pass.getText().toString()))
                    pass.setError("doesn't match");
                else
                    pass.setError(null);
            }
        });
    }

    void _start() {
        System.out.println("Inside _start");
        preferences = getSharedPreferences("zeteo", MODE_PRIVATE);
        Constants.userId = preferences.getInt("_id", 0);
        Constants.userName = preferences.getString("user_name", "");
        toggleLogIn(null);
        if (Constants.userName.length() > 0 && Constants.userId > 0) {
            accessWebService();
        } else {
            System.out.println("we can't access the webService, sign-up appear");
            ((TextView) findViewById(R.id.anim)).setText("");
            ((LinearLayout) findViewById(R.id.sign_up)).setVisibility(View.VISIBLE);
        }
    }

    public void accessWebService() {
        System.out.println("accessing WebService");
        JsonReadTask task = new JsonReadTask() {
            @Override
            public void ListDrawer() {
                if (jsonResult != null && !jsonResult.equals("[]")) {
                    try {
                        JSONObject jsonChildNode = (new JSONArray(jsonResult)).getJSONObject(0);
                        Constants.userName = jsonChildNode.optString("user_name");
                        Constants.userId = jsonChildNode.optInt("_id");
                        preferences.edit().putInt("_id", Constants.userId).commit();
                        preferences.edit().putString("user_name", Constants.userName).commit();
                        finish();
                        startHomeActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.anim)).setText("");
                    username.setText(Constants.userName);
                }
            }
        };
        task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/login_pref.php", Constants.userId.toString(), Constants.userName);
    }

    public void toggleLogIn(View view) {
        System.out.println("just a toggle of fields");
        isLogin = !isLogin;
        if (isLogin) {
            ((TextView) findViewById(R.id.islogin)).setText(Html.fromHtml(Constants.signup));
            findViewById(R.id.confirm_password).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.start)).setText("LOG IN");
        } else {
            ((TextView) findViewById(R.id.islogin)).setText(Html.fromHtml(Constants.login));
            findViewById(R.id.confirm_password).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.start)).setText("START");
        }
    }

    public void logInSignUp(View view) {
        System.out.println("onClick the button");
        if (isLogin){
            System.out.println("try Login");
            tryLogin();
        }
        else{
            System.out.println("try sign up");
            trySignUp();
        }
    }

    private void trySignUp() {
        System.out.println("Inside trySignUp");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
//        GCMRegistrar.checkManifest(this);
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
        System.out.println("regId = "+ regId);
        // Check if regid already presents
        if (regId.equals("")) {
            System.out.println(regId);
            // Registration is not present, register now with GCM
            System.out.println("Inside trySignUp");
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
            }
        }

        if (username.getText().length() >= 4 && username.getText().length() <= 20 &&
                password.getText().length() >= 6 && password.getText().length() <= 20 &&
                pass.getText().length() >= 6 && pass.getText().length() <= 20 &&
                password.getText().toString().equals(pass.getText().toString())
                ) {
            InputMethodManager inputManager = (InputMethodManager)
                    this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
            final JsonReadTask task = new JsonReadTask() {
                @Override
                public void ListDrawer() {
                    if (jsonResult != null && !jsonResult.equals("[]")) {
                        try {

                            GCMRegistrar.setRegisteredOnServer(getApplicationContext(),true);
                            String message = getApplicationContext().getString(R.string.server_registered);
                            CommonUtilities.displayMessage(getApplicationContext(), message);
                            JSONObject jsonChildNode = (new JSONArray(jsonResult)).getJSONObject(0);
                            Constants.userName = jsonChildNode.optString("user_name");
                            Constants.userId = jsonChildNode.optInt("_id");
                            preferences.edit().putInt("_id", Constants.userId).commit();
                            preferences.edit().putString("user_name", Constants.userName).commit();
                            startHomeActivity();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        password.setError("Incorrect!");
                        username.setError("Incorrect!");
                        pass.setError("Incorrect!");

                    }
                }
            };
            new CountDownTimer(60000, 300) {
                int i = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    if (i == 6) {
                        System.out.println(regId.toString());
                        task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/sign_up.php",
                                username.getText().toString(),
                                password.getText().toString(),
                                pass.getText().toString(),
                                regId);
                    }
                    ((TextView) findViewById(R.id.anim)).setText(
                            "" + Constants.appname.substring(0, i % 3 + 1));
                    if (task.getStatus() == AsyncTask.Status.FINISHED) {
                        findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.anim)).setText("");
                        cancel();
                    }
                }

                @Override
                public void onFinish() {
                    findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.anim)).setText("");
                    task.cancel(true);

                }
            }.start();
        }

    }

    private void tryLogin() {
        InputMethodManager inputManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (username.getText().length() >= 4 && username.getText().length() <= 20 &&
                password.getText().length() >= 6 && password.getText().length() <= 20) {
            findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
            final JsonReadTask task = new JsonReadTask() {
                @Override
                public void ListDrawer() {
                    if (jsonResult != null && !jsonResult.equals("[]")) {
                        try {
                            JSONObject jsonChildNode = (new JSONArray(jsonResult)).getJSONObject(0);
                            Constants.userName = jsonChildNode.optString("user_name");
                            Constants.userId = jsonChildNode.optInt("_id");
                            preferences.edit().putInt("_id", Constants.userId).commit();
                            preferences.edit().putString("user_name", Constants.userName).commit();
                            startHomeActivity();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        password.setError("Incorrect!");
                        username.setError("Incorrect!");
                    }
                }
            };
            new CountDownTimer(60000, 300) {
                int i = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    i++;
                    if (i == 6) {
                        task.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/login.php",
                                username.getText().toString(),
                                password.getText().toString());
                    }
                    ((TextView) findViewById(R.id.anim)).setText(
                            "" + Constants.appname.substring(0, i % 3 + 1));
                    if (task.getStatus() == AsyncTask.Status.FINISHED) {
                        findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.anim)).setText("");
                        cancel();
                    }
                }

                @Override
                public void onFinish() {
                    findViewById(R.id.sign_up).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.anim)).setText("");
                    task.cancel(true);
                }
            }.start();
        }
    }

    private void startHomeActivity() {
        findViewById(R.id.sign_up).setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
