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
import tk.vn.app.model.CardBean;
import tk.vn.app.model.CompactSubscriptionBean;

/**
 * Created by neelp on 28-03-2018.
 */

public class CardPlayListFetchTask {

    private final Context context;
    private SimpleRequest<CardBean[]> rt;
    private ProgressDialog progressDialog;
    private CardBean[] list;
    private Consumer<CardBean[]> consumer;
    private boolean showProgress;

    public CardPlayListFetchTask(@NonNull Context context, Consumer<CardBean[]> consumer){
        this.context = context;
        this.consumer = consumer;
    }
    public CardPlayListFetchTask(@NonNull Context context, Consumer<CardBean[]> consumer, boolean showProgress){
        this(context,consumer);
        this.showProgress = showProgress;
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchSubscriptions(){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        long subscriptionId = sp.getLong(Const.SHARED_PREF_SUBSCRIPTION_ID,-1);;

        if(subscriptionId >= 0){
            fetchSubscriptions(subscriptionId);
        }else{
            Log.w(CardPlayListFetchTask.class.getName(),"subscription-id not retrieved from SharedPreferences");
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchSubscriptions(long subscriptionId){
        SharedPreferences sp = context.getSharedPreferences(Const.DEF_SHARED_PREF,
                                    Context.MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN,"");;

        if(token == null || token.isEmpty()){
            Log.w(CardPlayListFetchTask.class.getName(),"token not retrieved from SharedPreferences");
            return;
        }

        Log.i(CardPlayListFetchTask.class.getName(),"retrieving list of card plays with token :- "+token);
        rt = new SimpleRequest<CardBean[]>(CardBean[].class, HttpMethod.GET,
                HeaderTools.ACCEPT_JSON,
                HeaderTools.makeAuthorizationHeader(Const.AUTH_PREFIX+token)){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(showProgress){
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Please wait");
                    progressDialog.setTitle("retrieving data from server");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(CardBean[] list) {
                super.onPostExecute(list);
                if(showProgress)
                    progressDialog.dismiss();
                CardPlayListFetchTask.this.list = list;
                if(consumer != null)
                    consumer.consume(list);
                else
                    Log.i(CardPlayListFetchTask.class.getName(),"consumer reference null");
            }

        };
        rt.execute(Const.API_BASE_URL+"/courses/subscription/"+subscriptionId+"/play-cards");
    }

    public @Nullable CardBean[] getCardBean(){
        return list;
    }

    public SimpleRequest<CardBean[]> getRt() {
        return rt;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public Consumer<CardBean[]> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<CardBean[]> consumer) {
        this.consumer = consumer;
    }
}
