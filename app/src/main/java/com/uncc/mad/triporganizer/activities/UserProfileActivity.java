package com.uncc.mad.triporganizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.uncc.mad.triporganizer.R;
import com.uncc.mad.triporganizer.interfaces.IData;
import com.uncc.mad.triporganizer.models.ServiceHelper;

public class UserProfileActivity extends AppCompatActivity implements IData {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public void sendResponse(ServiceHelper response, String objectTypeModel) {

    }
}
