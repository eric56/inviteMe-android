package br.com.android.invviteme;

import android.app.Application;
import android.content.Context;

import br.com.android.invviteme.utils.TypefaceUtil;

public class InvviteMe extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setFont(Context context){
        TypefaceUtil.overrideFont(context, "DEFAULT", "fonts/OpenSans-Regular.ttf");
        TypefaceUtil.overrideFont(context, "MONOSPACE","fonts/OpenSans-Regular.ttf");
        TypefaceUtil.overrideFont(context, "SERIF","fonts/OpenSans-Regular.ttf");
        TypefaceUtil.overrideFont(context, "SANS_SERIF","fonts/OpenSans-Regular.ttf");
    }
}
