package com.thangtd.jackystore.commons;

import android.app.Application;

import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication;

public class MyApplication extends Application {

    private String name;
    private String oathcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOathcode() {
        return oathcode;
    }

    public void setOathcode(String oathcode) {
        this.oathcode = oathcode;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZaloSDKApplication.wrap(this);
    }
}
