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
import org.springframework.http.HttpStatus;

/**
 * Created by neelp on 28-03-2018.
 */

public class LogoutTask {

    private final Context context;
    private SimpleRequest<String> rt;
    private ProgressDialog progressDialog;
    private HttpStatus httpStatus;
    private Consumer<HttpStatus> consumer;

    public LogoutTask(@NonNull Context context, Consumer<HttpStatus> consumer){
        this.context = context;
        this.consumer = consumer;
    }

    public void logout(){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(LogoutTask.class.getName(),"token not retrieved from SharedPreferences");
        }

        Log.i(LogoutTask.class.getName(),"performing logout with token :- "+token);
        rt = new SimpleRequest<String>(String.class, HttpMethod.DELETE,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("signing out");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                progressDialog.dismiss();
                LogoutTask.this.httpStatus = super.getHttpStatus();
                if(consumer != null)
                    consumer.consume(super.getHttpStatus());
                else
                    Log.i(LogoutTask.class.getName(),"consumer reference null");

            }

        };
        rt.execute(Const.API_BASE_URL+"/auth/logout");
    }

    public @Nullable HttpStatus getResponseStatus(){
        return httpStatus;
    }

    public SimpleRequest<String> getRt() {
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
