package tk.vn.app.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.simple_rest.s_rest.restapi.request.ExtendedRequest;
import com.simple_rest.s_rest.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import tk.vn.app.model.UserBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class UserDetailUpdateTask {

    private final Context context;
    private ExtendedRequest<String,UserBean> rt;
    private ProgressDialog progressDialog;
    private HttpStatus httpStatus;
    private Consumer<HttpStatus> consumer;

    public UserDetailUpdateTask(@NonNull Context context, Consumer<HttpStatus> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public void updateUser(UserBean userBean){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(UserDetailUpdateTask.class.getName(),"token not retrieved from SharedPreferences");
        }

        Log.i(UserDetailUpdateTask.class.getName(),"retrieving user details with token :- "+token);
        rt = new ExtendedRequest<String, UserBean>(String.class, userBean, HttpMethod.PUT,
                HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){
//        rt = new ExtendedRequest<String, String>(String.class,"", HttpMethod.GET,
//                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("updating");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                progressDialog.dismiss();
                UserDetailUpdateTask.this.httpStatus = super.getHttpStatus();
                if(consumer != null)
                    consumer.consume(super.getHttpStatus());
                else
                    Log.i(UserDetailUpdateTask.class.getName(),"consumer reference null");

            }

        };
        rt.execute(Const.API_BASE_URL+"/user/update");
    }

    public @Nullable HttpStatus getResponseStatus(){
        return httpStatus;
    }

    public ExtendedRequest<String, UserBean> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<HttpStatus> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<HttpStatus> consumer) {
        this.consumer = consumer;
    }
}
