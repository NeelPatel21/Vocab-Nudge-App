package tk.vn.app.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.simple_rest.s_rest.restapi.request.HeaderTools;
import com.simple_rest.s_rest.restapi.request.SimpleRequest;

import org.springframework.http.HttpMethod;

import tk.vn.app.model.CompactSubscriptionBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class CourseDetailsFetchTask {

    private final Context context;
    private SimpleRequest<CompactSubscriptionBean[]> rt;
    private ProgressDialog progressDialog;
    private CompactSubscriptionBean[] list;
    private Consumer<CompactSubscriptionBean[]> consumer;

    public CourseDetailsFetchTask(@NonNull Context context, Consumer<CompactSubscriptionBean[]> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public void fetchSubscriptions(){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(CourseDetailsFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(CourseDetailsFetchTask.class.getName(),"retrieving list of courses with token :- "+token);
        rt = new SimpleRequest<CompactSubscriptionBean[]>(CompactSubscriptionBean[].class, HttpMethod.GET,
                HeaderTools.ACCEPT_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("retrieving details");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(CompactSubscriptionBean[] list) {
                super.onPostExecute(list);
                progressDialog.dismiss();
                if(consumer != null)
                    consumer.consume(list);
                else
                    Log.i(CourseDetailsFetchTask.class.getName(),"consumer reference null");
            }

        };
        rt.execute(Const.API_BASE_URL+"/courses/");
    }

    public @Nullable CompactSubscriptionBean[] getCompactSubscriptionBean(){
        return list;
    }

    public SimpleRequest<CompactSubscriptionBean[]> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<CompactSubscriptionBean[]> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<CompactSubscriptionBean[]> consumer) {
        this.consumer = consumer;
    }
}
