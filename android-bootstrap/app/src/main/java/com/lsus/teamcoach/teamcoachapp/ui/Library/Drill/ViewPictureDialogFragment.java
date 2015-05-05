package com.lsus.teamcoach.teamcoachapp.ui.Library.Drill;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by TeamCoach on 5/4/2015.
 */
public class ViewPictureDialogFragment extends DialogFragment {

    @InjectView(R.id.iv_drillImage) protected ImageView drillPictureContainer;

    private Bitmap pictureBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_picture_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        drillPictureContainer.setImageBitmap(pictureBitmap);
    }

    public void setPictureBitmap(Bitmap bmp) { this.pictureBitmap = bmp; }
}