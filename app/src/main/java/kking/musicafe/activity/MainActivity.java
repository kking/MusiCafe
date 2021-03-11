package kking.musicafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kking.musicafe.R;
import kking.musicafe.fragment.IntervalEarFragment;
import kking.musicafe.fragment.MainMenuFragment;

public class MainActivity extends AppCompatActivity {
    private static final int CONTAINER = R.id.mainFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar mActionBar = getSupportActionBar();
        BottomNavigationView mNavigationView = findViewById(R.id.bottomNavigationContainer);

        // set background for top ActionBar
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(getDrawable(R.drawable.background_gradient));

        // create listener for BottomNavigationView
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exercisesItem:
                        replaceFragment(IntervalEarFragment.newInstance(), "IntervalEarFragment");
                        return true;
                    case R.id.libraryItem:
                        //replaceFragment(Fragment.newInstance(), "");
                        return true;
                    case R.id.playItem:
                        //replaceFragment(Fragment.newInstance(), "");
                        return true;
                    case R.id.settingsItem:
                        //replaceFragment(Fragment.newInstance(), "");
                        return true;
                    default:
                        return false;
                }
            }
        });

        // populate LinearLayout container with main menu
        getSupportFragmentManager().beginTransaction()
                .replace(CONTAINER, MainMenuFragment.newInstance())
                .commit();
    }

    private void replaceFragment(Fragment fragment, String name) {
        getSupportFragmentManager().beginTransaction()
                .replace(CONTAINER, fragment)
                .addToBackStack(name)
                .commit();
    }

} // End of class MainActivity
