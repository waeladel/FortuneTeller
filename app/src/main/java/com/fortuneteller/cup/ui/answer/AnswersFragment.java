package com.fortuneteller.cup.ui.answer;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortuneteller.cup.adapters.AnswersAdapter;
import com.fortuneteller.cup.models.Answer;
import com.fortuneteller.cup.ui.CameraPermissionAlertFragment;
import com.fortuneteller.cup.Interface.ItemClickListener;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.FragmentAnswerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kotlinx.coroutines.CoroutineScope;

import static android.app.Activity.RESULT_OK;
import static com.fortuneteller.cup.Utils.FilesHelper.createImageUri;

public class AnswersFragment extends Fragment implements ItemClickListener {
    private final static String TAG = AnswersFragment.class.getSimpleName();

    private AnswersViewModel mViewModel;
    private FragmentAnswerBinding mBinding;
    private Context mContext;
    private Activity activity;
    private FragmentManager mFragmentManager;
    private LinearLayoutManager mLinearLayoutManager;

    private  static final String PERMISSION_RATIONALE_FRAGMENT = "cameraPermissionFragment";
    private static final int REQUEST_CAMERA_PERMISSIONS_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 111;
    // Random number generator
    private static int mRandomAnswer ;// 0 - 7;

    private String[] mSpinnerArray;
    private ArrayAdapter<String> mSpinnerAdapter;

    private RecyclerView mRecycler;
    private ArrayList<Answer> mArrayList;
    private AnswersAdapter mAdapter;

    public static AnswersFragment newInstance() {
        return new AnswersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(AnswersViewModel.class);
        mFragmentManager = getChildFragmentManager(); // Needed to open the rational dialog

        // prepare the Adapter in onCreate to use only one Adapter
        mArrayList = new ArrayList<>();
        mAdapter = new AnswersAdapter(mContext, new AnswersAdapter.AnswerComparator(),this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
        mBinding = FragmentAnswerBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        /*mBinding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Accept button click");
                if (!isPermissionGranted()) {
                    requestPermission();
                }else{
                    openCamera();
                }
            }
        });*/

        // Initiate the RecyclerView
        mBinding.answersRecycler.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mBinding.answersRecycler.setLayoutManager(mLinearLayoutManager);
        mBinding.answersRecycler.setAdapter(mAdapter);
        
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the answers list to update the recycler view
        // It's best to observe on onActivityCreated so that we dona't have to update ViewModel manually.
        // This is because LiveData will not call the observer since it had already delivered the last result to that observer.
        // But recycler adapter is updated any way despite that LiveData delivers updates only when data changes, and only to active observers.
        // Use getViewLifecycleOwner() instead of this, to get only one observer for this view
        mViewModel.getAnswers().observe(getViewLifecycleOwner(), new Observer<PagingData<Answer>>() {
            @Override
            public void onChanged(PagingData<Answer> answerPagingData) {
                // Observe the PagingData stream, and pass each generated value to your adapter's submitData() method.

                // Activities can use getLifecycle() directly, but Fragments should instead use
                // getViewLifecycleOwner().getLifecycle().
                mAdapter.submitData(getViewLifecycleOwner().getLifecycle(), answerPagingData);
            }
        });

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
        Log.d(TAG, "requestCode ="+ requestCode + " resultCode "+ resultCode);
        if(resultCode == RESULT_OK){
            // Photo captured successfully
            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    // returning from the camera after capture the photo
                    Log.d(TAG, "open camera requestCode= "+ requestCode);
                    // Display the fortune message
                    Random random = new Random();
                    mRandomAnswer = random.nextInt(11) ; // from 0 - 10

                    // Create an ArrayAdapter that will contain all list items
                    mSpinnerArray = getResources().getStringArray(R.array.answers_array);

                    /* Assign the name array to that adapter and
                    also choose a simple layout for the list items */
                    mSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mSpinnerArray);

                    Log.d(TAG, "onActivityResult: answer= "+mSpinnerAdapter.getItem(mRandomAnswer));
                    break;
            }
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