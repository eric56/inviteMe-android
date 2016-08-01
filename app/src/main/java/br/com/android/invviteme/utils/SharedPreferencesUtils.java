package br.com.android.invviteme.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    //Quando for usar um SharedPreferences para armazenar dados que nao seja do usuario, criar outro id. EX para Cidade => br.com.android,invviteme.ID_SP_CITY
    public static String ID_SP_USER = "br.com.android,invviteme.ID_SP_USER";

    public static void insertInSPUsers(Context context, String key, String value ) {
        SharedPreferences sp = context.getSharedPreferences(ID_SP_USER, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String selectByKeyFromSPUsers(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(ID_SP_USER, Context.MODE_PRIVATE);
        return(sp.getString(key, ""));
    }

    public static void clearSharedPreferenceUsers(Context context ){
        SharedPreferences sp = context.getSharedPreferences(ID_SP_USER, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

}
