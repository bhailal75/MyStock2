package com.stock.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stock.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * Created by sparken09 on 27/10/17.
 */

public class Utility {
    private static DialogFragment dateDialogFragment;
    public static long doubleTap = 0;

    public final static int CLICK_INTERVAL = 2000;
    public static long doubleTapTime;
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static void navigationIntent(Context context, Class<?> classname) {
        Intent intent = new Intent(context, classname);
        context.startActivity(intent);
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void loadImage(String url, ImageView imageView) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(R.drawable.placeholder);
            return;
        }
        if (imageView == null)
            return;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public static void loadImage(String url, ImageView imageView, int placeHolder) {
        if (url == null || url.equals("")) {
            imageView.setImageResource(placeHolder);
            return;
        }
        if (imageView == null)
            return;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeHolder)
                .showImageForEmptyUri(placeHolder)
                .showImageOnFail(placeHolder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(Utility.DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public static void hideKeyboared(Activity activity) {
        if (activity == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(),
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static void log(String msg) {
        Log.i("Noty", msg);
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

//
//    public static void showUpSnackBar(Activity activity, String text) {
//        try {
//            if (activity != null) {
//                View view = activity.getLayoutInflater().inflate(R.layout.row_error_message, null);
//                TextView customTextView = (TextView) view.findViewById(R.id.row_tv_sucess_message);
//                customTextView.setText(text);
//                Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
//                Crouton.make(activity, view).setConfiguration(croutonConfiguration).show();
//            }
//        } catch (Exception ae) {
//            ae.printStackTrace();
//        }
//    }

//    public static void showUpSnackBar(View view, String text) {
//        try {
//
//            Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
//            sb.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
//            sb.show();
//        } catch (Exception ae) {
//            ae.printStackTrace();
//        }
//    }

//
//    public static void showRedSnackBar(Activity activity, String text) {
//        try {
//
//
//
//            if (activity != null) {
//                View view = activity.getLayoutInflater().inflate(R.layout.row_error_message_red, null);
//                TextView customTextView = (TextView) view.findViewById(R.id.row_tv_fail_message);
//                customTextView.setText(text);
////                Configuration croutonConfiguration = new Configuration.Builder().setDuration(1000).build();
////                Crouton.make(activity, view).setConfiguration(croutonConfiguration).show();
//                Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
//                sb.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
//                sb.show();
//            }
//
//
//        } catch (Exception ae) {
//            ae.printStackTrace();
//        }
//    }

    public static void showRedSnackBar(View view, String text) {
        try {
            Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
            sb.show();
        } catch (Exception ae) {
            ae.printStackTrace();
        }
    }

    public static Calendar getProperCalendar(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Calendar calendarDate = Calendar.getInstance();
        if (calendarDate != null) {
            try {
                calendarDate.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendarDate;
    }

    public static String saveBitmapOnSDCard(Context context, String fileName, byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        File folder = new File(context.getExternalCacheDir(), "Images");
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File file = new File(folder, fileName);
        if (file.exists()) file.delete();

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        // String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        return byte_arr;
    }

}
