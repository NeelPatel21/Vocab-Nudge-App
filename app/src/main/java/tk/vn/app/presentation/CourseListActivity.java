package tk.vn.app.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.CourseDetailsFetchTask;
import tk.vn.app.model.CompactSubscriptionBean;

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

        CourseDetailsFetchTask task = new CourseDetailsFetchTask(this, new Consumer<CompactSubscriptionBean[]>() {

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
    }
}