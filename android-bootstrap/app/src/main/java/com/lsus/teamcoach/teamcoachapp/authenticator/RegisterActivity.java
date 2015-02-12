package com.lsus.teamcoach.teamcoachapp.authenticator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R.layout;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.R.id;
import android.support.v7.app.ActionBarActivity;

import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

public class  RegisterActivity extends ActionBarAccountAuthenticatorActivity  {

    @Inject BootstrapService bootstrapService;
    @Inject Bus bus;

    @InjectView(id.btnRegister) protected Button confirmRegisterButton;
    @InjectView(id.tvRegisterCancel) protected TextView cancelRegister;
    @InjectView(id.etEmail) protected EditText email;
    @InjectView(id.etPassword) protected EditText password;
    @InjectView(id.etFirstName) protected EditText firstName;
    @InjectView(id.etLastName) protected EditText lastName;
    @InjectView(id.registerRadioGroup) protected RadioGroup radioButtons;
    @InjectView(id.coachRB) protected RadioButton coachRadioButton;
    @InjectView(id.playerRB) protected RadioButton playerRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        setContentView(layout.activity_register);
        Views.inject(this);

        confirmRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRegister(confirmRegisterButton);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onRegister(final View view)
    {
        int selectedId = radioButtons.getCheckedRadioButtonId();

        ParseUser user = new ParseUser();
        //ParseObject user = new ParseObject("User");


        user.put("firstName", firstName.getText().toString());
        user.put("lastName", lastName.getText().toString());
        //user.put("email", email.getText().toString());
        //user.put("password", password.getText().toString());
        //user.put("username", email.getText().toString());
        user.put("alias", firstName.getText().toString());


        //ADD CHECKS LATER!!!!!!!!
        user.setUsername(email.toString());
        user.setPassword(password.toString());
        user.setEmail(email.toString());

        boolean givenRole = false;

        if(selectedId == coachRadioButton.getId()) {
            user.put("role", "Coach");
            givenRole = true;
        }else if(selectedId == playerRadioButton.getId()){
            user.put("role", "Player");
            givenRole = true;
        }else{
            //handle no selection
            Context context = getApplicationContext();
            CharSequence text = "No Role Selected";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        }

        if (givenRole = true){

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        //Continue to app!
                        Context context = getApplicationContext();
                        CharSequence text = "User Created!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                    }
                    else
                    {
                        //Failed. Find error :(
                        Context context = getApplicationContext();
                        CharSequence text = "Error!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                    }
                }
            });}

            return true;
    }
}