package com.example.myapplicationtp3;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer1, fragmentContainer2;
    private AffichageFragment affichageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentContainer1 = findViewById(R.id.fragment_container_1);
        fragmentContainer2 = findViewById(R.id.fragment_container_2);
        affichageFragment = new AffichageFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        SaisieFragment saisieFragment = new SaisieFragment();
        transaction.add(R.id.fragment_container_1, saisieFragment);

        transaction.commit();
    }


}