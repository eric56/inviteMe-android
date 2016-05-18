package br.com.android.invviteme.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.List;

import br.com.android.invviteme.R;
import br.com.android.invviteme.animations.HideShowProgressBar;
import br.com.android.invviteme.constants.ApiCall;
import br.com.android.invviteme.delegate.AutoCompleteEmailTaskDelegate;
import br.com.android.invviteme.httpServices.HttpServiceLogin;
import br.com.android.invviteme.model.LoginModel;
import br.com.android.invviteme.model.User;
import br.com.android.invviteme.tasks.AutoCompleteEmailTask;
import br.com.android.invviteme.utils.FacebookUtils;
import br.com.android.invviteme.utils.ValidFields;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements AutoCompleteEmailTaskDelegate {

    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginButton facebookLoginButton;
    private Button mEmailSignInButton;
    private TextView linkSingUp;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        referencesUI();
        populateAutoComplete();

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        linkSingUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
                finish();
            }
        });

        facebookLoginButton.setReadPermissions(FacebookUtils.getPermissionsReadFacebook());
        facebookLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.GONE);
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        mLoginFormView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(FacebookException e) {
                        mLoginFormView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void referencesUI(){
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        linkSingUp = (TextView)findViewById(R.id.linkSingUp);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        new AutoCompleteEmailTask(this,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        HideShowProgressBar.showProgress(true,mProgressView,mLoginFormView, LoginActivity.this);

        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;

        boolean cancel = false;

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!ValidFields.isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
            focusView.requestFocus();
        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiCall.BASE_URL)
                    .build();

            HttpServiceLogin serviceLogin = retrofit.create(HttpServiceLogin.class);

            Call<User> call = serviceLogin.invokeLogin(new LoginModel(email,password));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    //// TODO: //TODO: converter o json e setar o user no SharedPreference e redirecionar para a main
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
                    Log.d("invviteMe", t.getMessage());
                    Snackbar.make(mEmailView, R.string.error_process, Snackbar.LENGTH_LONG)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                @TargetApi(Build.VERSION_CODES.M)
                                public void onClick(View v) {
                                }
                            });
                }
            });

        }
    }

    @Override
    public void setAdapterEmail(List<String> listEmails) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, listEmails);
        mEmailView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

