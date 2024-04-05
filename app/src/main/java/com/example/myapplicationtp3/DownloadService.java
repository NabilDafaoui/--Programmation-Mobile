package com.example.myapplicationtp3;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String fileUrl = intent.getStringExtra("fileUrl");
        new DownloadTask().execute(fileUrl);
        return START_NOT_STICKY;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                Log.e("DownloadService", "Error downloading file: " + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String fileContents) {
            // Appeler une méthode pour parser le fichier téléchargé et récupérer ses données
            parseFile(fileContents);
        }
    }

    private void parseFile(String fileContents) {
        try {
            // Supposons que le contenu du fichier est au format JSON
            JSONObject jsonObject = new JSONObject(fileContents);

            // Extraire les données nécessaires du JSON
            String data1 = jsonObject.getString("data1");
            int data2 = jsonObject.getInt("data2");
            boolean data3 = jsonObject.getBoolean("data3");

            // Vous pouvez faire ce que vous voulez avec les données extraites
            // Par exemple, les afficher dans un Log pour le débogage
            Log.d("DownloadService", "Data 1: " + data1);
            Log.d("DownloadService", "Data 2: " + data2);
            Log.d("DownloadService", "Data 3: " + data3);

            // Ou les envoyer à une activité pour affichage
            Intent intent = new Intent("ACTION_DATA_RECEIVED");
            intent.putExtra("data1", data1);
            intent.putExtra("data2", data2);
            intent.putExtra("data3", data3);
            sendBroadcast(intent); // Envoyer un broadcast à l'activité pour lui transmettre les données

        } catch (JSONException e) {
            // Gérer les erreurs liées au parsing JSON
            Log.e("DownloadService", "Error parsing JSON: " + e.getMessage());
        }
    }
}

