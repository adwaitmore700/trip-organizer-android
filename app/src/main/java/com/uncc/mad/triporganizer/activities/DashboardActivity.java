package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.ServiceHelper;

import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity implements IData {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    @Override
    public void sendResponse(ServiceHelper response, String objectTypeModel) {

    }
}
