package app.meditel.zeteo.main;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import app.meditel.zeteo.CommentDialog;
import app.meditel.zeteo.Constants;
import app.meditel.zeteo.JsonReadTask;
import app.meditel.zeteo.MyCursorAdapter;
import app.meditel.zeteo.MyMatrixCursor;
import app.meditel.zeteo.QuestionDialog;
import app.meditel.zeteo.R;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private FrameLayout mFragment;
    private Button upIt;
    private Button downIt;

    private NavListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = (FrameLayout) findViewById(R.id.content_frame);
//        imageViewUp = (ImageView) findViewById(R.id.thumb_up);
//        imageViewDown = (ImageView) findViewById(R.id.thumb_down);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ExpandableListView) findViewById(R.id.left_drawer);
        mDrawerList = new NavListView(this, mDrawerListView);
        mDrawerList.initContent();


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                v.setSelected(true);
                switch (groupPosition) {
                    case 0:
                        switch (childPosition) {
                            case 0:
                                startProfileActivity();
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                startHomeActivity();
                                break;
                            case 1:
                                startProfileActivity();
                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        startHomeActivity();
    }

    private void startHomeActivity() {
        // update the main content by replacing fragments
        Fragment fragment = new HomeFragment(this);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawers();

    }

    private void startProfileActivity() {
        // update the main content by replacing fragments
        Fragment fragment = new ProfileFragment(this);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawers();

        //setTab(findViewById(R.id.uquestions));
    }

    public void openQuestion(View view) {
        startQuestionActivity(view);
    }

    public void startQuestionActivity(View view) {

        // update the main content by replacing fragments
        Fragment fragment = new QuestionFragment(this, view);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("Main").commit();

        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawers();
    }

    public void toggleAnswer(View view) {
        if (((LinearLayout) view).getChildAt(0).getVisibility() == View.VISIBLE) {
            ((LinearLayout) view).getChildAt(0).setVisibility(View.GONE);
            ((LinearLayout) view).getChildAt(1).setVisibility(View.VISIBLE);
        } else {
            ((LinearLayout) view).getChildAt(0).setVisibility(View.VISIBLE);
            ((LinearLayout) view).getChildAt(1).setVisibility(View.GONE);
        }
        ((ListView) view.getParent()).getLayoutParams().height =
                LinearLayout.LayoutParams.MATCH_PARENT;
    }

    public void showComment(View view) {
        Intent intent = new Intent(this, CommentDialog.class);
        startActivity(intent);
    }

    public void toggleThumbUp(View view) {
        ImageView imageView = (ImageView) view;
        Integer integer = (Integer) imageView.getTag();
        integer = integer == null ? 0 : integer;
        switch (integer) {
            case R.drawable.thumb_up:
            case 0:
                imageView.setImageResource(R.drawable.thumb_up_clicked);
                imageView.setTag(R.drawable.thumb_up_clicked);
                break;
            case R.drawable.thumb_up_clicked:
            default:
                imageView.setImageResource(R.drawable.thumb_up);
                imageView.setTag(R.drawable.thumb_up);
                break;
        }
    }

    public void toggleThumb(View view) {

        upIt = (Button)findViewById(R.id.buttonUp);
        downIt = (Button)findViewById(R.id.buttonDown);
        TextView total = (TextView)findViewById(R.id.total_votes);
        int tot = Integer.valueOf(String.valueOf(total.getText()));

        JsonReadTask taskVoteThumb = new JsonReadTask(){
            @Override
            public void ListDrawer() {
            }
        };

        if (view.getId() == R.id.buttonUp)
        {
            Integer integer = (Integer) upIt.getTag();
            integer = integer == null ? 0 : integer;
             switch (integer) {
                case R.drawable.thumb_up:
                case 0:
                    try{
                        tot++;
                        taskVoteThumb.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_vote.php",
                                String.valueOf(((ViewGroup)getFragmentManager().findFragmentById(R.id.content_frame).getView()).getChildAt(1).getId()),
                                String.valueOf(Constants.userId),String.valueOf(1));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    upIt.setBackgroundResource(R.drawable.thumb_up_clicked);
                    upIt.setTag(R.drawable.thumb_up_clicked);
                    if((Integer) downIt.getTag() == R.drawable.thumb_down_clicked){
                        tot++;
                        downIt.setBackgroundResource(R.drawable.thumb_down);
                        downIt.setTag(R.drawable.thumb_down);
                    }
                    break;
                case R.drawable.thumb_up_clicked:
                default:
                    try{
                        tot--;
                        taskVoteThumb.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_vote.php",
                                String.valueOf(((ViewGroup)getFragmentManager().findFragmentById(R.id.content_frame).getView()).getChildAt(1).getId()),
                                String.valueOf(Constants.userId),String.valueOf(0));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    upIt.setBackgroundResource(R.drawable.thumb_up);
                    upIt.setTag(R.drawable.thumb_up);
                    break;

            }
            total.setText(String.valueOf(tot));

        }
        else{
            System.out.println("down button");
            Integer integer = (Integer) downIt.getTag();
            integer = integer == null ? 0 : integer;

            switch (integer) {
                case R.drawable.thumb_down:
                case 0:
                    try{
                        tot--;
                        taskVoteThumb.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_vote.php",
                                String.valueOf(((ViewGroup)getFragmentManager().findFragmentById(R.id.content_frame).getView()).getChildAt(1).getId()),
                                String.valueOf(Constants.userId),String.valueOf(-1));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    downIt.setBackgroundResource(R.drawable.thumb_down_clicked);
                    downIt.setTag(R.drawable.thumb_down_clicked);
                    if((Integer) upIt.getTag() == R.drawable.thumb_up_clicked){
                        tot--;
                        upIt.setBackgroundResource(R.drawable.thumb_up);
                        upIt.setTag(R.drawable.thumb_up);
                    }
                    break;
                case R.drawable.thumb_down_clicked:
                default:
                    try{
                        tot++;
                        taskVoteThumb.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/user_vote.php",
                                String.valueOf(((ViewGroup)getFragmentManager().findFragmentById(R.id.content_frame).getView()).getChildAt(1).getId()),
                                String.valueOf(Constants.userId),String.valueOf(0));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    downIt.setBackgroundResource(R.drawable.thumb_down);
                    downIt.setTag(R.drawable.thumb_down);
                    break;
            }
            total.setText(String.valueOf(tot));
        }
    }

    public void toggleThumbDown(View view) {
        ImageView imageView = (ImageView) view;
        Integer integer = (Integer) imageView.getTag();
        integer = integer == null ? 0 : integer;
        switch (integer) {
            case R.drawable.thumb_down:
            case 0:
                downIt.setBackgroundResource(R.drawable.thumb_down_clicked);
                downIt.setTag(R.drawable.thumb_down_clicked);
                upIt.setBackgroundResource(R.drawable.thumb_up);
                upIt.setTag(R.drawable.thumb_up);
                break;
            case R.drawable.thumb_down_clicked:
            default:
                downIt.setBackgroundResource(R.drawable.thumb_down);
                downIt.setTag(R.drawable.thumb_down);
                break;
        }
        System.out.println("We are here 2");
    }

    public void setRightAnswer(View view) {
        Button star = (Button) view;
        System.out.println("inside star !! ");
        JsonReadTask rightAnswer = new JsonReadTask(){
            @Override
            public void ListDrawer() {
            }
        };
        Integer integer = (Integer) star.getTag();
        integer = integer == null ? 0 : integer;
        View lyt = (View) star.getParent().getParent();
        System.out.println(lyt.getId());
        if(integer == R.drawable.star_not || integer == 0){
            star.setBackgroundResource(R.drawable.star);
            star.setTag(R.drawable.star);
            try{
                rightAnswer.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/right_answer.php",
                        String.valueOf(11),
                        String.valueOf(1));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            rightAnswer.execute("http://" + Constants.staticIp + ":8080/Zeteo/android_connect/right_answer.php",
                    String.valueOf(11),
                    String.valueOf(0));
            star.setBackgroundResource(R.drawable.star_not);
            star.setTag(R.drawable.star_not);
        }
    }

    public void setTab(View view) {
        findViewById(R.id.uquestions).setBackground(null);
        findViewById(R.id.uanswers).setBackground(null);
        findViewById(R.id.uvotes).setBackground(null);
        findViewById(R.id.ucomments).setBackground(null);
        view.setBackground(getResources().getDrawable(R.drawable.header_bg));
        view.setPadding(10, 10, 10, 10);

    }

    public void refresh(View view) {
        ((MyMatrixCursor) ((MyCursorAdapter) ((ListView) findViewById(Constants.listID)).getAdapter()).getCursor()).refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.post_button:
                Intent intent = new Intent(this, QuestionDialog.class);
                if (mFragment.getChildAt(0).getId() == R.id.question)
                    intent.putExtra("_id", ((ViewGroup) mFragment.getChildAt(0)).getChildAt(1).getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}