package com.example.myapplicationtp3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer1, fragmentContainer2;
    private AffichageFragment affichageFragment;
    private String nomSaved, prenomSaved; // Variables pour stocker temporairement les données

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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

        // Vérifier s'il y a des données sauvegardées
        if (savedInstanceState != null) {
            // Récupérer les données sauvegardées
            nomSaved = savedInstanceState.getString("nomSaved");
            prenomSaved = savedInstanceState.getString("prenomSaved");
        }

        // Vérifier s'il y a des données déjà saisies à restaurer
        if (nomSaved != null && prenomSaved != null) {
            // Passer ces données au nouveau fragment1-Saisie
            Bundle bundle = new Bundle();
            bundle.putString("nom", nomSaved);
            bundle.putString("prenom", prenomSaved);
            saisieFragment.setArguments(bundle);
        }

        transaction.add(R.id.fragment_container_1, saisieFragment);
        transaction.commit();


        Button startDownloadButton = findViewById(R.id.startDownloadButton);
        startDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer le service pour télécharger le fichier
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra("fileUrl", "https://filesamples.com/formats/json?utm_content=cmp-true/ exemple1.json");
                startService(intent);
            }
        });

    }


    // Sauvegarder les données saisies lors du changement d'état de l'activité
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nomSaved", nomSaved);
        outState.putString("prenomSaved", prenomSaved);
    }

    // Intercepter l'événement de retour pour conserver les données saisies
    @Override
    public void onBackPressed() {
        // Récupérer les données saisies dans le fragment1-Saisie
        super.onBackPressed();
        SaisieFragment fragment = (SaisieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_1);
        if (fragment != null) {
            nomSaved = fragment.getNom();
            prenomSaved = fragment.getPrenom();
        }

        // Passer ces données au nouveau fragment1-Saisie
        Bundle bundle = new Bundle();
        bundle.putString("nom", nomSaved);
        bundle.putString("prenom", prenomSaved);

        SaisieFragment saisieFragment = new SaisieFragment();
        saisieFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_1, saisieFragment)
                .commit();
    }
}
