package com.thangtd.jackystore.commons;

import android.Manifest;

public class Constants {
    //
    public static final String URI = "http://jackystore.com";
    // book type
    public static final int LOCAL = 0;
    public static final int FOREIGN = 1;
    public static final int MANGA = 2;
    // manipulate
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    // camera action
    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int SCAN = 2;

    // The request code used in ActivityCompat.requestPermissions()
    // and returned in the Activity's onRequestPermissionsResult()
    public static final int PERMISSION_ALL = 1;
    public static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
}
