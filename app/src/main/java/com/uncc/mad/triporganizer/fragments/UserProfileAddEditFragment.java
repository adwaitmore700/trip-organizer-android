package com.uncc.mad.triporganizer.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
//import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.uncc.mad.triporganizer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileAddEditFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public UserProfileAddEditFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_profile_add_edit, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity().setTitle();
        getView().findViewById(R.id.add_edit_view_iv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(photo.resolveActivity(getActivity().getPackageManager())!=null){
                    Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
