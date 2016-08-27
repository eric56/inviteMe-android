package br.com.android.invviteme.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

    private TextView nameHeaderProfile, emailHeaderProfile;
    private EditText phoneProfile, statusUserProfile, birthdayProfile, passwordProfile, confirmPasswordProfile;
    private RadioGroup radioGroupGenderProfile;
    private RadioButton femaleProfile, maleProfile;
    private View contentChangePassword, contentPasswords, formProfile;
    private CheckBox checkAlterPassword;
    private Switch isEdit;
    private CircularImageView photoUser;
    private View progressBar;
    private Button saveButton;

    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

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
                    contentPasswords.setVisibility(View.VISIBLE);
                }else{
                    contentPasswords.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void disableFields() {
        phoneProfile.setFocusable(false);
        birthdayProfile.setFocusable(false);
        passwordProfile.setFocusable(false);
        statusUserProfile.setFocusable(false);
        confirmPasswordProfile.setFocusable(false);
        checkAlterPassword.setChecked(false);
        contentChangePassword.setVisibility(View.GONE);
        contentPasswords.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    private void enableFields() {
        phoneProfile.setFocusable(true);
        birthdayProfile.setFocusable(true);
        passwordProfile.setFocusable(true);
        statusUserProfile.setFocusable(true);
        confirmPasswordProfile.setFocusable(true);
        checkAlterPassword.setChecked(false);
        contentChangePassword.setVisibility(View.VISIBLE);
        contentPasswords.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
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
        nameHeaderProfile.setText(name);
        emailHeaderProfile.setText(user.getLastName());
        if(FacebookUtils.isLoggedFacebook(providers)){
            birthdayProfile.setText("-");
        }else{
            birthdayProfile.setText(user.getBirthday());
        }
        if(user.getGender().equals("M")){
            radioGroupGenderProfile.check(R.id.maleProfile);
        }else{
            radioGroupGenderProfile.check(R.id.femaleProfile);
        }
        HideShowProgressBar.showProgress(false,progressBar,formProfile,getActivity());
    }

    private void referenceUI(View view) {
        nameHeaderProfile = (TextView) view.findViewById(R.id.nameHeaderProfile);
        emailHeaderProfile = (TextView) view.findViewById(R.id.emailHeaderProfile);
        phoneProfile = (EditText) view.findViewById(R.id.phoneProfile);
        statusUserProfile = (EditText) view.findViewById(R.id.statusUserProfile);
        birthdayProfile = (EditText) view.findViewById(R.id.birthdayProfile);
        passwordProfile = (EditText) view.findViewById(R.id.passwordProfile);
        confirmPasswordProfile = (EditText) view.findViewById(R.id.confirmPasswordProfile);
        radioGroupGenderProfile = (RadioGroup) view.findViewById(R.id.radioGroupGenderProfile);
        femaleProfile = (RadioButton) view.findViewById(R.id.femaleProfile);
        maleProfile = (RadioButton) view.findViewById(R.id.maleProfile);
        contentChangePassword = view.findViewById(R.id.contentChangePassword);
        contentPasswords =  view.findViewById(R.id.contentPasswords);
        formProfile = view.findViewById(R.id.scrollFormProfile);
        checkAlterPassword = (CheckBox) view.findViewById(R.id.checkAlterPassword);
        isEdit = (Switch) view.findViewById(R.id.isEdit);
        photoUser = (CircularImageView) view.findViewById(R.id.photoUser);
        progressBar = view.findViewById(R.id.progressBarProfile);
        saveButton = (Button) view.findViewById(R.id.save_profile_button);
    }

}
