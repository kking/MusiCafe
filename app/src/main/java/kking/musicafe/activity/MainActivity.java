package kking.musicafe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kking.musicafe.R;
import kking.musicafe.fragment.IntervalEarFragment;
import kking.musicafe.fragment.MainMenuFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int CONTAINER = R.id.mainFrameLayout, MENU = R.id.bottomNavigationContainer;

    private FrameLayout mFrameLayout;
    private BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout = findViewById(CONTAINER);
        mNavigationView = findViewById(MENU);

        // create listener for BottomNavigationView
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exercisesItem:
                        replaceFragment(IntervalEarFragment.newInstance(), "IntervalEarFragment");
                        return true;
                    case R.id.libraryItem:
                        return true;
                    case R.id.playItem:
                        return true;
                    case R.id.settingsItem:
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
