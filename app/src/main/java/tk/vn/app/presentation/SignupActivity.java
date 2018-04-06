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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simple_rest.s_rest.restapi.request.ExtendedRequest;
import com.simple_rest.s_rest.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.net.UserSignupTask;
import tk.vn.app.model.LoginBean;
import tk.vn.app.model.UserBean;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.username_editText)
    EditText usernameView;

    @BindView(R.id.email_editText)
    EditText emailIdView;

    @BindView(R.id.firstName_editText)
    EditText firstNameView;

    @BindView(R.id.lastName_editText)
    EditText lastNameView;

    @BindView(R.id.passwd_editText)
    EditText passView;

    @BindView(R.id.confirm_passwd_editText)
    EditText conPassView;

    @BindView(R.id.signup_button)
    Button signUpButton;

    UserSignupTask st;

    ExtendedRequest<String,LoginBean> rt;

    @BindView(R.id.sign_in_login_text)
    TextView signinText;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        signinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignup() {
        if (st != null) {
            return;
        }

        // Reset errors.
        usernameView.setError(null);
        emailIdView.setError(null);
        firstNameView.setError(null);
        lastNameView.setError(null);
        passView.setError(null);
        conPassView.setError(null);

        // Store values at the time of the login attempt.
        UserBean userBean = new UserBean();
        userBean.setUserName(usernameView.getText().toString());
        userBean.setEmailId(emailIdView.getText().toString());
        userBean.setFirstName(firstNameView.getText().toString());
        userBean.setLastName(lastNameView.getText().toString());
        userBean.setPassword(passView.getText().toString());
        String conPass = conPassView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid username.
        if (TextUtils.isEmpty(userBean.getUserName())) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }
        else if (!isUsernameValid(userBean.getUserName())) {
            usernameView.setError("This username is invalid");
            focusView = usernameView;
            cancel = true;
        }
        
        // Check for a valid emailId.
        if (TextUtils.isEmpty(userBean.getEmailId())) {
            emailIdView.setError(getString(R.string.error_field_required));
            focusView = emailIdView;
            cancel = true;
        }
        else if (!isUsernameValid(userBean.getEmailId())) {
            emailIdView.setError("This emailId is invalid");
            focusView = emailIdView;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(userBean.getPassword())){
            // length of password
            passView.setError("password is required");
            focusView = passView;
            cancel = true;
        }else if(userBean.getPassword().length()< Const.PASS_LENGTH){
            // length of password
            passView.setError("password to short");
            focusView = passView;
            cancel = true;
        }else if(TextUtils.isEmpty(conPass)) {
            // confirm password
            conPassView.setError("confirm password required");
            focusView = conPassView;
            cancel = true;
        }else if(!userBean.getPassword().equals(conPass)) {
            // matching password & confirm password
            conPassView.setError("confirm password is not matching");
            focusView = conPassView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }

        // perform the update attempt.
        st = new UserSignupTask(this, new Consumer<HttpStatus>() {
            @Override
            public void consume(HttpStatus httpStatus) {
                signUpPostProcessor(httpStatus);
            }
        });
        st.createUser(userBean);
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this
        return username.length() > 4;
    }

    private boolean isEmailValid(String emailId) {
        //TODO: Replace this
        return emailId.length() > 6;
    }

    private void signUpPostProcessor(HttpStatus httpStatus){
        if(httpStatus!=null && httpStatus.equals(HttpStatus.CREATED)){
            // created successful
            Log.i("sign-up user","successfully");
            Toast.makeText(this,"sign-up successful",Toast.LENGTH_SHORT).show();
            attemptLogin(usernameView.getText().toString(),passView.getText().toString());
        }else{
            // sign up fail
            Log.w("sign-up user","sign-up fail, response status :- "+httpStatus);
            Toast.makeText(this,"sign-up fail",Toast.LENGTH_SHORT).show();
        }
        st = null;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String username,String password) {
        if (rt != null) {
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
                progressDialog = new ProgressDialog(SignupActivity.this);
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
        Log.i("Sign up, Login","performed successfully, token:- "+token);
        if(status!=null && status.equals(HttpStatus.OK)){
            //TODO login successful
            SharedPreferences sharedPref = getSharedPreferences(Const.DEF_SHARED_PREF,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(Const.SHARED_PREF_TOKEN, token);
            editor.commit();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            //TODO login fail
            Log.w("Sign up, Login","fail, response status :- "+status);
            Toast.makeText(this,"login fail",Toast.LENGTH_SHORT).show();
        }
        st = null;
    }

}
