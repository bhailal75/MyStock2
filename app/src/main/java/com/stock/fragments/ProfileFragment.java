package com.stock.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.stock.LoginActivity;
import com.stock.MainActivity;
import com.stock.R;
import com.stock.StockApp;
import com.stock.baseclass.BaseFragment;
import com.stock.container.BaseContainer;
import com.stock.databinding.FragmentProfileBinding;
import com.stock.interfaces.SelectPhoto;
import com.stock.retrofit.APICall;
import com.stock.retrofit.APICallback;
import com.stock.retrofit.OnApiResponseListner;
import com.stock.retrofit.model.LoginResponse;
import com.stock.retrofit.model.MessageResponse;
import com.stock.retrofit.model.NewsResponse;
import com.stock.retrofit.model.ProfileData;
import com.stock.retrofit.model.ProfileResponse;
import com.stock.utility.Compressor;
import com.stock.utility.MyPref;
import com.stock.utility.Utility;
import com.stock.widget.CustomDialogClass;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;



public class ProfileFragment extends BaseFragment implements OnApiResponseListner,SelectPhoto {
    private FragmentProfileBinding mBinding;
    private Call<?> callProfile;
    private ProfileData profileData;
    private int PICK_IMAGE_CAMERA = 0;
    private int PICK_IMAGE_GALLARY = 1;
    private Bitmap bitmap,compressorbitmap;
    private String imagepath;
    private CustomDialogClass cdd;
    private ByteArrayOutputStream stream;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        String token = getMypref().getData(MyPref.Keys.TOKEN);
        try {
            String profile = new String(Base64.decode(token, Base64.DEFAULT), "UTF-8");
            profileData = new GsonBuilder().create().fromJson(profile, ProfileData.class);

            mBinding.frgProfileFirstName.setText(profileData.getFirstName());
            mBinding.frgProfileLastName.setText(profileData.getLastName());
            mBinding.frgProfilePhone.setText(profileData.getPhone());
            mBinding.frgProfileEmail.setText(profileData.getEmail());
            Utility.loadImage(profileData.getImage(), mBinding.frgProfileImage, R.drawable.placeholderprofile);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        if (Utility.haveNetworkConnection(getActivity())) {
//            mBinding.frgProfileLl.setVisibility(View.GONE);
//            mBinding.frgProfilePv.setVisibility(View.VISIBLE);
//
//            callProfile = ((StockApp) getActivity().getApplication()).getApiTask().getProfile(new APICallback(getActivity(),
//                    APICall.PROFILE_CODE, this));
//        } else {
//            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
//        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.frg_profile_edit_btn)
    public void updateProfile() {
        mBinding.frgProfileEditBtn.setVisibility(View.GONE);
        mBinding.frgProfileSaveBtn.setVisibility(View.VISIBLE);
        mBinding.frgProfileFirstName.setEnabled(true);
        mBinding.frgProfileLastName.setEnabled(true);
        mBinding.frgProfilePhone.setEnabled(true);
        mBinding.frgProfileImage.setEnabled(true);
        mBinding.profileImageLl.setEnabled(true);
        mBinding.frgProfileImage.setClickable(true);
        mBinding.editPhotoTxt.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.frg_profile_change_password)
    public void change_password(){
        mBinding.frgProfileLl.setVisibility(View.GONE);
        mBinding.changePasswordLl.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.save_changes_password)
    public void savePassword(){
        Utility.hideKeyboared(getActivity());
        if ((Utility.doubleTapTime + Utility.CLICK_INTERVAL) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            if (isValid()) {
                if (Utility.haveNetworkConnection(getActivity())) {
                    showDialog();
                    callProfile = ((StockApp) getActivity().getApplication()).getApiTask().changePassword(
                            mBinding.currentPassword.getText().toString(),
                            mBinding.newPassword.getText().toString(),
                            new APICallback(getActivity(),
                                    APICall.CHANGE_PASSWORD_CODE, this));
                } else {
                    Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
                }
            }
        }
    }

    @OnClick(R.id.cancel_change_password)
    public void cancelClick(){
        mBinding.frgProfileLl.setVisibility(View.VISIBLE);
        mBinding.changePasswordLl.setVisibility(View.GONE);
    }

    private boolean isValid() {
        if (mBinding.currentPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.msg_current_password));
            return false;
        }
        if (mBinding.newPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.msg_new_password));
            return false;
        }
        if (mBinding.confirmPassword.getText().toString().trim().length() == 0) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.msg_confirm_password));
            return false;
        }
        if (!mBinding.newPassword.getText().toString().trim().equals(mBinding.confirmPassword.getText().toString().trim())) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.change_new_confirm_not_match));
            return false;
        }

        if (mBinding.currentPassword.getText().toString().trim().equals(mBinding.newPassword.getText().toString().trim())) {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.change_current_new_match));
            return false;
        }
        return true;
    }

    @OnClick(R.id.frg_profile_save_btn)
    public void saveProfile() {

        if (Utility.haveNetworkConnection(getActivity())) {
            showDialog();
            if (imagepath != null) {
                callProfile = ((StockApp) getActivity().getApplication()).getApiTask().getUpdateProfile(
                        mBinding.frgProfileFirstName.getText().toString(),
                        mBinding.frgProfileLastName.getText().toString(),
                        mBinding.frgProfilePhone.getText().toString(),
                        imagepath,
                        new APICallback(getActivity(),
                                APICall.UPDATE_PROFILE_CODE, this));
            } else {
                callProfile = ((StockApp) getActivity().getApplication()).getApiTask().getUpdateProfile(
                        mBinding.frgProfileFirstName.getText().toString(),
                        mBinding.frgProfileLastName.getText().toString(),
                        mBinding.frgProfilePhone.getText().toString(),
                        new APICallback(getActivity(),
                                APICall.UPDATE_PROFILE_CODE, this));
            }

        } else {
            Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (callProfile != null && !callProfile.isCanceled())
            callProfile.cancel();
    }

    @OnClick(R.id.frg_profile_image)
    public void editProfileImage() {

if(mBinding.frgProfileEditBtn.getVisibility()==View.GONE){
        new CustomDialogClass(this).show(getChildFragmentManager(), "");}
    }


    @Override
    public void onResponseComplete(Object clsGson, int requestCode) {
        dismissDialog();
//        mBinding.frgProfilePv.setVisibility(View.GONE);
//        mBinding.frgProfileLl.setVisibility(View.VISIBLE);
        if (requestCode == APICall.CHANGE_PASSWORD_CODE) {
            if (clsGson instanceof MessageResponse) {
                MessageResponse messageResponse = (MessageResponse) clsGson;
                Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
                mBinding.newPassword.setText("");
                mBinding.currentPassword.setText("");
                mBinding.confirmPassword.setText("");
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
        if (requestCode == APICall.UPDATE_PROFILE_CODE) {
            if (clsGson instanceof LoginResponse) {
                LoginResponse loginResponse = (LoginResponse) clsGson;
                getMypref().setData(MyPref.Keys.TOKEN, loginResponse.getToken());
                getMypref().setData(MyPref.Keys.ISLOGIN, true);
                ((StockApp) getActivity().getApplication()).refreshToken();
                Utility.showRedSnackBar(mBinding.getRoot(), getString(R.string.update_profile));
                mBinding.getRoot().postDelayed(runnable, 1000);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(ProfileFragment.this).attach(ProfileFragment.this).commit();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//            ActivityCompat.finishAffinity(MainActivity.this);

        }
    };

    @Override
    public void onResponseError(Object errorMessage, int requestCode) {
        dismissDialog();
        mBinding.frgProfilePv.setVisibility(View.GONE);
        if (errorMessage instanceof MessageResponse) {
            MessageResponse messageResponse = (MessageResponse) errorMessage;
            Utility.showRedSnackBar(mBinding.getRoot(), messageResponse.getMessage());
        }
    }

    private void activeCamera() {

        PermissionEverywhere.getPermission(getActivity(),
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}).enqueue(new PermissionResultCallback() {
            @Override
            public void onComplete(PermissionResponse permissionResponse) {
                if (permissionResponse.isAllGranted()){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);

                }
            }
        });
    }

    private void activeGallary() {

        PermissionEverywhere.getPermission(getActivity(),
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}).enqueue(new PermissionResultCallback() {
            @Override
            public void onComplete(PermissionResponse permissionResponse) {
                if (permissionResponse.isAllGranted()){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "select image"), PICK_IMAGE_GALLARY);
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

            Uri tempUri = getImageUri(getActivity(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            compressorbitmap = new Compressor(getActivity()).compressToBitmap(finalFile);
//            byte[] bytesProfile = Utility.convertBitmapToByteArray(bitmap);
            byte[] bytesProfile = stream.toByteArray();
            imagepath = Utility.saveBitmapOnSDCard(getActivity(), "Image_" + String.valueOf(System.currentTimeMillis()) + ".png", bytesProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBinding.frgProfileImage.setImageBitmap(compressorbitmap);
    }

    private void onSelectFromGallaryResult(Intent data) {
        Uri uri = data.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            Uri tempUri = getImageUri(getActivity(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            compressorbitmap = new Compressor(getActivity()).compressToBitmap(finalFile);

//            byte[] bytesProfile = Utility.convertBitmapToByteArray(bitmap);
            byte[] bytesProfile = stream.toByteArray();
            imagepath = Utility.saveBitmapOnSDCard(getActivity(), "Image_" + String.valueOf(System.currentTimeMillis()) + ".png", bytesProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        mBinding.actSignupImage.setImageURI(uri);
        mBinding.frgProfileImage.setImageBitmap(compressorbitmap);
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
