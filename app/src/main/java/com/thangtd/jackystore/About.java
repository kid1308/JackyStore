package com.thangtd.jackystore;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class About extends AppCompatActivity {

    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        PackageInfo infoCurrent;
        try {
            infoCurrent = getPackageManager().getPackageInfo("com.thangtd.jackystore", 0);
            currentVersion = infoCurrent.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = getString(R.string.app_version) + currentVersion;
        TextView tvVersion = findViewById(R.id.aboutVERSION);
        tvVersion.setText(version);
    }
}
