package com.stock.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtils {
    private static final int PERMISSION_REQUEST_CODE = 9999;

    private PermissionResultListener permissionResultListener;
    private Map<String, Boolean> permissionResult;


    public void checkPermission(Activity activity, String[] permissions, int requestCode,
                                @NonNull PermissionResultListener permissionResultListener) {
        this.permissionResultListener = permissionResultListener;
        permissionResult = new HashMap<>();
        ArrayList<String> alRequestPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                alRequestPermissions.add(permission);
            } else {
                permissionResult.put(permission, true);
            }
        }
        if (alRequestPermissions.size() > 0) {
            String requestPermission[] = new String[alRequestPermissions.size()];
            for (int counter = 0; counter < requestPermission.length; counter++) {
                requestPermission[counter] = alRequestPermissions.get(counter);
            }
            ActivityCompat.requestPermissions(activity, requestPermission, PERMISSION_REQUEST_CODE);
        } else {
            permissionResultListener.onPermissionResult(permissionResult, requestCode);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int counter = 0; counter < permissions.length; counter++) {
                permissionResult.put(permissions[counter], grantResults[counter] == PackageManager.PERMISSION_GRANTED);
            }
            permissionResultListener.onPermissionResult(permissionResult, requestCode);
        }
    }

    public interface PermissionResultListener {
        void onPermissionResult(Map<String, Boolean> permissionResult, int requestCode);
    }
}