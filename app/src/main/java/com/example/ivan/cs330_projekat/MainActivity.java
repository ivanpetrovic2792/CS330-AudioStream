package com.example.ivan.cs330_projekat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.media.AudioManager.STREAM_MUSIC;
import static android.media.AudioRecord.getMinBufferSize;
import static android.media.AudioTrack.MODE_STREAM;

public class MainActivity extends AppCompatActivity
{
    private static NotificationManagerCompat notificationManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        String nameFromIntent = getIntent().getStringExtra("EMAIL");
        textView.setText("Welcome " + nameFromIntent);
    }

    public void onClickListen(View view)
    {
        Thread t = new Thread(new Receiver());
        t.start();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "notify_001")
                .setSmallIcon(R.drawable.ikonica)
                .setContentTitle("Aplikacija je aktivna")
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentText("Zvuk se trenutno strimuje");
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(001, mBuilder.build());

    }

    public void stopStream(View view)
    {
        Receiver.stopLiveAudioStream();
        notificationManager.cancelAll();
    }
}