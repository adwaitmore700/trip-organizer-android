package com.uncc.mad.triporganizer.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uncc.mad.triporganizer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileAddEditFragment extends Fragment {


    public UserProfileAddEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_add_edit, container, false);
    }

}
