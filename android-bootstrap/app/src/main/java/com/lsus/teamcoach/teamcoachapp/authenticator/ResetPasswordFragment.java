package com.lsus.teamcoach.teamcoachapp.authenticator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Email;
import com.lsus.teamcoach.teamcoachapp.ui.TextWatcherAdapter;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

/**
 * Created by Don on 5/1/2015.
 */
public class ResetPasswordFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.btn_password_reset)
    Button btn_password_reset;

    @InjectView(R.id.et_forgot_password)
    EditText et_forgot_password;

    @InjectView(R.id.btn_password_rest_back)
    Button btn_password_rest_back;

    protected SafeAsyncTask<Boolean> asyncTask;

    @Inject
    protected BootstrapService bootstrapService;

    private final TextWatcher watcher = validationTextWatcher();
    protected BootstrapAuthenticatorActivity bootstrapAuthenticatorActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_fragment, container, false);
        Injector.inject(this);
        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btn_password_reset.setOnClickListener(this);
        btn_password_rest_back.setOnClickListener(this);
        et_forgot_password.addTextChangedListener(watcher);
        btn_password_reset.setEnabled(false);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == btn_password_reset.getId()){
            showProgress();

            asyncTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {
                    bootstrapService.passwordReset(new Email(et_forgot_password.getText().toString()));
                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if(!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if(cause != null) {
                            Log.d("Error", cause.getMessage());
                        }
                    }
                }

                @Override
                protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Password Reset")
                            .setMessage("Your password has just been reset")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                @Override
                protected void onFinally(){
                    hideProgress();
                    bootstrapAuthenticatorActivity.loginContainer.setVisibility(View.VISIBLE);
                    back();

                }
            };
            asyncTask.execute();

        }

        if(v.getId() == btn_password_rest_back.getId()){
            bootstrapAuthenticatorActivity.loginContainer.setVisibility(View.VISIBLE);
            back();
        }
    }

    public void back(){
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
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
        final boolean populated = populated(et_forgot_password);
        btn_password_reset.setEnabled(populated);
    }

    private boolean populated(final EditText editText) {
        return editText.length() > 0;
    }

    public void setBootstrapAuthenticatorActivity(BootstrapAuthenticatorActivity bootstrapAuthenticatorActivity) {
        this.bootstrapAuthenticatorActivity = bootstrapAuthenticatorActivity;
    }

}
