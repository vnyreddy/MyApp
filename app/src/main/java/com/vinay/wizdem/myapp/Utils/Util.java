package com.vinay.wizdem.myapp.Utils;

import android.content.Context;
import android.widget.Toast;

import com.vinay.wizdem.myapp.Models.AppData;

/**
 * Created by vinay_1 on 8/20/2017.
 */

public class Util {
    public static AppData appData;
    public static void noInternetToast(Context context){
        Toast.makeText(context,"No Internet..",Toast.LENGTH_SHORT).show();
    }
}
