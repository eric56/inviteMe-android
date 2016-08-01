package br.com.android.invviteme.utils;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class FacebookUtils {

    public static List<String> getPermissionsReadFacebook(){
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
        return permissions;
    }

    public static List<String> getPermissionsPublishFacebook(){
        List<String> permissions = new ArrayList<>();
        permissions.add("publish_actions");
        return permissions;
    }

    public static boolean isLoggedFacebook(List<String> providers){
        return providers != null && providers.get(0).contains("facebook") && AccessToken.getCurrentAccessToken() != null;
    }

}
