package com.fortuneteller.cup.ui.editprofile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.fortuneteller.cup.Interface.DatabaseUserCallback;
import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.FragmentEditProfileBinding;
import com.fortuneteller.cup.models.User;
import com.fortuneteller.cup.ui.DatePickerFragment;
import com.fortuneteller.cup.ui.main.MainViewModel;

import java.text.DateFormat;
import java.util.Calendar;

import static com.fortuneteller.cup.App.USER_SPINNER_GENDER_FEMALE;
import static com.fortuneteller.cup.App.USER_SPINNER_GENDER_MALE;

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
    private NavController navController ;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        mFragmentManager = getChildFragmentManager(); // Needed to open the rational dialog

        navController = NavHostFragment.findNavController(this);

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
                if (mFragmentManager != null) {
                    datePicker.setCallBack(ondate); //Set Call back to capture selected date
                    datePicker.show(getChildFragmentManager(),"date picker");
                    Log.i(TAG, "datePicker show clicked ");
                }

            }
        });

        mBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        // Set user in the viewModel
        if(mViewModel.getUser() == null){
            // Get User from database if it's null
            mViewModel.getUser(1, new DatabaseUserCallback() {
                @Override
                public void onCallback(User user) {
                    if(user != null){
                        Log.d(TAG, "onCallback: user name= "+user.getName());
                        mViewModel.setUser(user);
                        showCurrentUser();
                    }else{
                        Log.d(TAG, "onCallback: there is no current user");
                        mViewModel.setUser(new User());
                    }
                }
            });
        }else{
            Log.d(TAG,  "getUserOnce. user is not null. no need to get user from database "+mViewModel.getUser().getName());
            //currentUser = mEditProfileViewModel.getUser();
            showCurrentUser();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // No need to fetch user, we are creating new one, not editing it.
        /*mViewModel.getUser(1, new DatabaseUserCallback() {
            @Override
            public void onCallback(User user) {
                if(user != null){
                    Log.d(TAG, "onCallback: user name= "+user.getName());
                }else{
                    Log.d(TAG, "onCallback: there is no current user");
                }
            }
        });*/
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
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // To set the visible birthday string to the user
            String birthDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

            birthInMillis = c.getTimeInMillis(); // so store birth on the database

            // set EditProfileViewModel.user values
            if(c.getTime() != null){
                mBinding.birthValue.setText(birthDate);
                //mEditProfileViewModel.getUser().setBirthDate(birthInMillis);
            }

            // set USer birthday
            if(birthInMillis != null){
                mViewModel.getUser().setBirthDate(birthInMillis);
            }

            /*c.getTimeInMillis();
            DateHelper.getBirthDate(c.getTime());
            c.getTime();*/
        }
    };

    private void showCurrentUser() {
        if(mViewModel.getUser() == null){
            return;
        }

        if(!TextUtils.isEmpty(mViewModel.getUser().getName())){
            mBinding.nameValue.setText(mViewModel.getUser().getName());
        }

        //Set gender value
        if(TextUtils.equals(mViewModel.getUser().getGender(), USER_SPINNER_GENDER_MALE) ){
            mBinding.spinnerGenderValue.setSelection(0);
        }else{
            mBinding.spinnerGenderValue.setSelection(1);
        }

        //Set birthday value
        if(mViewModel.getUser().getBirthDate() != null){
            // To set the visible birthday string to the user
            String birthDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mViewModel.getUser().getBirthDate());
            mBinding.birthValue.setText(birthDate);
        }


    }

    private void saveProfile() {

        // check if name and avatar are not empty
        if (TextUtils.isEmpty(mBinding.nameValue.getText())) {
            Toast.makeText(getActivity(), R.string.empty_profile_name_error, Toast.LENGTH_LONG).show();
            return;
        }

        // set user name
        mViewModel.getUser().setName(String.valueOf(mBinding.nameValue.getText()).trim());

        if (mBinding.spinnerGenderValue.getSelectedItemPosition() == 0) {
            mViewModel.getUser().setGender(USER_SPINNER_GENDER_MALE);
        } else {
            mViewModel.getUser().setGender(USER_SPINNER_GENDER_FEMALE);
        }

        // Birthday is set when user finishes selecting a date

        // update the database
        mViewModel.insert(mViewModel.getUser());

        // Navigate to to main fragment
        navController.navigateUp();


    }

}