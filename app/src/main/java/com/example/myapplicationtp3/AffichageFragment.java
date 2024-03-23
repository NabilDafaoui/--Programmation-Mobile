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
import java.io.IOException;
import java.io.File;
import android.os.Bundle;
import android.widget.Button;
import java.io.FileWriter;
import org.json.JSONObject;
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

                // Envoyer un message de confirmation à l'utilisateur
                Toast.makeText(getActivity(), "Données validées !", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void saveDataToJsonFile(JSONObject data) {
        // Créer un répertoire de stockage de fichiers
        File directory = new File(getActivity().getExternalFilesDir(null) + "/data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Créer un fichier JSON
        File file = new File(directory, "data.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(data.toString());
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(getActivity(), "Données enregistrées dans le fichier JSON !", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Erreur lors de l'enregistrement des données !", Toast.LENGTH_SHORT).show();
        }
    }


}

