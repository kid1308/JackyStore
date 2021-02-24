package com.thangtd.jackystore;

import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener;
import com.zing.zalo.zalosdk.oauth.OauthResponse;

public class LoginListener extends OAuthCompleteListener {
    @Override
    public void onAuthenError(int errorCode, String message) {
    }

    @Override
    public void onGetOAuthComplete(OauthResponse response) {
        String code = response.getOauthCode();
        //Đăng nhập thành công..
    }
}
