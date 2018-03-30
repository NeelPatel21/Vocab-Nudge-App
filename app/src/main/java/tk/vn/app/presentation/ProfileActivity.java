package tk.vn.app.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.vn.app.R;
import tk.vn.app.com.Const;
import tk.vn.app.com.Consumer;
import tk.vn.app.com.UserDetailFetchTask;
import tk.vn.app.com.UserDetailUpdateTask;
import tk.vn.app.model.UserBean;

public class ProfileActivity extends AppCompatActivity {

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

    @BindView(R.id.update_button)
    Button updateButton;

    UserDetailUpdateTask ut;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // bind the view using butterknife
        ButterKnife.bind(this);

        // redirect to main activity if already logged in
        SharedPreferences sp = getSharedPreferences(Const.DEF_SHARED_PREF,MODE_PRIVATE);
        String token = sp.getString(Const.SHARED_PREF_TOKEN, "");
        if(token.isEmpty()){
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        UserDetailFetchTask task = new UserDetailFetchTask(this, new Consumer<UserBean>() {
            @Override
            public void consume(UserBean userBean) {
                if(userBean != null){
                    usernameView.setText(userBean.getUserName());
                    firstNameView.setText(userBean.getFirstName());
                    lastNameView.setText(userBean.getLastName());
                    emailIdView.setText(userBean.getEmailId());
                }
                //TODO implement logic for error in fetching profile
            }
        });
        task.fetchUserBean();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptUpdate();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptUpdate() {
        if (ut != null) {
            return;
        }

        // Reset errors.
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

        // Check for a valid password
        if (!TextUtils.isEmpty(userBean.getPassword())) {
            if(userBean.getPassword().length()<Const.PASS_LENGTH){
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
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }

        // perform the update attempt.
        ut = new UserDetailUpdateTask(this, new Consumer<HttpStatus>() {
            @Override
            public void consume(HttpStatus httpStatus) {
                updatePostProcessor(httpStatus);
            }
        });
        ut.updateUser(userBean);
    }

    private void updatePostProcessor(HttpStatus httpStatus){

        if(httpStatus!=null && httpStatus.equals(HttpStatus.ACCEPTED)){
            // update successful
            Log.i("Update user profile","performed successfully");
            Toast.makeText(this,"update successful",Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
//            finish();
        }else{
            // update fail
            Log.w("Update user profile","update fail, response status :- "+httpStatus);
            Toast.makeText(this,"update fail",Toast.LENGTH_SHORT).show();
        }
        ut = null;
    }
}
