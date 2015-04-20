package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.AgeFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Session.SessionListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.TypeFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

/**
 * Created by TeamCoach on 3/18/2015.
 */
public class AddDrillDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @InjectView(R.id.btnCancelAddDrill) protected Button btnCancelAddDrill;
    @InjectView(R.id.btnAddDrill) protected Button btnAddDrill;
    @InjectView(R.id.etAddDrillName) protected EditText etDrillName;
    @InjectView(R.id.sAddDrillAgeGroupBottom) protected Spinner sAgeGroupBottom;
    @InjectView(R.id.sAddDrillAgeGroupTop) protected Spinner sAgeGroupTop;
    @InjectView(R.id.tv_from) protected TextView tvFrom;
    @InjectView(R.id.tv_to) protected TextView tvTo;
    @InjectView(R.id.btnAddAgeGroup) protected Button btnAddAgeGroup;
    @InjectView(R.id.sAddDrillType) protected Spinner sDrillType;
    @InjectView(R.id.etAddDrillDescription) protected EditText etDescription;

    @Inject BootstrapService bootstrapService;

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


    private Fragment parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Add Drill");

        View view = inflater.inflate(R.layout.add_drill_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnCancelAddDrill.setOnClickListener(this);
        btnAddDrill.setOnClickListener(this);
        btnAddAgeGroup.setOnClickListener(this);

        //Sets up the values for the Age Groups
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sAgeGroupBottom.setAdapter(ageAdapter);
        sAgeGroupTop.setAdapter(ageAdapter);

        //Sets up the values for the Drill Types
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.drill_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
       typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sDrillType.setAdapter(typeAdapter);

        sAgeGroupBottom.setOnItemSelectedListener(this);

        //TODO fix setting the age.
        if(ageSelected){
            sAgeGroupBottom.setSelection(getIndex(sAgeGroupBottom, age));
        }

        if(typeSelected){
            sDrillType.setSelection(getIndex(sDrillType, type));
        }
    }

    @Override
    public void onClick(View view) {
        if(btnAddDrill.getId() == view.getId()) {
            if(validateFields()){
                addDrill();
            }
        }
        if(btnCancelAddDrill.getId() == view.getId()){
            dismiss();
        }

        if(btnAddAgeGroup.getId() == view.getId()){
            tvFrom.setVisibility(View.VISIBLE);
            tvTo.setVisibility(View.VISIBLE);
            sAgeGroupTop.setVisibility(View.VISIBLE);
            btnAddAgeGroup.setVisibility(View.GONE);

            useAgeRange = true;
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
        Toaster.showLong(getActivity(), "Nothing Selected!");
    }

    public void setAgeSelected(Boolean ageSelected){
        this.ageSelected = ageSelected;
    }

    public void setTypeSelected(Boolean typeSelected) { this.typeSelected = typeSelected; }

    public void setAge(String age){
        this.age = age;
    }

    public void setType(String type) { this.type = type; }

    private boolean validateFields(){
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
                Toaster.showShort(getActivity(), "Make sure selected age groups are correct!");
                return false;
            } else {
                if((topPos - bottomPos) > 5){
                    Toaster.showShort(getActivity(), "Max age range is 5!");
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

    private int getIndex(Spinner spinner, String item){
        int index = 0;

        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index = i;
            }
        }
        return index;
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        getActivity().dismissDialog(0);
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

    private void addDrill(){
        groupId = getHash();
        if(!useAgeRange){
            openThread();

        } else {
            int bottomPos = sAgeGroupBottom.getSelectedItemPosition();
            int topPos = sAgeGroupTop.getSelectedItemPosition();

            if(bottomPos == topPos){
                openThread();
            } else{

                ExecutorService es = Executors.newCachedThreadPool();

                for(int i = bottomPos; i <= topPos; i++){

                    sAgeGroupBottom.setSelection(i);
                    age = sAgeGroupBottom.getSelectedItem().toString();

                    es.execute(new Runnable() {
                        @Override
                        public void run() {
                            bootstrapService.addDrill(assembleDrill(true));
                        }
                    });
                    if(i == bottomPos) Toaster.showLong(getActivity(), "Creating drills, please Wait.");
                    try {
                        es.awaitTermination(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {}

                }
                if(typeSelected) refreshList();
                AddDrillDialogFragment.this.dismiss();
            }
        }

    }

    private Drill assembleDrill(boolean isGroup){
        Drill drill = new Drill(groupId, drillName, type, age, description, creator);
        drill.setIsGroup(isGroup);
        return drill;
    }

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

    private void openThread(){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {
                bootstrapService.addDrill(assembleDrill(false));
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
                }
            }

            @Override
            public void onSuccess(final Boolean authSuccess) {
                if(typeSelected) refreshList();
                AddDrillDialogFragment.this.dismiss();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }


}
