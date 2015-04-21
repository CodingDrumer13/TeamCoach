package com.lsus.teamcoach.teamcoachapp.ui.Library.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Session;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.AgeFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.Drill.DrillListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.LibraryListFragment;
import com.lsus.teamcoach.teamcoachapp.ui.Library.TypeFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.BOTTOM_AGE;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.SESSION_ID;
import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.TOP_AGE;

/**
 * Created by TeamCoach on 4/10/2015.
 */
public class AddSessionDialogFragment extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    @InjectView(R.id.btnCancelAddSession) protected Button btnCancelAddSession;
    @InjectView(R.id.btnAddSession) protected Button btnAddSession;
    @InjectView(R.id.etAddSessionName) protected EditText etSessionName;
    @InjectView(R.id.sAddSessionType) protected Spinner sSessionType;
    @InjectView(R.id.sAddSessionAgeGroupBottom) protected Spinner sAgeGroupBottom;
    @InjectView(R.id.sAddSessionAgeGroupTop) protected Spinner sAgeGroupTop;
    @InjectView(R.id.tv_from) protected TextView tvFrom;
    @InjectView(R.id.tv_to) protected TextView tvTo;
    @InjectView(R.id.btnAddAgeGroup) protected Button btnAddAgeGroup;
    @InjectView(R.id.ck_MakePublic) protected CheckBox ckMakePublic;

    Intent addSessionIntent;

    @Inject
    BootstrapService bootstrapService;

    private SafeAsyncTask<Boolean> authenticationTask;

    private boolean ageSelected;
    private boolean typeSelected;
    private String groupId;
    private String sessionName;
    private String age;
    private String type;
    private boolean useAgeRange = false;
    private String creator;
    private boolean isPublic;

    private Session session;

    private Fragment parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Create Session");

        View view = inflater.inflate(R.layout.add_session_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnCancelAddSession.setOnClickListener(this);
        btnAddSession.setOnClickListener(this);

        //TODO Change visibility to make age ranges.
        //TODO Change SessionInfoActivity to handle ranges.
        btnAddAgeGroup.setVisibility(View.GONE);
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
                R.array.session_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sSessionType.setAdapter(typeAdapter);

        sAgeGroupBottom.setOnItemSelectedListener(this);

        if(ageSelected){
            sAgeGroupBottom.setSelection(getIndex(sAgeGroupBottom, age));
        }

        if(typeSelected){
            sSessionType.setSelection(getIndex(sSessionType, type));
        }
    }

    @Override
    public void onClick(View view) {
        if(btnAddSession.getId() == view.getId()) {
            if(validateFields()){
                prepareSession();
            }
        }

        if(btnAddAgeGroup.getId() == view.getId()){
            tvFrom.setVisibility(View.VISIBLE);
            tvTo.setVisibility(View.VISIBLE);
            sAgeGroupTop.setVisibility(View.VISIBLE);
            btnAddAgeGroup.setVisibility(View.GONE);

            useAgeRange = true;
        }

        if(btnCancelAddSession.getId() == view.getId()){
            dismiss();
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
        if (!etSessionName.getText().toString().equalsIgnoreCase("")){
            sessionName = etSessionName.getText().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!sSessionType.getSelectedItem().toString().equalsIgnoreCase("")) {
            type = sSessionType.getSelectedItem().toString();
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

    private void prepareSession(){
        groupId = getHash();
        if(ckMakePublic.isChecked()){
            isPublic = true;
        } else {
            isPublic = false;
        }

        addSessionIntent = new Intent(getActivity(), SessionInfoActivity.class);

        if(!useAgeRange){
            addSessionIntent.putExtra(SESSION, assembleSession(false));
            finishAdding(assembleSession(false));
        } else {
            int bottomPos = sAgeGroupBottom.getSelectedItemPosition();
            int topPos = sAgeGroupTop.getSelectedItemPosition();

            if(bottomPos == topPos){
                addSessionIntent.putExtra(SESSION, assembleSession(false));
            } else{
                addSessionIntent.putExtra(SESSION, assembleSession(false));
                addSessionIntent.putExtra(BOTTOM_AGE, sAgeGroupBottom.getSelectedItem().toString());
                addSessionIntent.putExtra(TOP_AGE, sAgeGroupTop.getSelectedItem().toString());
            }
        }
//        //TODO change back and remove finishAdding method to not use parse.com
//        startActivity(addSessionIntent);
//        dismiss();
    }

    private void finishAdding(final Session session){
        authenticationTask = new SafeAsyncTask<Boolean>() {
            public Boolean call() throws Exception {
                AddSessionDialogFragment.this.setSession(bootstrapService.addSession(session));
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
                addSessionIntent.putExtra(SESSION_ID, AddSessionDialogFragment.this.getSession().getObjectId());
                //addSessionIntent.putExtra(SESSION, AddSessionDialogFragment.this.getSession());
                startActivity(addSessionIntent);
                Toaster.showLong(getActivity(), "Press edit to add your first drill.");
                dismiss();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                hideProgress();
                authenticationTask = null;
            }
        };
        authenticationTask.execute();
    }

    private Session assembleSession(boolean isGroup){
        Session session = new Session(groupId, sessionName, type, age, isPublic, creator);
        session.setIsGroup(isGroup);
        session.setDrillList(new ArrayList<Drill>());
        return session;
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

    public void setSession(Session session) { this.session = session; }

    public Session getSession() { return session; }
}
