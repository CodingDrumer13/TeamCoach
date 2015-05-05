package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.BootstrapServiceProvider;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.authenticator.LogoutService;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.DrillObject;
import com.lsus.teamcoach.teamcoachapp.core.DrillPictureObject;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.AgeFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.TypeFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.DRILL_TYPE;

/**
 * Created by TeamCoach on 4/14/2015.
 */
public class AddDrillFragment extends Fragment implements View.OnClickListener,  AdapterView.OnItemSelectedListener{

    @InjectView(R.id.btnAddImage) protected Button btnAddImage;
    @InjectView(R.id.iv_drillImage) protected ImageView drillImage;
    @InjectView(R.id.etAddDrillName) protected EditText etDrillName;
    @InjectView(R.id.sAddDrillAgeGroupBottom) protected Spinner sAgeGroupBottom;
    @InjectView(R.id.sAddDrillAgeGroupTop) protected Spinner sAgeGroupTop;
    @InjectView(R.id.tv_from) protected TextView tvFrom;
    @InjectView(R.id.tv_to) protected TextView tvTo;
    @InjectView(R.id.btnAddAgeGroup) protected Button btnAddAgeGroup;
    @InjectView(R.id.sAddDrillType) protected Spinner sDrillType;
    @InjectView(R.id.etAddDrillDescription) protected EditText etDescription;

    @Inject
    BootstrapService bootstrapService;

    private SafeAsyncTask<Boolean> authenticationTask;

    private boolean ageSelected;
    private boolean typeSelected;
    private boolean useAgeRange = false;
    private String groupId;
    private String drillName;
    private String age;
    private String type;
    private String description;
    private String creator;
    private ParseFile picture;
    private boolean hasPicture = false;

    private static int RESULT_LOAD_IMAGE = 1;


    private AddDrillActivity parentActivity;
    private Fragment parent;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Injector.inject(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_drill_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnAddAgeGroup.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);

        //Sets up the values for the Age Groups
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_group_array, R.layout.teamcoach_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sAgeGroupBottom.setAdapter(ageAdapter);
        sAgeGroupTop.setAdapter(ageAdapter);

