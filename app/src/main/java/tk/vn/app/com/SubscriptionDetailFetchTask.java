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

import tk.vn.app.model.SubscriptionBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class SubscriptionDetailFetchTask {

    private final Context context;
    private SimpleRequest<SubscriptionBean> rt;
    private ProgressDialog progressDialog;
    private SubscriptionBean subscriptionBean;
    private Consumer<SubscriptionBean> consumer;
    private boolean showProgress;

    public SubscriptionDetailFetchTask(@NonNull Context context, Consumer<SubscriptionBean> consumer){
        this.context = context;
        this.consumer = consumer;
    }
    public SubscriptionDetailFetchTask(@NonNull Context context, Consumer<SubscriptionBean> consumer, boolean showProgress){
        this(context,consumer);
        this.showProgress = showProgress;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchSubscriptionBean(long subscriptionId){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(SubscriptionDetailFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(SubscriptionDetailFetchTask.class.getName(),"retrieving subscription detail with token :- "+token);
        rt = new SimpleRequest<SubscriptionBean>(SubscriptionBean.class, HttpMethod.GET,
                HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(showProgress){
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please wait");
                    progressDialog.setTitle("Retrieving Subscription");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(SubscriptionBean subscriptionBean) {
                super.onPostExecute(subscriptionBean);
                if(showProgress)
                    progressDialog.dismiss();
                SubscriptionDetailFetchTask.this.subscriptionBean = subscriptionBean;
                if(consumer != null)
                    consumer.consume(subscriptionBean);
                else
                    Log.i(SubscriptionDetailFetchTask.class.getName(),"consumer reference null");
            }

        };
        rt.execute(Const.API_BASE_URL+"/courses/subscription/"+subscriptionId);
    }

    public @Nullable SubscriptionBean getSubscriptionBean(){
        return subscriptionBean;
    }

    public SimpleRequest<SubscriptionBean> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<SubscriptionBean> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<SubscriptionBean> consumer) {
        this.consumer = consumer;
    }
}
