package com.stock;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.stock.baseclass.BaseAppCompactActivity;
import com.stock.databinding.ActivitySignUpBinding;
import com.stock.interfaces.SelectPhoto;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.SignUpResponse;
import com.stock.utility.Compressor;
import com.stock.utility.MyPref;
import com.stock.utility.PermissionUtils;
import com.stock.utility.Utility;
import com.stock.widget.CustomDialogClass;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.Permission;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static android.R.attr.bitmap;
import static com.stock.ForgotPasswordActivity.isValidEmaillId;


public class SignUpActivity extends BaseAppCompactActivity implements OnApiResponseListner, View.OnTouchListener, SelectPhoto {

    private ActivitySignUpBinding mBinding;
    private Call<?> callRegister;
    private Bitmap bitmap, compressorbitmap;
    private int PICK_IMAGE_CAMERA = 0;
    private int PICK_IMAGE_GALLARY = 1;
    private String imagepath = null;
    private String device_token;
    private CustomDialogClass cdd;
    private ByteArrayOutputStream stream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        ButterKnife.bind(this);
        device_token = FirebaseInstanceId.getInstance().getToken();


    }


    @OnClick(R.id.act_signup_back)
    public void signupBackPress() {
        onBackPressed();
    }

    @OnClick(R.id.act_signup_next)
    public void nextStep() {
        if (isValid())
            if (Utility.haveNetworkConnection(this)) {
                showDialog();
                if(bitmap != null) {
                    callRegister = ((StockApp) getApplication()).getApiTask().signUp(
                            mBinding.actSignupFirstname.getText().toString().trim(),
                            mBinding.actSignupLastname.getText().toString().trim(),
                            mBinding.actSignupEmail.getText().toString().trim(),
                            mBinding.actSignupPassword.getText().toString().trim(),
                            mBinding.actSignupPhone.getText().toString().trim(),
                            imagepath, device_token,
                            new APICallback(this, APICall.REGISTER_REQ_CODE, this));
                }else{
                    callRegister = ((StockApp) getApplication()).getApiTask().signUp(
                            mBinding.actSignupFirstname.getText().toString().trim(),
                            mBinding.actSignupLastname.getText().toString().trim(),
                            mBinding.actSignupEmail.getText().toString().trim(),
                            mBinding.actSignupPassword.getText().toString().trim(),
                            mBinding.actSignupPhone.getText().toString().trim(),
                            device_token,
                            new APICallback(this, APICall.REGISTER_REQ_CODE, this));
                }

            } else {
                Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
            }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (callRegister != null && !callRegister.isCanceled())
            callRegister.cancel();
    }

    @OnClick(R.id.act_signup_image)
    public void selectImage() {

//        cdd = new CustomDialogClass(this);
//        cdd.show();
        new CustomDialogClass(this).show(getSupportFragmentManager(), "");
    }

    private boolean isValid() {
        if (mBinding.actSignupFirstname.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "First name is Required");
            return false;
        }
        if (mBinding.actSignupLastname.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Last name is Required");
            return false;
        }

        if (mBinding.actSignupEmail.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Email is required");
            return false;
        }
        if (!isValidEmaillId(mBinding.actSignupEmail.getText().toString().trim())) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Valid Email is Required");
            return false;
        }
        if (mBinding.actSignupPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Password is Required");
            return false;
        }
        if (mBinding.actSignupPhone.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), "Phone is Required");
            return false;
        }
        return true;
    }

    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        if (requestCode == APICall.REGISTER_REQ_CODE) {
            dismissDialog();
            if (clsGson instanceof SignUpResponse) {
                SignUpResponse signUpResponse = (SignUpResponse) clsGson;
                getMyPref().setData(MyPref.Keys.TOKEN, signUpResponse.getToken());
                getMyPref().setData(MyPref.Keys.ISLOGIN, true);
                ((StockApp) getApplication()).refreshToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(SignUpActivity.this);
            }
        }

    }

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        if (errorMessage instanceof MessageResponse) {

            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    private void activeCamera() {


        PermissionEverywhere.getPermission(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}).enqueue(new PermissionResultCallback() {
            @Override
            public void onComplete(PermissionResponse permissionResponse) {
                if (permissionResponse.isAllGranted()) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);

                }
            }
        });
    }

    private void activeGallary() {
        PermissionEverywhere.getPermission(this,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}).enqueue(new PermissionResultCallback() {
            @Override
            public void onComplete(PermissionResponse permissionResponse) {
                if (permissionResponse.isAllGranted()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "select image"), PICK_IMAGE_GALLARY);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLARY && resultCode == RESULT_OK) {
            onSelectFromGallaryResult(data);
        } else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            bitmap = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            compressorbitmap = new Compressor(this).compressToBitmap(finalFile);
//            byte[] bytesProfile = Utility.convertBitmapToByteArray(bitmap);
            byte[] bytesProfile = stream.toByteArray();
            imagepath = Utility.saveBitmapOnSDCard(this, "Image_" + String.valueOf(System.currentTimeMillis()) + ".png", bytesProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBinding.actSignupImage.setImageBitmap(compressorbitmap);
    }

    private void onSelectFromGallaryResult(Intent data) {
        Uri uri = data.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            compressorbitmap = new Compressor(this).compressToBitmap(finalFile);
//            byte[] bytesProfile = Utility.convertBitmapToByteArray(bitmap);
            byte[] bytesProfile = stream.toByteArray();
            imagepath = Utility.saveBitmapOnSDCard(this, "Image_" + String.valueOf(System.currentTimeMillis()) + ".png", bytesProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBinding.actSignupImage.setImageBitmap(compressorbitmap);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utility.hideKeyboared(SignUpActivity.this);
        return false;
    }

    @Override
    public void getGallary() {
        activeGallary();
    }

    @Override
    public void getCamera() {
        activeCamera();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        stream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
