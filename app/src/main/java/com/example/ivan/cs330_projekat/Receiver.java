package com.example.ivan.cs330_projekat;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver implements Runnable
{
    private final String SERVER_ADDRESS     = "224.0.0.3";
    private final int    SERVER_PORT        = 48824;

    private final int    SAMPLE_RATE        = 32000;
    private final int    PACKET_SIZE        = 1200;
    private final int    CHANNEL_OUT_MONO   = 4;
    private final int    CHANNEL_OUT_STEREO = 12;

    private static boolean stopTrack = false;

    public static AudioTrack track;
    private static AudioManager am;

    private static InetAddress inetAddress;
    private static MulticastSocket multicastSocket;

    private final int CHANNELS = CHANNEL_OUT_MONO;

    @Override
    public void run() {

        //ESTABLISH CONNECTION
        try{
            inetAddress = InetAddress.getByName(SERVER_ADDRESS);

        }catch(Exception ex){
            Log.e("TCP","Error: server probably closed");
            ex.printStackTrace();
        }

        //am = (AudioManager)Context.getSystemService(Context.AUDIO_SERVICE);
        //int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        stopTrack = false;

        int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNELS, AudioFormat.ENCODING_PCM_16BIT);
        Log.e("AUDIO","minSize = " + minSize);

        track = new AudioTrack( AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                CHANNELS, AudioFormat.ENCODING_PCM_16BIT,
                minSize, AudioTrack.MODE_STREAM);
        track.play();

        try{

            multicastSocket = new MulticastSocket(SERVER_PORT);
            multicastSocket.joinGroup(inetAddress);
            //multicastSocket.setReuseAddress(true);
            //multicastSocket.setBroadcast(true);

            byte[] buffer = new byte[PACKET_SIZE];

            while(!stopTrack){

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);
                track.write(packet.getData(), 0, buffer.length);
                //Log.e("ERRO","SOCKET");
            }
            //multicastSocket.close();
        } catch (Exception e) {
            Log.e("UDP", "Receiver error", e);
        }
    }

    public static void stopLiveAudioStream(){
        track.stop();
        multicastSocket.close();
        stopTrack = true;
    }
}
