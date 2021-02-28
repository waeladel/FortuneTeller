package com.fortuneteller.cup.ui.editprofile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fortuneteller.cup.databinding.FragmentEditProfileBinding;
import com.fortuneteller.cup.ui.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class EditProfileFragment extends Fragment  {
    private final static String TAG = EditProfileFragment.class.getSimpleName();

    private EditProfileViewModel mViewModel;
    private FragmentEditProfileBinding mBinding;
    private Context mContext;
    private Activity activity;
    private FragmentManager mFragmentManager;

    private String[] mTestArray;
    private ArrayAdapter<String> mAdapter;
    private Long birthInMillis;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
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
        //return inflater.inflate(R.layout.fragment_main, container, false);
        mBinding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        // Save profile when save button is clicked
        mBinding.birthContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open date picker
                DatePickerFragment datePicker;
                datePicker = new DatePickerFragment(mContext);
                if (getChildFragmentManager() != null) {
                    datePicker.setCallBack(ondate); //Set Call back to capture selected date
                    datePicker.show(getChildFragmentManager(),"date picker");
                    Log.i(TAG, "datePicker show clicked ");
                }

            }
        });

        return view;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //A call back to capture selected date
    private DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            String birthDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
            // set EditProfileViewModel.user values
            if(c.getTime() != null){
                mBinding.birthValue.setText(birthDate);
                //mEditProfileViewModel.getUser().setBirthDate(birthInMillis);
            }
            /*c.getTimeInMillis();
            DateHelper.getBirthDate(c.getTime());
            c.getTime();*/
        }
    };

}