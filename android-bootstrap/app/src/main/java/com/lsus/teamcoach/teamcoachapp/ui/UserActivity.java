package com.lsus.teamcoach.teamcoachapp.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

public class UserActivity extends BootstrapActivity implements View.OnClickListener {

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;
    @InjectView(R.id.tv_userEmail) protected TextView email;
    @InjectView(R.id.tv_username) protected TextView username;
    @InjectView(R.id.tv_role) protected TextView role;
    @InjectView(R.id.button_Edit) protected Button button_Edit;
    @InjectView(R.id.button_Submit) protected Button button_Submit;
    @InjectView(R.id.et_name) protected EditText et_name;
    @InjectView(R.id.et_userEmail) protected  EditText et_email;
    @InjectView(R.id.et_username) protected EditText et_username;

    private User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_view);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(USER);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(user.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(avatar);

        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        role.setText(String.format("%s", user.getRole()));

        email.setText(String.format("%s", user.getEmail()));

        username.setText(String.format("%s", user.getUsername()));

        // Gets the logged in accounts user information
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(Constants.Auth.BOOTSTRAP_ACCOUNT_TYPE);
        Account myAccount =  accounts[0];

        if(myAccount.name.equalsIgnoreCase(user.getUsername())){
            button_Edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == button_Edit.getId())
        {
            //The register button has been clicked
            //onRegister(confirmRegisterButton);
            onEdit();
        }else if(view.getId() == button_Submit.getId()){
            //The cancel text has been clicked
            //bootstrapAuthenticatorActivity.getSupportFragmentManager().beginTransaction().remove(RegisterFragment.this).commit();
            onSubmit();
        };
    }

    public void onEdit(){
        button_Edit.setVisibility(View.GONE);
        button_Submit.setVisibility(View.VISIBLE);

        //Resets all TextViews to be EditText fields
        name.setVisibility(View.GONE);
        et_name.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        et_email.setVisibility(View.VISIBLE);
        username.setVisibility(View.GONE);
        et_username.setVisibility(View.VISIBLE);
        role.setVisibility(View.GONE);

        //Sets the text in the EditText fields
        et_name.setText(String.format("%s", name.getText().toString()));
        et_email.setText(String.format("%s", email.getText().toString()));
        et_username.setText(String.format("%s", username.getText().toString()));

    }

    public void onSubmit(){
        button_Submit.setVisibility(View.GONE);
        button_Edit.setVisibility(View.VISIBLE);

        //Resets all EditText fields to be TextViews
        name.setVisibility(View.VISIBLE);
        et_name.setVisibility(View.GONE);
        email.setVisibility(View.VISIBLE);
        et_email.setVisibility(View.GONE);
        username.setVisibility(View.VISIBLE);
        et_username.setVisibility(View.GONE);
        role.setVisibility(View.VISIBLE);


        //Sets the text in the TextViews.
        //Needs to be changed to update Parse using Rest API!  ------------------------------
        //Need to introduce checks here!!!! -------------------------------------------------
        name.setText(String.format("%s", et_name.getText().toString()));
        email.setText(String.format("%s", et_email.getText().toString()));
        username.setText(String.format("%s", et_username.getText().toString()));
    }
}

