package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.DrillObject;
import com.lsus.teamcoach.teamcoachapp.core.DrillPictureObject;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.Team;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.ui.Library.AgeFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.TypeFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.InjectView;
import retrofit.RetrofitError;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;

/**
 * Created by TeamCoach on 4/20/2015.
 */
public class AddDrillActivity extends BootstrapActivity implements View.OnClickListener {

    @InjectView(R.id.btnCancelAddDrill) protected Button btnCancelAddDrill;
    @InjectView(R.id.btnAddDrill) protected Button btnAddDrill;

    private String age;
    private String type;

    private AddDrillFragment addDrillFragment;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_drill_activity);

        setTitle("Add Drill");

        if (getIntent() != null && getIntent().getExtras() != null) {
            age = getIntent().getExtras().getSerializable(DRILL_AGE).toString();
            type = getIntent().getExtras().getSerializable(DRILL_TYPE).toString();
        }

        btnCancelAddDrill.setOnClickListener(this);
        btnAddDrill.setOnClickListener(this);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        addDrillFragment = new AddDrillFragment();
        addDrillFragment.setRetainInstance(true);
        addDrillFragment.setParentActivity(this);
        addDrillFragment.setAge(age);
        addDrillFragment.setType(type);

        fragmentTransaction.replace(R.id.drill_activity_container, addDrillFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
     public void onClick(View view) {
        if(btnAddDrill.getId() == view.getId()) {
            if(addDrillFragment.validateFields()){
                addDrillFragment.addDrillObject();
            }
        }
        if(btnCancelAddDrill.getId() == view.getId()){
            finish();
        }
    }
}
