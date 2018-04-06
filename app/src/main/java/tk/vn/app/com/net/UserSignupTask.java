package tk.vn.app.com.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.simple_rest.s_rest.restapi.request.ExtendedRequest;
import com.simple_rest.s_rest.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.model.UserBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class UserSignupTask {

    private final Context context;
    private ExtendedRequest<String,UserBean> rt;
    private ProgressDialog progressDialog;
    private HttpStatus httpStatus;
    private Consumer<HttpStatus> consumer;

    public UserSignupTask(@NonNull Context context, Consumer<HttpStatus> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public void createUser(UserBean userBean){

        Log.i(UserSignupTask.class.getName(),"signing up user");
        rt = new ExtendedRequest<String, UserBean>(String.class, userBean, HttpMethod.POST,
                HeaderTools.CONTENT_TYPE_JSON){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("signing-up");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                progressDialog.dismiss();
                UserSignupTask.this.httpStatus = super.getHttpStatus();
                if(consumer != null)
                    consumer.consume(super.getHttpStatus());
                else
                    Log.i(UserSignupTask.class.getName(),"consumer reference null");

            }

        };
        rt.execute(Const.API_BASE_URL+"/user/sign-up");
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
