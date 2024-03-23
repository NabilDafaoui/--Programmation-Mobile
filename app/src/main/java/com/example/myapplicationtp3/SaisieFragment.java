package com.example.myapplicationtp3;
import android.os.Environment;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import android.os.AsyncTask;
import java.net.URL;
import java.net.URLConnection;
import androidx.core.app.ActivityCompat;

public class SaisieFragment extends Fragment {
    private EditText editTextNom, editTextPrenom, editTextDateNaissance, editTextTelephone, editTextEmail;
    private CheckBox checkBoxSport, checkBoxMusique, checkBoxLecture;
    private Switch switchSynchronisation;
    private Button buttonSoumettre;
    private Button buttonTelecharger;

    public SaisieFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saisie, container, false);

        editTextNom = view.findViewById(R.id.editTextNom);
        editTextPrenom = view.findViewById(R.id.editTextPrenom);
        editTextDateNaissance = view.findViewById(R.id.editTextDateNaissance);
        editTextTelephone = view.findViewById(R.id.editTextTelephone);
        editTextEmail = view.findViewById(R.id.editTextEmail);

        checkBoxSport = view.findViewById(R.id.checkBoxSport);
        checkBoxMusique = view.findViewById(R.id.checkBoxMusique);
        checkBoxLecture = view.findViewById(R.id.checkBoxLecture);

        switchSynchronisation = view.findViewById(R.id.switchSynchronisation);

        buttonSoumettre = view.findViewById(R.id.buttonSoumettre);

        buttonSoumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les données saisies et les envoyer au fragment 2
                String nom = editTextNom.getText().toString();
                String prenom = editTextPrenom.getText().toString();
                String dateNaissance = editTextDateNaissance.getText().toString();
                String telephone = editTextTelephone.getText().toString();
                String email = editTextEmail.getText().toString();

                boolean sport = checkBoxSport.isChecked();
                boolean musique = checkBoxMusique.isChecked();
                boolean lecture = checkBoxLecture.isChecked();

                boolean synchronisation = switchSynchronisation.isChecked();

                Bundle bundle = new Bundle();
                bundle.putString("nom", nom);
                bundle.putString("prenom", prenom);
                bundle.putString("dateNaissance", dateNaissance);
                bundle.putString("telephone", telephone);
                bundle.putString("email", email);
                bundle.putBoolean("sport", sport);
                bundle.putBoolean("musique", musique);
                bundle.putBoolean("lecture", lecture);
                bundle.putBoolean("synchronisation", synchronisation);

                Fragment affichageFragment = new AffichageFragment();
                affichageFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_2, affichageFragment)
                        .commit();
                // Enregistrer les données saisies dans un fichier texte
                enregistrerDonnees(nom, prenom, dateNaissance, telephone, email, sport, musique, lecture, synchronisation);
            }
        });

        /*buttonTelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadFileFromURL().execute("https://samples.openweathermap.org/data/2.5/weather?q=London&appid=b6907d289e10d714a6e88b30761fae22");


            }
        });*/


        return view;
    }
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Afficher une barre de progression ou un message de chargement
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // Récupérer la taille du fichier
                int lengthOfFile = connection.getContentLength();
                // Télécharger le fichier
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/service.txt";
                OutputStream output = new FileOutputStream(filePath);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publier la progression du téléchargement
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // Mettre à jour la barre de progression ou un message de progression
        }

        @Override
        protected void onPostExecute(String file_url) {
            // Afficher un message de téléchargement terminé ou effectuer une action après le téléchargement
        }
    }
    private void enregistrerDonnees(String nom, String prenom, String dateNaissance, String telephone, String email, boolean sport, boolean musique, boolean lecture, boolean synchronisation) {
        // Créer le contenu du fichier texte avec les données saisies
        StringBuilder builder = new StringBuilder();
        builder.append("Nom : ").append(nom).append("\n");
        builder.append("Prénom : ").append(prenom).append("\n");
        builder.append("Date de naissance : ").append(dateNaissance).append("\n");
        builder.append("Téléphone : ").append(telephone).append("\n");
        builder.append("Email : ").append(email).append("\n");
        builder.append("Sport : ").append(sport).append("\n");
        builder.append("Musique : ").append(musique).append("\n");
        builder.append("Lecture : ").append(lecture).append("\n");
        builder.append("Synchronisation : ").append(synchronisation).append("\n");

        // Écrire les données dans un fichier dans le dossier de l'application
        try {
            FileOutputStream outputStream = getContext().openFileOutput("donnees.txt", Context.MODE_APPEND);
            outputStream.write(builder.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

