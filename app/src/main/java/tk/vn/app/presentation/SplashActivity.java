package tk.vn.app.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.CourseDetailFetchTask;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.com.SubscriptionDetailFetchTask;
import tk.vn.app.com.UserDetailFetchTask;
import tk.vn.app.model.CourseBean;
import tk.vn.app.model.SubscriptionBean;
import tk.vn.app.model.UserBean;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // redirect to main activity if already logged in
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(token.isEmpty()){
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }else{
            fetchUserDetail();
        }


    }

    private void startMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void fetchUserDetail(){

        UserDetailFetchTask task = new UserDetailFetchTask(this, new Consumer<UserBean>() {
            @Override
            public void consume(UserBean userBean) {
                if(userBean != null){
                    RunTimeStore.storeObj(Const.USER_DETAIL,userBean);
                    fetchCourseDetail();
                }else{
                    SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(Const.SHARED_PREF_TOKEN);
                    editor.commit();
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                //TODO implement logic for error in fetching profile
            }
        },false);
        task.fetchUserBean();
    }


    private void fetchCourseDetail(){
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);

        // retrieve courseId
        long courseId = sp.getLong(Const.SHARED_PREF_COURSE_ID, 0);
        if(courseId == 0){
            //TODO implement logic for no course active
            fetchSubscriptionDetail();
            return;
        }

        CourseDetailFetchTask task = new CourseDetailFetchTask(this, new Consumer<CourseBean>() {
            @Override
            public void consume(CourseBean courseBean) {
                if(courseBean != null){
                    RunTimeStore.storeObj(Const.COURSE_DETAIL,courseBean);
                    System.out.println(courseBean);
                    fetchSubscriptionDetail();
                }
                //TODO implement logic for error in fetching course
            }
        },false);
        task.fetchCourseBean(courseId);
    }

    private void fetchSubscriptionDetail(){
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);

        // retrieve subscriptionId
        long subscriptionId = sp.getLong(Const.SHARED_PREF_SUBSCRIPTION_ID, 0);
        if(subscriptionId == 0){
            //TODO implement logic for no subscription active
            startMain();
            return;
        }

        SubscriptionDetailFetchTask task = new SubscriptionDetailFetchTask(this, new Consumer<SubscriptionBean>() {
            @Override
            public void consume(SubscriptionBean subscriptionBean) {
                if(subscriptionBean != null){
                    RunTimeStore.storeObj(Const.SUBSCRIPTION_DETAIL,subscriptionBean);
                    System.out.println(subscriptionBean);
                    startMain();
                }
                //TODO implement logic for error in fetching subscription
            }
        },false);
        task.fetchSubscriptionBean(subscriptionId);
    }
}
