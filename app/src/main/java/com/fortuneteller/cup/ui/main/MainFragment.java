package com.fortuneteller.cup.ui.main;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fortuneteller.cup.CameraPermissionAlertFragment;
import com.fortuneteller.cup.Interface.ItemClickListener;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.MainFragmentBinding;

import java.util.List;

import static com.fortuneteller.cup.Utils.FilesHelper.createImageUri;

public class MainFragment extends Fragment implements ItemClickListener {
    private final static String TAG = MainFragment.class.getSimpleName();

    private MainViewModel mViewModel;
    private MainFragmentBinding mBinding;
    private Context mContext;
    private Activity activity;
    private FragmentManager mFragmentManager;
    private  static final String PERMISSION_RATIONALE_FRAGMENT = "cameraPermissionFragment";
    private static final int REQUEST_CAMERA_PERMISSIONS_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 111;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getChildFragmentManager(); // Needed to open the rational dialog

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
        mBinding = MainFragmentBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mBinding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Accept button click");
                if (!isPermissionGranted()) {
                    requestPermission();
                }else{
                    openCamera();
                }
            }
        });
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof Activity){// check if fragmentContext is an activity
            activity =(Activity) context;
        }

        /*Album.initialize(AlbumConfig.newBuilder(activity)
                .setAlbumLoader(new MediaLoader())
                .build());*/
    }

    private void openCamera() {
        Log.i(TAG, "selectMedia. capture picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        List<ResolveInfo> listPhotosCam = mContext.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (listPhotosCam.size() <= 0) {
            Log.i(TAG, "No camera app to handel the intent");
            Toast.makeText(mContext, R.string.no_camera_app_error, Toast.LENGTH_LONG).show();
            return;
        }
        // Create a file location to tell the camera to save the new photo at it
        Uri mMediaUri = createImageUri(mContext);
        if(mMediaUri != null){
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri); // Pass the new file uri in the intent extras
        }
        Log.d(TAG, "mMediaUri= "+mMediaUri);
                /*takePictureIntent.setClipData(ClipData.newRawUri("", mediaUri));
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/
        try {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    // If Storage and camera permissions are granted return true so that we stop asking for permissions
    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "is permission Granted= "+(ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));
            return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        }else{
            Log.d(TAG, "is permission Granted= "+(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));

            return (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        }

    }

    private void requestPermission() {
        // Permission is not granted
        // Should we show an explanation?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // API level 29 Android 10 and higher
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Log.i(TAG, "requestPermission: permission should show Rationale");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionRationaleDialog();
            } else {
                // No explanation needed; request the permission
                Log.i(TAG, "requestPermission: No explanation needed; request the permission");
                // using requestPermissions(new String[] instead of ActivityCompat.requestPermissions(this, new String[] to get onRequestPermissionsResult in the fragment
                requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
            }
        }else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Log.i(TAG, "requestPermission: permission should show Rationale");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionRationaleDialog();
            } else {
                // No explanation needed; request the permission
                Log.i(TAG, "requestPermission: No explanation needed; request the permission");
                // using requestPermissions(new String[] instead of ActivityCompat.requestPermissions(this, new String[] to get onRequestPermissionsResult in the fragment
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
            }
        }
    }

    private void showPermissionRationaleDialog() {
        CameraPermissionAlertFragment permissionRationaleDialog = CameraPermissionAlertFragment.newInstance(mContext, this);
        permissionRationaleDialog.show(mFragmentManager, PERMISSION_RATIONALE_FRAGMENT);
        Log.i(TAG, "showPermissionRationaleDialog: permission AlertFragment show clicked ");
    }

    // Get Request Permissions Result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult we got a permissions result");
        if (requestCode == REQUEST_CAMERA_PERMISSIONS_CODE) {
            // If request is cancelled, the result arrays are empty.
            // Camera permission is not a must, we can proceed with reading photos from gallery
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the task you need to do.
                Log.i(TAG, "onRequestPermissionsResult permission was granted");
                openCamera();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Log.i(TAG, "onRequestPermissionsResult permission denied");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode ="+ requestCode);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                // returning from the camera after capture the photo
                Log.d(TAG, "open camera requestCode= "+ requestCode);
                // Display the fortune message
                break;
        }
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        // Listen to dialog buttons onClick
        Log.d(TAG, "item clicked position= " + position + " View= "+view);
        if(view == null && position == 1){
            // dialog's Positive button clicked
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // API level 29 Android 10 and higher
                // OK button of the permission dialog is clicked, lets ask for permissions
                requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
            }else{
                // OK button of the permission dialog is clicked, lets ask for permissions
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
            }

            return; // No need to check other clicks, it's the OK button of the permission dialog

        }

    }
}