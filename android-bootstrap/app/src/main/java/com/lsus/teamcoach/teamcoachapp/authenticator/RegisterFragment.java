package com.lsus.teamcoach.teamcoachapp.authenticator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.R.id;
import com.lsus.teamcoach.teamcoachapp.R.layout;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.TextWatcherAdapter;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.Parse;
import com.parse.ParseException;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

public class RegisterFragment extends Fragment implements View.OnClickListener  {

    private BootstrapAuthenticatorActivity bootstrapAuthenticatorActivity;
    private SafeAsyncTask<Boolean> authenticationTask;

    private String token;

    private String userRole;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPassword;
    private String userAlias;
    private String userUsername;
    private String userPhoneNumber;

    private final TextWatcher watcher = validationTextWatcher();

    @Inject BootstrapService bootstrapService;

    @InjectView(id.btnRegister) protected Button confirmRegisterButton;
    @InjectView(id.tvRegisterCancel) protected TextView cancelRegister;
    @InjectView(id.etEmail) protected EditText email;
    @InjectView(id.etPassword) protected EditText password;
    @InjectView(id.etFirstName) protected EditText firstName;
    @InjectView(id.etLastName) protected EditText lastName;
    @InjectView(id.registerRadioGroup) protected RadioGroup radioButtons;
    @InjectView(id.coachRB) protected RadioButton coachRadioButton;
    @InjectView(id.playerRB) protected RadioButton playerRadioButton;
    @InjectView(id.etRegisterPhoneNumber) protected EditText et_phonenumber;

    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout.activity_register, container, false);
        Injector.inject(this);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);
        confirmRegisterButton.setOnClickListener(this);
        cancelRegister.setOnClickListener(this);
        confirmRegisterButton.setEnabled(false);

        firstName.addTextChangedListener(watcher);
        lastName.addTextChangedListener(watcher);
        email.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);
        et_phonenumber.addTextChangedListener(watcher);

        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateUIWithValidation();
            }
        });
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
       getActivity().dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        getActivity().showDialog(0);
    }

    public boolean onRegister(final View view)
    {
        int selectedId = radioButtons.getCheckedRadioButtonId();

        showProgress();

        //ADD CHECKS LATER!!!!!!!!
        userFirstName = firstName.getText().toString();
        userLastName = lastName.getText().toString();
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userAlias = firstName.getText().toString();
        userUsername = email.getText().toString();
        userPhoneNumber = et_phonenumber.getText().toString();


        boolean givenRole = false;

        if(selectedId == coachRadioButton.getId()) {
            userRole = "Coach";
            givenRole = true;
        }else if(selectedId == playerRadioButton.getId()){
            userRole = "Player";
            givenRole = true;
        }else{
            //handle no selection
            Context context = getActivity().getApplicationContext();
            CharSequence text = "No Role Selected";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        }

        if (givenRole) {
            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    User user = new User(userUsername, userPassword, userAlias, userRole, userEmail, userFirstName, userLastName, userPhoneNumber);
                    User loginResponse = bootstrapService.register(user);
                    token = loginResponse.getSessionToken();


                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if (!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if (cause != null) {
                            Toaster.showLong(getActivity(), cause.getMessage());
                        }
                    }else{
                        //Warning! Maybe an error besides a taken parse username
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Attention")
                                .setMessage("There is already an account associated with this email address")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        email.requestFocus();
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onSuccess(final Boolean authSuccess) {
                    bootstrapAuthenticatorActivity.finishLogin(token, userEmail, userPassword);
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    hideProgress();
                    authenticationTask = null;
                    }
            };
            authenticationTask.execute();

        }
        return true;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == confirmRegisterButton.getId())
        {
            //The register button has been clicked
            onRegister(confirmRegisterButton);
        }else if(view.getId() == cancelRegister.getId()){
            //The cancel text has been clicked
            bootstrapAuthenticatorActivity.iv_TeamCoachImage.setVisibility(View.VISIBLE);
            bootstrapAuthenticatorActivity.getSupportFragmentManager().beginTransaction().remove(RegisterFragment.this).commit();
        }
    }

    public void setBootstrapAuthenticatorActivity(BootstrapAuthenticatorActivity bootstrapAuthenticatorActivity){
        this.bootstrapAuthenticatorActivity = bootstrapAuthenticatorActivity;
    }

    private TextWatcher validationTextWatcher() {
        return new TextWatcherAdapter() {
            public void afterTextChanged(final Editable gitDirEditText) {
                updateUIWithValidation();
            }

        };
    }

    //    Input Check for edittext
    private void updateUIWithValidation() {
        final boolean populated = populated(email) && populated(password) && populated(firstName) && populated(lastName) && radioGroupPopulated(radioButtons);
        confirmRegisterButton.setEnabled(populated);
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    private boolean radioGroupPopulated(final RadioGroup radioButtons) {
        if (radioButtons.getCheckedRadioButtonId() == -1) {
//            no radio buttons are checked
              return false;
        } else {
//            one of the radio buttons is checked
            return true;
        }
    }
}