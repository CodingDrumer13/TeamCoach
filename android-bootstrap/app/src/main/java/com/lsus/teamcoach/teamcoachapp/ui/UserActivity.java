package com.lsus.teamcoach.teamcoachapp.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    @InjectView(R.id.tv_role) protected TextView role;
    @InjectView(R.id.tv_userEmail) protected TextView email;
    @InjectView(R.id.tv_username) protected TextView username;
    @InjectView(R.id.button_Edit) protected Button button_Edit;
    @InjectView(R.id.button_Submit) protected Button button_Submit;

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
        Account myAccount = null;

        myAccount = accounts[0];

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
    }

    public void onSubmit(){
        button_Submit.setVisibility(View.GONE);
        button_Edit.setVisibility(View.VISIBLE);
    }


}

