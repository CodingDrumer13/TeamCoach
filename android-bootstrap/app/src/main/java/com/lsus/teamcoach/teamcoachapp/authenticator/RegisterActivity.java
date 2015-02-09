package com.lsus.teamcoach.teamcoachapp.authenticator;

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

import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.R.id;
import com.parse.ParseObject;
import com.parse.ParseUser;

import butterknife.InjectView;

public class  RegisterActivity extends ActionBarActivity {



    @InjectView(id.btnRegister) protected Button confirmRegisterButton;
    @InjectView(id.tvRegisterCancel) protected TextView cancelRegister;
    @InjectView(id.et_email) protected EditText email;
    @InjectView(id.et_password) protected EditText password;
    @InjectView(id.etFirstName) protected EditText firstName;
    @InjectView(id.etLastName) protected EditText lastName;
    @InjectView(id.registerRadioGroup) protected RadioGroup radioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        confirmRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lastName.getText();
                email.getText();
                password.getText();
                int selectedId = radioButtons.getCheckedRadioButtonId();

                ParseObject parseObject = new ParseObject("User");

                ParseUser user = new ParseUser();
                user.put("fisrtName", firstName.getText());
                user.put("lastName", lastName.getText());
                user.put("username", email.getText());
                user.put("email",email.getText());

                switch(selectedId){
                    case 0:
                        user.put("role", "Coach");
                        break;
                    case 1:
                        user.put("role", "Player");
                        break;
                    default:
                        // handle no selection
                        break;
                }

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

    public boolean onRegister()
    {
        confirmRegisterButton.setOnClickListener(new View.OnClickListener()
        {
                public void onClick(View v)
                {

                }

        });
        return true;
    }
}