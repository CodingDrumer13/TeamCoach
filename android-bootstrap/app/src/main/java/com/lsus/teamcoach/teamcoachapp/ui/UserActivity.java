package com.lsus.teamcoach.teamcoachapp.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

public class UserActivity extends BootstrapActivity {

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;
    @InjectView(R.id.tv_role) protected TextView role;
    @InjectView(R.id.tv_userEmail) protected TextView email;
    @InjectView(R.id.tv_username) protected TextView username;

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

    }


}

