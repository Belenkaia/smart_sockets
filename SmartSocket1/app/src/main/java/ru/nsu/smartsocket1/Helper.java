package ru.nsu.smartsocket1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class Helper {
    protected static final String TAG = "SmartSockets";
    public static String getMetaData(Context context, String name)
    {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            Log.e(TAG, "Unable to load meta-data: " + e.getMessage());
        }
        return  null;
    }
}
