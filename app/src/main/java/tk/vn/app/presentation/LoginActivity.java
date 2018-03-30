package tk.vn.app.presentation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simple_rest.s_rest.restapi.request.ExtendedRequest;
import com.simple_rest.s_rest.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.model.LoginBean;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;
    ExtendedRequest<String,LoginBean> rt;

    // UI references.
    @BindView(R.id.login_username)
    EditText mUsernameView;

    @BindView(R.id.login_password)
    EditText mPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.username_login_form)
    View mLoginFormView;

    @BindView(R.id.login_sign_in_button)
    Button mSignInButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // bind the view using butterknife
        ButterKnife.bind(this);

        // redirect to main activity if already logged in
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(!token.isEmpty()){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (rt != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        else if (!isUsernameValid(username)) {
            mUsernameView.setError("This username is invalid");
            focusView = mUsernameView;
            cancel = true;
        }
        
        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.

        LoginBean loginBean = new LoginBean(username,password);
        rt = new ExtendedRequest<String, LoginBean>(String.class, loginBean, HttpMethod.POST,
                HeaderTools.CONTENT_TYPE_JSON){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.setTitle("Signing up");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loginPostProcessor();
                progressDialog.dismiss();
            }
        };
        rt.execute(Const.API_BASE_URL+"/login");
    }

    private void loginPostProcessor(){
        String token = rt.getObj();

        // terminate waiting logic
        HttpStatus status = rt.getHttpStatus();
        Log.i("Login","performed successfully, token:- "+token);
        if(status!=null && status.equals(HttpStatus.OK)){
            //TODO login successful
            SharedPreferences sharedPref = getSharedPreferences(Const.DEF_SHARED_PREF,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Const.SHARED_PREF_TOKEN, token);
            editor.commit();
            Toast.makeText(this,"login successful",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            //TODO login fail
            Log.w("Login","fail, response status :- "+status);
            Toast.makeText(this,"login fail",Toast.LENGTH_SHORT).show();
        }
        rt = null;
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this
        return password.length() > 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final LoginBean loginBean;
//
//        UserLoginTask(LoginBean loginBean) {
//            this.loginBean = loginBean;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mUsername)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                Toast.makeText(LoginActivity.this,"Sign in fail, Username or Password wrong",Toast.LENGTH_LONG).show();
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mUsernameView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

