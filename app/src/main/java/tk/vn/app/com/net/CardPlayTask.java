package tk.vn.app.com.net;

import android.annotation.SuppressLint;
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

import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.model.CardPlayBean;
import tk.vn.app.model.UserBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class CardPlayTask {

    private final Context context;
    private ExtendedRequest<String,CardPlayBean> rt;
    private ProgressDialog progressDialog;
    private HttpStatus httpStatus;
    private Consumer<HttpStatus> consumer;

    public CardPlayTask(@NonNull Context context, Consumer<HttpStatus> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    @SuppressLint("StaticFieldLeak")
    public void createUser(CardPlayBean cardPlayBean){

        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(CourseSubscribeTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(CardPlayTask.class.getName(),"signing up user");
        rt = new ExtendedRequest<String, CardPlayBean>(String.class, cardPlayBean, HttpMethod.POST,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token),
                HeaderTools.CONTENT_TYPE_JSON){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                progressDialog.dismiss();
                CardPlayTask.this.httpStatus = super.getHttpStatus();
                if(consumer != null)
                    consumer.consume(super.getHttpStatus());
                else
                    Log.i(CardPlayTask.class.getName(),"consumer reference null");

            }

        };
        rt.execute(Const.API_BASE_URL+"/courses/card-play");
    }

    public @Nullable HttpStatus getResponseStatus(){
        return httpStatus;
    }

    public ExtendedRequest<String, CardPlayBean> getRt() {
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
