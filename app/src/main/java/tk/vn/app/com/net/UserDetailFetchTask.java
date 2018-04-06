package tk.vn.app.com.net;

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

import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.model.UserBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class UserDetailFetchTask {

    private final Context context;
    private SimpleRequest<UserBean> rt;
    private ProgressDialog progressDialog;
    private UserBean userBean;
    private Consumer<UserBean> consumer;
    private boolean showProgress;

    public UserDetailFetchTask(@NonNull Context context, Consumer<UserBean> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public UserDetailFetchTask(@NonNull Context context, Consumer<UserBean> consumer, boolean showProgress){
        this(context,consumer);
        this.showProgress = showProgress;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchUserBean(){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(UserDetailFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(UserDetailFetchTask.class.getName(),"retrieving user details with token :- "+token);
        rt = new SimpleRequest<UserBean>(UserBean.class, HttpMethod.GET,
                HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(showProgress){
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please wait");
                    progressDialog.setTitle("loading");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(UserBean userBean) {
                super.onPostExecute(userBean);
                if(showProgress)
                    progressDialog.dismiss();
                UserDetailFetchTask.this.userBean = userBean;
                if(consumer != null)
                    consumer.consume(userBean);
                else
                    Log.i(UserDetailFetchTask.class.getName(),"consumer reference null");
            }

        };
        rt.execute(Const.API_BASE_URL+"/user/profile");
    }

    public @Nullable UserBean getUserBean(){
        return userBean;
    }

    public SimpleRequest<UserBean> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<UserBean> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<UserBean> consumer) {
        this.consumer = consumer;
    }
}
