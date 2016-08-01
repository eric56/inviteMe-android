package br.com.android.invviteme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkmmte.view.CircularImageView;

import java.util.List;

import br.com.android.invviteme.R;
import br.com.android.invviteme.activities.MainActivity;
import br.com.android.invviteme.animations.HideShowProgressBar;
import br.com.android.invviteme.model.Users;
import br.com.android.invviteme.utils.ConsoleTags;
import br.com.android.invviteme.utils.FacebookUtils;

public class FragmentProfile extends Fragment {

    private TextView birthday, nameHeaderProfile, emailHeaderProfile;
    private EditText firstName, lastName, email, phone, password, confirmPassword;
    private RadioGroup radioGroupGenderProfile;
    private RadioButton radioMaleProfile, radioFemaleProfile;
    private CircularImageView photoUser;
    private Switch isEdit;
    private AppCompatCheckBox checkAlterPassword;
    private View progressBar, formProfile, inputsPasswords;

    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2,container,false);

        mainActivity = (MainActivity) getActivity();

        referenceUI(view);
        requestDataCurrentUser();
        photoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(),photoUser);
                popup.getMenuInflater().inflate(R.menu.menu_option_photo_user_profile, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.view_photo:
                                break;
                            case R.id.import_photo_facebook:
                                break;
                            case R.id.import_photo_album:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        isEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if(isOn){
                    enableFields();
                }else{
                    disableFields();
                }
            }
        });

        checkAlterPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    inputsPasswords.setVisibility(View.VISIBLE);
                }else{
                    inputsPasswords.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void disableFields() {
        firstName.setFocusable(false);
        lastName.setFocusable(false);
        email.setFocusable(false);
        phone.setFocusable(false);
        birthday.setFocusable(false);
        password.setFocusable(false);
        confirmPassword.setFocusable(false);
        checkAlterPassword.setChecked(false);
        checkAlterPassword.setVisibility(View.GONE);
        inputsPasswords.setVisibility(View.GONE);
    }

    private void enableFields() {
        firstName.setFocusable(true);
        lastName.setFocusable(true);
        email.setFocusable(true);
        phone.setFocusable(true);
        birthday.setFocusable(true);
        password.setFocusable(true);
        confirmPassword.setFocusable(true);
        checkAlterPassword.setChecked(false);
        checkAlterPassword.setVisibility(View.VISIBLE);
        inputsPasswords.setVisibility(View.GONE);
    }

    private void requestDataCurrentUser() {
        final FirebaseUser currentUser = mainActivity.getmAuth().getCurrentUser();
        if(currentUser != null){
            HideShowProgressBar.showProgress(true,progressBar,formProfile,getActivity());
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users/"+currentUser.getUid());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    setFields(user,currentUser.getProviders());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    HideShowProgressBar.showProgress(false,progressBar,formProfile,getActivity());
                    Log.w(ConsoleTags.ERROR_INVVITE,databaseError.toException());
                    Toast.makeText(getActivity(),getString(R.string.error_process),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            mainActivity.getmAuth().signOut();
        }
    }

    private void setFields(Users user, List<String> providers) {
        String name = user.getName()+" "+user.getLastName();
//        nameHeaderProfile.setText(name);
  //      emailHeaderProfile.setText(user.getEmail());
        firstName.setText(user.getName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        if(FacebookUtils.isLoggedFacebook(providers)){
            birthday.setText("-");
        }else{
            birthday.setText(user.getBirthday());
        }
        if(user.getGender().equals("M")){
            radioGroupGenderProfile.check(R.id.maleProfile);
        }else{
            radioGroupGenderProfile.check(R.id.femaleProfile);
        }
        HideShowProgressBar.showProgress(false,progressBar,formProfile,getActivity());
    }

    private void referenceUI(View view) {
        nameHeaderProfile = (TextView) view.findViewById(R.id.nameHeaderProfile);;
        emailHeaderProfile = (TextView) view.findViewById(R.id.emailHeaderProfile);
        firstName = (EditText) view.findViewById(R.id.firstNameProfile);
        lastName = (EditText) view.findViewById(R.id.lastNameProfile);
        email = (EditText) view.findViewById(R.id.emailProfile);
        phone = (EditText) view.findViewById(R.id.phoneProfile);
        password = (EditText) view.findViewById(R.id.passwordProfile);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPasswordProfile);
        birthday = (TextView) view.findViewById(R.id.birthdayProfile);
        radioGroupGenderProfile = (RadioGroup) view.findViewById(R.id.radioGroupGenderProfile);
        radioMaleProfile = (RadioButton) view.findViewById(R.id.maleProfile);
        radioFemaleProfile = (RadioButton) view.findViewById(R.id.femaleProfile);
        photoUser = (CircularImageView) view.findViewById(R.id.photoUser);
        isEdit = (Switch) view.findViewById(R.id.isEdit);
        checkAlterPassword = (AppCompatCheckBox) view.findViewById(R.id.checkAlterPassword);
        progressBar = view.findViewById(R.id.progressBarProfile);
        formProfile = view.findViewById(R.id.scrollFormProfile);
        inputsPasswords = view.findViewById(R.id.inputsPasswords);
    }

}