        //Sets up the values for the Drill Types
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.drill_type_array, R.layout.teamcoach_spinner_item);
        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sDrillType.setAdapter(typeAdapter);

        sAgeGroupBottom.setOnItemSelectedListener(this);

        sAgeGroupBottom.setSelection(getIndex(sAgeGroupBottom, age));

        sDrillType.setSelection(getIndex(sDrillType, type));

    }


    @Override
     public void onClick(View view) {
        if(btnAddAgeGroup.getId() == view.getId()){
            tvFrom.setVisibility(View.VISIBLE);
            tvTo.setVisibility(View.VISIBLE);
            sAgeGroupTop.setVisibility(View.VISIBLE);
            btnAddAgeGroup.setVisibility(View.GONE);

            useAgeRange = true;
        }

        if(btnAddImage.getId() == view.getId()){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == sAgeGroupBottom.getId()){
            if(position > sAgeGroupTop.getSelectedItemPosition()){
                sAgeGroupTop.setSelection(position);
            }
        }
        if(spinner.getId() == sAgeGroupTop.getId()){
            if(position < sAgeGroupBottom.getSelectedItemPosition()){
                sAgeGroupBottom.setSelection(position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toaster.showLong(this.getActivity(), "Nothing Selected!");
    }

    public void setAgeSelected(Boolean ageSelected){
        this.ageSelected = ageSelected;
    }

    public void setTypeSelected(Boolean typeSelected) { this.typeSelected = typeSelected; }

    public void setAge(String age){
        this.age = age;
    }

    public void setType(String type) { this.type = type; }

    /**
     * Validates all fields to make sure they are filled out correctly.
     * @return
     */
    public boolean validateFields(){
        if (!etDrillName.getText().toString().equalsIgnoreCase("")){
            drillName = etDrillName.getText().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!sDrillType.getSelectedItem().toString().equalsIgnoreCase("")) {
            type = sDrillType.getSelectedItem().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!sAgeGroupBottom.getSelectedItem().toString().equalsIgnoreCase("")){
            age = sAgeGroupBottom.getSelectedItem().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(useAgeRange){
            int bottomPos = sAgeGroupBottom.getSelectedItemPosition();
            int topPos = sAgeGroupTop.getSelectedItemPosition();
            if(bottomPos > topPos){
                Toaster.showShort(this.getActivity(), "Make sure selected age groups are correct!");
                return false;
            } else {
                if((topPos - bottomPos) > 5){
                    Toaster.showShort(this.getActivity(), "Max age range is 5!");
                    return false;
                }
            }
        }



        if(!etDescription.getText().toString().equalsIgnoreCase("")){
            description = etDescription.getText().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        Singleton singleton = Singleton.getInstance();
        creator = singleton.getCurrentUser().getEmail();

        return true;
    }

    /**
     * Gets the index of a string in a spinner.
     * @param spinner
     * @param item
     * @return
     */
    private int getIndex(Spinner spinner, String item){
        int index = 0;

        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index = i;
            }
        }
        return index;
    }

    public void setParentActivity(AddDrillActivity parentActivity){
        this.parentActivity = parentActivity;
    }

    public void setParent(Fragment parent){
        if(parent instanceof LibraryListFragment){
            this.parent = (LibraryListFragment) parent;
        }else if(parent instanceof AgeFragment){
            this.parent = (AgeFragment) parent;
        } else if(parent instanceof TypeFragment){
            this.parent = (TypeFragment) parent;
        } else if(parent instanceof DrillListFragment){
            this.parent = (DrillListFragment) parent;
        }else if(parent instanceof SessionListFragment){
            this.parent = (SessionListFragment) parent;
        }
    }
//
    private void refreshList() {
        if (parent instanceof LibraryListFragment) {
            ((LibraryListFragment) parent).refresh();
        } else if (parent instanceof AgeFragment) {
            ((AgeFragment) parent).refresh();
        } else if (parent instanceof TypeFragment) {
            ((TypeFragment) parent).refresh();
        } else if (parent instanceof DrillListFragment) {
            ((DrillListFragment) parent).refresh();
        } else if (parent instanceof SessionListFragment) {
            ((SessionListFragment) parent).refresh();
        }
    }

    /**
     * USED FOR ADDING A DRILL USING PARSE OBJECT
     */
    public void addDrillObject(){
        groupId = getHash();
        Toaster.showShort(this.getActivity(), "Creating drills, please Wait.");

        if(!useAgeRange){
            final DrillObject drillObject = assembleDrillObject(false);
            drillObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        if (hasPicture) {
                            final DrillPictureObject drillPicture = new DrillPictureObject();
                            drillPicture.setDrillId(drillObject.getString("groupId"));
                            drillPicture.setDrillPicture(picture);
                            drillPicture.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (typeSelected) refreshList();
                                    AddDrillFragment.this.close();
                                }
                            });
                        } else {
                            if (typeSelected) refreshList();
                            AddDrillFragment.this.close();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            int bottomPos = sAgeGroupBottom.getSelectedItemPosition();
            int topPos = sAgeGroupTop.getSelectedItemPosition();

            if(bottomPos == topPos){
                final ParseObject drillObject = assembleDrillObject(false);
                drillObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            if (hasPicture) {
                                DrillPictureObject drillPicture = new DrillPictureObject();
                                drillPicture.setDrillId(drillObject.getString("groupId"));
                                drillPicture.setDrillPicture(picture);
                                drillPicture.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (typeSelected) refreshList();
                                        AddDrillFragment.this.close();
                                    }
                                });
                            } else {
                                if (typeSelected) refreshList();
                                AddDrillFragment.this.close();
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            } else{
                for(int i = bottomPos; i <= topPos; i++){

                    sAgeGroupBottom.setSelection(i);
                    age = sAgeGroupBottom.getSelectedItem().toString();

                    ParseObject drillObject = assembleDrillObject(true);

                    try {
                        drillObject.save();
                    } catch (ParseException e) {}

                    if(i == bottomPos) {
                        if (hasPicture) {
                            DrillPictureObject drillPicture = new DrillPictureObject();
                            drillPicture.setDrillId(drillObject.getString("groupId"));
                            drillPicture.setDrillPicture(picture);
                            drillPicture.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (typeSelected) refreshList();
                                    AddDrillFragment.this.close();
                                }
                            });
                        }
                    }

                }
                if(typeSelected) refreshList();
                AddDrillFragment.this.close();
            }
        }
    }

    /**
     * USED FOR ADDING A DRILL USING PARSE OBJECT
     * @param isGroup
     * @return
     */
    private DrillObject assembleDrillObject(boolean isGroup){
        DrillObject drillObject = new DrillObject();
        drillObject.setGroupId(groupId);
        drillObject.setDrillName(drillName);
        drillObject.setDrillType(type);
        drillObject.setDrillAge(age);
        drillObject.setDrillDescription(description);
        drillObject.setCreator(creator);
        drillObject.setDrillRating(0);
        drillObject.setNumberOfRatings(0);
        drillObject.setTimesUsed(0);
        drillObject.setIsGroup(isGroup);
        drillObject.setHasPicture(hasPicture);
        return drillObject;


    }

    /**
     * Creates a hash for grouping drills of an age range.
     * @return
     */
    private String getHash(){
        Singleton singleton = Singleton.getInstance();
        String toHash = singleton.getCurrentUser().getUsername() + System.currentTimeMillis();
        byte[] thedigest = {};

        //Creates the hash for the groupId
        try {
            byte[] bytesOfMessage = toHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            thedigest = md.digest(bytesOfMessage);

        } catch (UnsupportedEncodingException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        return thedigest.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == this.getActivity().RESULT_OK && null != data){

            hasPicture = true;


            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = this.getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            drillImage.setVisibility(View.VISIBLE);
            btnAddImage.setVisibility(View.GONE);
            drillImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] picData = bos.toByteArray();

            final ParseFile pictureToSave = new ParseFile("drillPicture.jpeg", picData);
            pictureToSave.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        picture = pictureToSave;
                    } else {
                        //Saving object failed.
                    }
                }
            });
        }
    }

    private void close(){
        parentActivity.finish();
    }
}
