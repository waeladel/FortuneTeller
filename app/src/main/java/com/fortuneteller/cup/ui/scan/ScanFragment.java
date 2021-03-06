package com.fortuneteller.cup.ui.scan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.fortuneteller.cup.Interface.ItemClickListener;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.FragmentScanBinding;
import com.fortuneteller.cup.models.Answer;
import com.fortuneteller.cup.ui.AnswerAlertFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ScanFragment extends Fragment implements ItemClickListener {
    private final static String TAG = ScanFragment.class.getSimpleName();

    private ScanViewModel mViewModel;
    private FragmentScanBinding mBinding;
    private Context mContext;
    private Activity activity;
    private FragmentManager mFragmentManager;

    private String[] mSpinnerArray;
    private ArrayAdapter<String> mSpinnerAdapter;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private  static final String ANSWER_ALERT_FRAGMENT = "answerAlertFragment";
    private NavController navController ;

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey("imageUri")) {
            mImageUri = Uri.parse(ScanFragmentArgs.fromBundle(getArguments()).getImageUri()); // any user
        }

        mViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        mFragmentManager = getChildFragmentManager(); // Needed to open the rational dialog
        navController = NavHostFragment.findNavController(this);

        try {
            Log.d(TAG, "onAttach: mImageUri= "+ mImageUri);
            mBitmap = handleSamplingAndRotationBitmap(mContext, mImageUri);
            Log.d(TAG, "handleSamplingAndRotationBitmap: Bitmap width= "+ mBitmap.getWidth());
            Log.d(TAG, "onAttach: mImageUri= "+ mImageUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.main_fragment, container, false);
        mBinding = FragmentScanBinding.inflate(inflater, container, false);
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

        mBinding.imageView.setImageBitmap(mBitmap);

        // Start the scanning animation
        startScan();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void onClick(View view, int position, boolean isLongClick) {
        // Listen to dialog buttons onClick
        Log.d(TAG, "item clicked position= " + position + " View= "+view);
        if(view == null && position == 1){
            // dialog's Positive button clicked. lets go to answers
            //navController.navigate(R.id.answersFragment);
            navController.navigate(R.id.action_scan_to_answers);


            return; // No need to check other clicks, it's the OK button of the permission dialog

        }

    }

    private void startScan() {
        mBinding.radar.start();
        mBinding.radarLayout.setVisibility(View.VISIBLE);

        CountDownTimer scanningTimer = new CountDownTimer( 5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.d(TAG, "onTick.  millisUntilFinished= "+ millisUntilFinished);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "mRemainingTimer onFinish.");
                showAnswer();

                // End UI animation when timer finishes
                mBinding.radar.stop();
                mBinding.radarLayout.setVisibility(View.GONE);
            }
        }.start();


    }

    private void showAnswer() {
        // Display the fortune message
        Random random = new Random();
        // Random number generator
        // 0 - 7;
        int randomAnswer = random.nextInt(11); // from 0 - 10

        // Create an ArrayAdapter that will contain all list items
        mSpinnerArray = getResources().getStringArray(R.array.answers_array);

        /* Assign the name array to that adapter and
        also choose a simple layout for the list items */
        mSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mSpinnerArray);

        Log.d(TAG, "onActivityResult: answer= "+ mSpinnerAdapter.getItem(randomAnswer));
        Log.d(TAG, "onActivityResult: answer item id= "+ mSpinnerAdapter.getItemId(randomAnswer));

        // Add the answer to database
        Answer answer = new Answer(mSpinnerAdapter.getItem(randomAnswer), randomAnswer);
        mViewModel.insert(answer);

        // Play theme music
        mViewModel.playMusic();

        AnswerAlertFragment answerAlertFragment = AnswerAlertFragment.newInstance(mContext, this, mSpinnerAdapter.getItem(randomAnswer));
        answerAlertFragment.show(mFragmentManager, ANSWER_ALERT_FRAGMENT);

    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */

    public Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * The method that will check the current image orientation to decide the rotation angle
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    // Finally the rotation method itself
    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


}