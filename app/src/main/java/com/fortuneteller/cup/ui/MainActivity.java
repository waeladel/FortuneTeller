package com.fortuneteller.cup.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.fortuneteller.cup.R;
import com.fortuneteller.cup.databinding.ActivityMainBinding;
import com.fortuneteller.cup.databinding.ToolbarBinding;

//import com.fortuneteller.cup.databinding.ActivityainBinding;

public class MainActivity extends AppCompatActivity {
    private NavController navController ;
    private ActivityMainBinding mBinding;
    private ToolbarBinding mToolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mToolbarBinding = mBinding.toolbar;
        View view = mBinding.getRoot();
        setContentView(view);
        //setSupportActionBar(mBinding.toolbar);

        navController = Navigation.findNavController(this, R.id.host_fragment);

        // Setup toolbar
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mToolbarBinding.toolbar, navController);

        //goToMainFragment(); // No need it's already the home distination
    }

    private void goToMainFragment() {
        if (null != navController.getCurrentDestination() && R.id.mainFragment != navController.getCurrentDestination().getId()) {
            navController.navigate(R.id.mainFragment);
        }
    }
}