package com.fortuneteller.cup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.fortuneteller.cup.R;

//import com.fortuneteller.cup.databinding.ActivityainBinding;

public class MainActivity extends AppCompatActivity {
    private NavController navController ;
    //private ActivityainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mBinding = ActivityainBinding.inflate(getLayoutInflater());
        //View view = mBinding.getRoot();
        //setContentView(view);

        navController = Navigation.findNavController(this, R.id.host_fragment);

        //goToMainFragment(); // No need it's already the home distination
    }

    private void goToMainFragment() {
        if (null != navController.getCurrentDestination() && R.id.mainFragment != navController.getCurrentDestination().getId()) {
            navController.navigate(R.id.mainFragment);
        }
    }
}