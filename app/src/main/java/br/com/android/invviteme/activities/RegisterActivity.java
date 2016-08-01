package br.com.android.invviteme.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.android.invviteme.R;
import br.com.android.invviteme.animations.HideShowProgressBar;
import br.com.android.invviteme.constants.DateConstant;
import br.com.android.invviteme.enums.StatusData;
import br.com.android.invviteme.delegate.AutoCompleteEmailTaskDelegate;
import br.com.android.invviteme.model.StatusType;
import br.com.android.invviteme.model.Users;
import br.com.android.invviteme.tasks.AutoCompleteEmailTask;
import br.com.android.invviteme.utils.ConsoleTags;
import br.com.android.invviteme.utils.PasswordManager;
import br.com.android.invviteme.utils.ValidFields;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements AutoCompleteEmailTaskDelegate {

    private static final int REQUEST_READ_CONTACTS = 0;

    private DatePickerDialog dateBirthDayPickerDialog;
    private SimpleDateFormat dateFormatter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    AutoCompleteTextView email;
    EditText firstName, lastName, password, confirmPassword, phoneUser;
    RadioGroup gender;
    RadioButton maleGander, femaleGender;
    TextView birthDay;
    Button buttonSingUp;
    View form, progressBar;

    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        dateFormatter = new SimpleDateFormat(DateConstant.DATE_FORMAT_BR, Locale.US);

        referenceUIs();
        populateAutoComplete();
        configureDatePickerDialog();

        user = null;

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFB = firebaseAuth.getCurrentUser();
                if (userFB != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference refUsers = database.getReference("users");
                    refUsers.child(userFB.getUid()).setValue(user).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.signOut();
                            if(task.isSuccessful()){
                                //TODO: Chamar a api de enviar email passando o email
                                Toast.makeText(RegisterActivity.this,R.string.send_email_confirmation,Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }else{
                                Log.w(ConsoleTags.ERROR_INVVITE,task.getException());
                                HideShowProgressBar.showProgress(false,progressBar,form,RegisterActivity.this);
                                Toast.makeText(RegisterActivity.this,R.string.error_process,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        };

        birthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateBirthDayPickerDialog.show();
            }
        });

        birthDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dateBirthDayPickerDialog.show();
                }
            }
        });

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

    }

    private void referenceUIs() {
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        email = (AutoCompleteTextView)findViewById(R.id.emailUser);
        password = (EditText)findViewById(R.id.passwordUser);
        confirmPassword = (EditText)findViewById(R.id.confirmPasswordUser);
        gender = (RadioGroup)findViewById(R.id.radioGroupGender);
        maleGander = (RadioButton)findViewById(R.id.male);
        femaleGender = (RadioButton)findViewById(R.id.female);
        birthDay = (TextView)findViewById(R.id.birthday);
        buttonSingUp = (Button)findViewById(R.id.register_button);
        form = findViewById(R.id.scrollRegister);
        progressBar = findViewById(R.id.progressBarRegister);
        phoneUser = (EditText) findViewById(R.id.phoneUser);
    }

    private void configureDatePickerDialog(){
        Calendar newCalendar = Calendar.getInstance();
        dateBirthDayPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDay.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void attemptRegister(){
        HideShowProgressBar.showProgress(true,progressBar,form,RegisterActivity.this);

        View focusView = null;
        firstName.setError(null);
        lastName.setError(null);
        email.setError(null);
        password.setError(null);
        confirmPassword.setError(null);
        birthDay.setError(null);
        phoneUser.setError(null);

        boolean cancel = false;

        if(TextUtils.isEmpty(firstName.getText().toString())){
            firstName.setError(getString(R.string.error_field_required));
            focusView = firstName;
            cancel = true;
        }
        if(TextUtils.isEmpty(lastName.getText().toString())){
            lastName.setError(getString(R.string.error_field_required));
            focusView = lastName;
            cancel = true;
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if(!ValidFields.isValidEmail(email.getText().toString())){
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }
        if(TextUtils.isEmpty(phoneUser.getText().toString())){
            phoneUser.setError(getString(R.string.error_field_required));
            focusView = phoneUser;
            cancel = true;
        }
        if(TextUtils.isEmpty(birthDay.getText().toString())){
            birthDay.setError(getString(R.string.error_field_required));
            focusView = birthDay;
            cancel = true;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        }
        if(TextUtils.isEmpty(confirmPassword.getText().toString())){
            confirmPassword.setError(getString(R.string.error_field_required));
            focusView = confirmPassword;
            cancel = true;
        }
        if(!Objects.equals(password.getText().toString(), confirmPassword.getText().toString())){
            confirmPassword.setError(getString(R.string.error_password_different));
            focusView = confirmPassword;
            cancel = true;
        }

        if (cancel) {
            HideShowProgressBar.showProgress(false,progressBar,form,RegisterActivity.this);
            focusView.requestFocus();
        } else {
            user = getUser();
            if(user != null){
                mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.w(ConsoleTags.ERROR_INVVITE,task.getException());
                            if(task.getException() != null && task.getException().getMessage().contains("email address is already")){
                                HideShowProgressBar.showProgress(false,progressBar,form,RegisterActivity.this);
                                Toast.makeText(RegisterActivity.this, R.string.email_is_already, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }else{
                HideShowProgressBar.showProgress(false,progressBar,form,RegisterActivity.this);
                Toast.makeText(RegisterActivity.this, R.string.error_process, Toast.LENGTH_LONG).show();
            }

        }
    }

    private Users getUser() {
        String genderType;
        if(gender.getCheckedRadioButtonId() == maleGander.getId()){
            genderType = "M";
        }else{
            genderType = "F";
        }
        try {
            StatusType statusType = new StatusType(StatusData.PENDENTE);
            return new Users(firstName.getText().toString(),lastName.getText().toString(),birthDay.getText().toString(),
                    phoneUser.getText().toString(),email.getText().toString(),PasswordManager.encrypt(password.getText().toString()),
                    genderType,statusType);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Log.w(ConsoleTags.ERROR_INVVITE,e);
        }
        return  null;
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
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public void setAdapterEmail(List<String> listEmails) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, listEmails);
        email.setAdapter(adapter);
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

}
