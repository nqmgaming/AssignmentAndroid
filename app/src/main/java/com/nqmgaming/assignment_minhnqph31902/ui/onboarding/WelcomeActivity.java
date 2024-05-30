package com.nqmgaming.assignment_minhnqph31902.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.nqmgaming.assignment_minhnqph31902.adapter.SlideAdapter;
import com.nqmgaming.assignment_minhnqph31902.preferences.UserPreferences;
import com.nqmgaming.assignment_minhnqph31902.R;
import com.nqmgaming.assignment_minhnqph31902.ui.MainActivity;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {

    //declare variables
    ViewPager viewPager;
    CircleIndicator dotsLayout;
    Button btnNext, btnSkip;
    SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check login
        UserPreferences userPreferences = new UserPreferences(this);

        //if login, go to MainActivity
        if (userPreferences.isLogin()) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);

        //mapping variables with view
        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.indicator);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        slideAdapter = new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);

        dotsLayout.setViewPager(viewPager);

        //set event click for buttons
        btnNext.setOnClickListener(v -> {

            //if click button next, go to next slide
            if (getItem(0) < slideAdapter.getCount() - 1) {
                viewPager.setCurrentItem(getItem(1), true);
            } else {

                //if click button next on last slide, go to GetStartActivity
                Intent intent = new Intent(WelcomeActivity.this, GetStartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSkip.setOnClickListener(v -> {

            //if click button skip, go to GetStartActivity
            Intent intent = new Intent(WelcomeActivity.this, GetStartActivity.class);
            startActivity(intent);
            finish();
        });

        viewPager.addOnPageChangeListener(viewListener);
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            //if slide is last slide, change text of button next to "Get Started"
            if (position >= slideAdapter.getCount() - 1) {
                btnNext.setText("Get Started");
            } else {

                //if slide is not last slide, change text of button next to "Next"
                btnNext.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    //get current item of viewpager
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}