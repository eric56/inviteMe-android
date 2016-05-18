package br.com.android.invviteme.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.android.invviteme.R;
import br.com.android.invviteme.animations.HideShowProgressBar;
import br.com.android.invviteme.constants.ApiCall;
import br.com.android.invviteme.constants.DateConstant;
import br.com.android.invviteme.delegate.AutoCompleteEmailTaskDelegate;
import br.com.android.invviteme.httpServices.HttpServiceRegister;
import br.com.android.invviteme.model.User;
import br.com.android.invviteme.tasks.AutoCompleteEmailTask;
import br.com.android.invviteme.utils.PasswordManager;
import br.com.android.invviteme.utils.ValidFields;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

public class Register extends AppCompatActivity implements AutoCompleteEmailTaskDelegate {

    private static final int REQUEST_READ_CONTACTS = 0;

    private DatePickerDialog dateBirthDayPickerDialog;
    private SimpleDateFormat dateFormatter;

    AutoCompleteTextView email;
    EditText firstName, lastName, password, confirmPassword;
    RadioGroup gender;
    RadioButton maleGander, femaleGender;
    TextView birthDay;
    Button buttonSingUp;
    View form, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dateFormatter = new SimpleDateFormat(DateConstant.DATE_FORMAT_BR, Locale.US);

        referenceUIs();
        populateAutoComplete();
        configureDatePickerDialog();

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
        HideShowProgressBar.showProgress(true,progressBar,form,Register.this);

        View focusView = null;
        firstName.setError(null);
        lastName.setError(null);
        email.setError(null);
        password.setError(null);
        confirmPassword.setError(null);
        birthDay.setError(null);

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
            HideShowProgressBar.showProgress(false,progressBar,form,Register.this);
            focusView.requestFocus();
        } else {
            User user = getUser();
            if(user != null){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiCall.BASE_URL)
                        .build();

                HttpServiceRegister serviceRegister = retrofit.create(HttpServiceRegister.class);

                Call<User> call = serviceRegister.invokeRegister(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        //TODO: converter o json e setar o user no SharedPreference e redirecionar para a main
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        HideShowProgressBar.showProgress(false,progressBar,form,Register.this);
                        Log.d("invviteMe", t.getMessage());
                        Snackbar.make(email, R.string.error_process, Snackbar.LENGTH_LONG)
                                .setAction(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    @TargetApi(Build.VERSION_CODES.M)
                                    public void onClick(View v) {
                                    }
                                });
                    }
                });

            }else{
                HideShowProgressBar.showProgress(false,progressBar,form,Register.this);
                Snackbar.make(email, R.string.error_process, Snackbar.LENGTH_LONG)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                            }
                        });
            }

        }
    }

    private User getUser() {
        String genderType;
        if(gender.getCheckedRadioButtonId() == maleGander.getId()){
            genderType = maleGander.getText().toString();
        }else{
            genderType = femaleGender.getText().toString();
        }
        try {
            return new User(firstName.getText().toString(),
                    lastName.getText().toString(),
                    stringToDate(birthDay.getText().toString()),
                    genderType,
                    email.getText().toString(),
                    PasswordManager.encrypt(password.getText().toString())
            );
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | ParseException e) {
            e.printStackTrace();
            Log.d("invviteMe",e.getMessage());
        }
        return  null;
    }

    private Date stringToDate(String s) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateConstant.DATE_FORMAT_BR, Locale.US);
        return dateFormat.parse(s);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
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

}
