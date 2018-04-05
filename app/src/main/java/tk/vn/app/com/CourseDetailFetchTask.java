package tk.vn.app.com;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.simple_rest.s_rest.restapi.request.HeaderTools;
import com.simple_rest.s_rest.restapi.request.SimpleRequest;

import org.springframework.http.HttpMethod;

import tk.vn.app.model.CourseBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class CourseDetailFetchTask {

    private final Context context;
    private SimpleRequest<CourseBean> rt;
    private ProgressDialog progressDialog;
    private CourseBean courseBean;
    private Consumer<CourseBean> consumer;
    private boolean showProgress;

    public CourseDetailFetchTask(@NonNull Context context, Consumer<CourseBean> consumer){
        this.context = context;
        this.consumer = consumer;
    }
    public CourseDetailFetchTask(@NonNull Context context, Consumer<CourseBean> consumer, boolean showProgress){
        this(context,consumer);
        this.showProgress = showProgress;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchCourseBean(long courseId){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(CourseDetailFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(CourseDetailFetchTask.class.getName(),"retrieving course detail with token :- "+token);
        rt = new SimpleRequest<CourseBean>(CourseBean.class, HttpMethod.GET,
                HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(showProgress) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please wait");
                    progressDialog.setTitle("Retrieving Course");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(CourseBean courseBean) {
                super.onPostExecute(courseBean);
                if(showProgress)
                    progressDialog.dismiss();
                CourseDetailFetchTask.this.courseBean = courseBean;
                if(consumer != null)
                    consumer.consume(courseBean);
                else
                    Log.i(CourseDetailFetchTask.class.getName(),"consumer reference null");
            }

        };
        rt.execute(Const.API_BASE_URL+"/courses/course/"+courseId);
    }

    public @Nullable CourseBean getCourseBean(){
        return courseBean;
    }

    public SimpleRequest<CourseBean> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<CourseBean> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<CourseBean> consumer) {
        this.consumer = consumer;
    }
}
