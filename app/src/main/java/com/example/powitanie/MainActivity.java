package com.example.powitanie;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private static final String ID_KANALU = "kanal_powitalny";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        EditText editTextImie = findViewById(R.id.editTextImie);
        Button buttonPowitanie = findViewById(R.id.buttonPowitanie);


        buttonPowitanie.setOnClickListener(v -> {
            String imie = editTextImie.getText().toString().trim();
            if (imie.isEmpty()) {
                pokazAlertBrakuImienia();
            } else {
                pokazDialogPotwierdzenia(imie);
            }
        });
    }

    private void pokazAlertBrakuImienia() {
        new AlertDialog.Builder(this)
                .setTitle("Błąd")
                .setMessage("Proszę wpisać swoje imię!")
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }

    private void pokazDialogPotwierdzenia(String imie) {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie")
                .setMessage("Cześć " + imie + "! Czy chcesz otrzymać powiadomienie powitalne?")
                .setPositiveButton("Tak, poproszę", (dialog, which) -> {
                    wyslijPowiadomienie(imie);
                    Toast.makeText(MainActivity.this, "Powiadomienie zostało wysłane!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Nie, dziękuję", (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia.", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID_KANALU, "Kanal powitan", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void wyslijPowiadomienie(String imie) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_KANALU)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Witaj!")
                .setContentText("Miło Cię widzieć, " + imie + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
