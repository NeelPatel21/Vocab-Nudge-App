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

    public UserDetailFetchTask(@NonNull Context context, Consumer<UserBean> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public void fetchUserBean(){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.i(UserDetailFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }
        System.setProperty("https.protocols", "TLSv2");

        Log.i(UserDetailFetchTask.class.getName(),"retrieving user details with token :- "+token);
        rt = new SimpleRequest<UserBean>(UserBean.class, HttpMethod.GET,
                HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(UserBean userBean) {
                super.onPostExecute(userBean);
                progressDialog.dismiss();
                if(consumer != null)
                    consumer.consume(userBean);
                System.out.println("check 1");
            }
        };
        rt.execute(Const.API_BASE_URL+"/user/profile");
    }

    public @Nullable UserBean getUserBean(){
        return userBean;
    }

}
