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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import br.com.android.invviteme.R;
import br.com.android.invviteme.animations.HideShowProgressBar;
import br.com.android.invviteme.delegate.AutoCompleteEmailTaskDelegate;
import br.com.android.invviteme.enums.KeysSharedPreference;
import br.com.android.invviteme.enums.StatusData;
import br.com.android.invviteme.model.StatusType;
import br.com.android.invviteme.model.Users;
import br.com.android.invviteme.tasks.AutoCompleteEmailTask;
import br.com.android.invviteme.utils.ConsoleTags;
import br.com.android.invviteme.utils.FacebookUtils;
import br.com.android.invviteme.utils.PasswordManager;
import br.com.android.invviteme.utils.SharedPreferencesUtils;
import br.com.android.invviteme.utils.ValidFields;

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

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

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
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        facebookLoginButton.setReadPermissions(FacebookUtils.getPermissionsReadFacebook());
        facebookLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.GONE);
                facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginFacebook(loginResult.getAccessToken());
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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    final List<String> providers = firebaseUser.getProviders();
                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users/"+firebaseUser.getUid());
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Users user = dataSnapshot.getValue(Users.class);
                            if(FacebookUtils.isLoggedFacebook(providers) && user == null){
                                GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object,GraphResponse response) {
                                            try {
                                                Profile profileUser = Profile.getCurrentProfile();
                                                String gender = "F";
                                                if(Objects.equals(object.getString("gender"), "male")){
                                                    gender = "M";
                                                }
                                                String phoneNumber = null;
                                                String birthDay = null;
                                                final Users newUser = new Users(profileUser.getFirstName(), profileUser.getLastName(),birthDay,
                                                        phoneNumber,object.getString("email"),null,gender,new StatusType(StatusData.ATIVO));
                                                DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("users");
                                                refUsers.child(firebaseUser.getUid()).setValue(newUser).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            SharedPreferencesUtils.insertInSPUsers(LoginActivity.this, KeysSharedPreference.NAMEUSER.getKey(), newUser.getCompleteName());
                                                            SharedPreferencesUtils.insertInSPUsers(LoginActivity.this, KeysSharedPreference.EMAILUSER.getKey(), newUser.getCompleteName());
                                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            finish();
                                                        }else{
                                                            mAuth.signOut();
                                                            HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
                                                            Toast.makeText(LoginActivity.this,R.string.error_process,Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                );
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "email,gender,birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }else{
                                if(user.getStatusType().getId() == 1){
                                    SharedPreferencesUtils.insertInSPUsers(LoginActivity.this, KeysSharedPreference.NAMEUSER.getKey(), user.getCompleteName());
                                    SharedPreferencesUtils.insertInSPUsers(LoginActivity.this, KeysSharedPreference.EMAILUSER.getKey(), user.getCompleteName());
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else if(user.getStatusType().getId() == 5){
                                    mAuth.signOut();
                                    HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
                                    Toast.makeText(LoginActivity.this,R.string.check_email_confirmation,Toast.LENGTH_LONG).show();
                                }else{
                                    mAuth.signOut();
                                    HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
                                    Toast.makeText(LoginActivity.this,"qq coisa",Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mAuth.signOut();
                        }
                    });
                }else{
                    HideShowProgressBar.showProgress(false,mProgressView, mLoginFormView, LoginActivity.this);
                }
            }
        };

    }

    private void loginFacebook(AccessToken accessToken) {
        HideShowProgressBar.showProgress(true,mProgressView, mLoginFormView, LoginActivity.this);
        if( accessToken != null ){
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.w(ConsoleTags.ERROR_INVVITE,task.getException());
                        HideShowProgressBar.showProgress(false,mProgressView, mLoginFormView, LoginActivity.this);
                        Toast.makeText(LoginActivity.this, getString(R.string.error_sing_in),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            mAuth.signOut();
        }
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
            try {
                mAuth.signInWithEmailAndPassword(email, PasswordManager.encrypt(password)).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(ConsoleTags.ERROR_INVVITE, task.getException());
                            HideShowProgressBar.showProgress(false,mProgressView,mLoginFormView, LoginActivity.this);
                            Toast.makeText(LoginActivity.this, getString(R.string.error_sing_in),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                Log.w(ConsoleTags.ERROR_INVVITE, e);
            }
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

