package com.example.myapplicationtp3;

import androidx.fragment.app.Fragment;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import android.os.Bundle;
import android.widget.Button;
import java.io.FileWriter;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

public class AffichageFragment extends Fragment {
    private TextView textViewNom, textViewPrenom, textViewDateNaissance, textViewTelephone, textViewEmail, textViewCentresInteret, textViewSynchronisation;
    private Button buttonValider;

    public AffichageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affichage, container, false);

        textViewNom = view.findViewById(R.id.textViewNom);
        textViewPrenom = view.findViewById(R.id.textViewPrenom);
        textViewDateNaissance = view.findViewById(R.id.textViewDateNaissance);
        textViewTelephone = view.findViewById(R.id.textViewTelephone);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewCentresInteret = view.findViewById(R.id.textViewInterets);
        textViewSynchronisation = view.findViewById(R.id.textViewSynchronisation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String nom = bundle.getString("nom");
            String prenom = bundle.getString("prenom");
            String dateNaissance = bundle.getString("dateNaissance");
            String telephone = bundle.getString("telephone");
            String email = bundle.getString("email");

            boolean sport = bundle.getBoolean("sport");
            boolean musique = bundle.getBoolean("musique");
            boolean lecture = bundle.getBoolean("lecture");

            boolean synchronisation = bundle.getBoolean("synchronisation");

            textViewNom.setText("Nom : " + nom);
            textViewPrenom.setText("Prénom : " + prenom);
            textViewDateNaissance.setText("Date de naissance : " + dateNaissance);
            textViewTelephone.setText("Numéro de téléphone : " + telephone);
            textViewEmail.setText("Adresse email : " + email);


            String centresInteret = "";
            if (sport) {
                centresInteret += "Sport, ";
            }
            if (musique) {
                centresInteret += "Musique, ";
            }
            if (lecture) {
                centresInteret += "Lecture, ";
            }
            if (centresInteret.length() > 0) {
                centresInteret = centresInteret.substring(0, centresInteret.length() - 2);
            }
            textViewCentresInteret.setText("Centres d'Interet : " + centresInteret);

            textViewSynchronisation.setText("Synchronisation : " + (synchronisation ? "Activé" : "Désactivé"));
        }


        buttonValider = view.findViewById(R.id.buttonValider);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer les données saisies
                String nom = textViewNom.getText().toString();
                String prenom = textViewPrenom.getText().toString();
                String dateNaissance = textViewDateNaissance.getText().toString();
                String telephone = textViewTelephone.getText().toString();
                String email = textViewEmail.getText().toString();
                String centresInteret = textViewCentresInteret.getText().toString();
                String synchronisation = textViewSynchronisation.getText().toString();

                // Enregistrer les données dans un fichier texte
                saveData(nom, prenom, dateNaissance, telephone, email, centresInteret, synchronisation, false);
                // Envoyer un message de confirmation à l'utilisateur
                Toast.makeText(getActivity(), "Données validées et enregistrées !", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void saveData(String nom, String prenom, String dateNaissance, String telephone, String email, String centresInteret, String synchronisation, boolean useJson) {
        if (useJson) {
            saveDataToJsonFile(nom, prenom, dateNaissance, telephone, email, centresInteret, synchronisation);
        } else {
            saveDataToTextFile(nom, prenom, dateNaissance, telephone, email, centresInteret, synchronisation);
        }
    }

   private void saveDataToTextFile(String nom, String prenom, String dateNaissance, String telephone, String email,  String centresInteret ,String synchronisation) {
        // Créer le contenu du fichier texte avec les données saisies
        StringBuilder builder = new StringBuilder();
        builder.append("Nom : ").append(nom).append("\n");
        builder.append("Prénom : ").append(prenom).append("\n");
        builder.append("Date de naissance : ").append(dateNaissance).append("\n");
        builder.append("Téléphone : ").append(telephone).append("\n");
        builder.append("Email : ").append(email).append("\n");
        builder.append("Centre d'Interet : ").append(centresInteret).append("\n");
        builder.append("Synchronisation : ").append(synchronisation).append("\n");

        // Écrire les données dans un fichier dans le dossier de l'application
        try {
            FileOutputStream outputStream = getActivity().openFileOutput("donnees.txt", Context.MODE_APPEND);
            outputStream.write(builder.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Erreur lors de l'enregistrement des données !", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataToJsonFile(String nom, String prenom, String dateNaissance, String telephone, String email, String centresInteret, String synchronisation) {
        try {
            // Créer un objet JSON pour stocker les données
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nom", nom);
            jsonObject.put("prenom", prenom);
            jsonObject.put("dateNaissance", dateNaissance);
            jsonObject.put("telephone", telephone);
            jsonObject.put("email", email);
            jsonObject.put("Centre d'Interet :", centresInteret);
            jsonObject.put("synchronisation", synchronisation);

            // Convertir l'objet JSON en une chaîne JSON
            String jsonData = jsonObject.toString();

            // Ouvrir un flux de sortie vers le fichier texte
            FileOutputStream outputStream = getActivity().openFileOutput("donnees.json", Context.MODE_PRIVATE);
            // Écrire les données JSON dans le fichier
            outputStream.write(jsonData.getBytes());
            outputStream.close();

            // Envoyer un message de confirmation à l'utilisateur
            Toast.makeText(getActivity(), "Données validées et enregistrées au format JSON !", Toast.LENGTH_SHORT).show();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Erreur lors de la sauvegarde des données au format JSON !", Toast.LENGTH_SHORT).show();
        }
    }








}

