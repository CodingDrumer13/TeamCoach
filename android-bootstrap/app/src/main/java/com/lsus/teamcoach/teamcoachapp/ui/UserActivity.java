package com.lsus.teamcoach.teamcoachapp.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

public class UserActivity extends BootstrapActivity implements View.OnClickListener {

    @Inject
    BootstrapService bootstrapService;

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
    @InjectView(R.id.tv_roleTag) protected TextView tv_roleTag;

    private User user;
    private SafeAsyncTask<Boolean> authenticationTask;
    private String authToken;

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

        //User Singlton to get user or session
        authToken = accountManager.peekAuthToken(myAccount,Constants.Auth.AUTHTOKEN_TYPE );
        user.setSessionToken(authToken);

        if(myAccount.name.equalsIgnoreCase(user.getUsername())){
            button_Edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == button_Edit.getId())
        {
            //The Edit button has been clicked
            onEdit();
        }else if(view.getId() == button_Submit.getId()){
            //The Submit button has been clicked
            onSubmit();
        }
    }

    private void onEdit(){
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
        tv_roleTag.setVisibility(View.GONE);

        //Sets the text in the EditText fields
        et_name.setText(String.format("%s", name.getText().toString()));
        et_email.setText(String.format("%s", email.getText().toString()));
        et_username.setText(String.format("%s", username.getText().toString()));

    }

    private void onSubmit(){
        if(validateFields()){
            button_Submit.setVisibility(View.GONE);
            button_Edit.setVisibility(View.VISIBLE);

            String[] names = et_name.getText().toString().split(" ");
            String firstName = getFirstName(names);
            String lastName = getLastName(names);

            //Resets all EditText fields to be TextViews
            name.setVisibility(View.VISIBLE);
            et_name.setVisibility(View.GONE);
            email.setVisibility(View.VISIBLE);
            et_email.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.GONE);
            role.setVisibility(View.VISIBLE);
            tv_roleTag.setVisibility(View.VISIBLE);

            //Sets the text in the TextViews.
            // Needs to be changed to update Parse using Rest API!  ------------------------------
            user.setUsername(et_email.getText().toString());
            user.setEmail(et_email.getText().toString());
            user.setFirstName(firstName);
            user.setLastName(lastName);

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    //Implement try/catch for update error
                    bootstrapService.update(user);

                    return true;
                }
            };
            authenticationTask.execute();

            //Update user in singlton

            name.setText(String.format("%s", et_name.getText().toString()));
            email.setText(String.format("%s", et_email.getText().toString()));
            username.setText(String.format("%s", et_username.getText().toString()));
        }
    }

    private boolean validateFields(){
        //TODO Need to introduce checks here!!!!
        //Handles the setting of the first and last name.
        String[] names = et_name.getText().toString().split(" ");
        if (names.length == 1){
            Toaster.showLong(this, "Please enter full name.");
            return false;
        }

        String userEmail = et_email.getText().toString();
        if(!userEmail.contains("@")){
            Toaster.showLong(this, "Invalid Email");
            return false;
        }else{
            if(!userEmail.contains(".com") && !userEmail.contains(".org")){
                Toaster.showLong(this, "Invalid Email");
                return false;
            }
        }

        return true;
    }

    private String getFirstName(String[] names){
        return names[0];
    }

    private String getLastName(String[] names){
        String lastName = "";
        if(names.length < 3){
            lastName = names[1];
        }else{
            for(int i = 1; i < names.length - 1; i++){
                if(i == names.length){
                    lastName = names[i];
                }else{
                    lastName += names[i] + " ";
                }
            }
        }

        return lastName;
    }
}

