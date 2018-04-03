package tk.vn.app.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.CourseDetailFetchTask;
import tk.vn.app.com.CourseListFetchTask;
import tk.vn.app.com.CourseSubscribeTask;
import tk.vn.app.com.RunTimeStore;
import tk.vn.app.model.CompactSubscriptionBean;
import tk.vn.app.model.CourseBean;

public class CourseListActivity extends AppCompatActivity {

    @BindView(R.id.course_listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // bind the view using butterknife
        ButterKnife.bind(this);

        // redirect to main activity if already logged in
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(token.isEmpty()){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        CourseListFetchTask task = new CourseListFetchTask(this, new Consumer<CompactSubscriptionBean[]>() {

            @Override
            public void consume(CompactSubscriptionBean[] list) {
                if(list != null){
                    config(list);
                }
                //TODO implement logic for error in fetching profile
            }
        });
        task.fetchSubscriptions();
    }

    private void config(CompactSubscriptionBean[] list){
        final CourseListItemAdapter adapter = new CourseListItemAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long courseId = ((CompactSubscriptionBean)adapterView.getItemAtPosition(i))
                        .getCourseId();
                CourseSubscribeTask courseSubscribeTask = new CourseSubscribeTask(CourseListActivity.this,
                        new Consumer<HttpStatus>() {
                            @Override
                            public void consume(HttpStatus httpStatus) {
                                subscribePostProcessor(httpStatus, courseId);
                            }
                        });


                courseSubscribeTask.subscribe(courseId);
            }
        });
    }

    private void subscribePostProcessor(HttpStatus httpStatus, long courseId){
        if(httpStatus!=null && httpStatus.equals(HttpStatus.ACCEPTED)){
            // created successful
            Log.i("subscribe course","successfully");

            // update preferences
            SharedPreferences sharedPref = getSharedPreferences(Const.DEF_SHARED_PREF,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(Const.SHARED_PREF_COURSE_ID, courseId);
            editor.commit();

            fetchCourseDetail();

            Toast.makeText(this,"subscription successful",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,CourseListActivity.class);
            startActivity(i);
            finish();
        }else{
            // course subscribe fail
            Log.w("subscribe course","subscription fail, response status :- "+httpStatus);
            Toast.makeText(this,"subscription fail",Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCourseDetail(){
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);

        // redirect to main activity if already logged in
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(token.isEmpty()){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        // retrieve courseId
        long courseId = sp.getLong(Const.SHARED_PREF_COURSE_ID, 0);
        if(courseId == 0){
            //TODO implement logic for no course active
            return;
        }

        CourseDetailFetchTask task = new CourseDetailFetchTask(this, new Consumer<CourseBean>() {
            @Override
            public void consume(CourseBean courseBean) {
                if(courseBean != null){
                    RunTimeStore.storeObj(Const.COURSE_DETAIL,courseBean);
                    System.out.println(courseBean);
                }
                //TODO implement logic for error in fetching course
            }
        });
        task.fetchCourseBean(courseId);
    }
}