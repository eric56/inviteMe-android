package br.com.android.invviteme.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Rosa on 08/05/2016.
 */
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

}
