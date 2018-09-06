package com.rezaduty.fun;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.rezaduty.mmmm.SmsListener;
import com.rezaduty.mmmm.SmsReceiver;
import com.rezaduty.mmmm.database.SqliteDatabase;

/**
 * Created by client on 8/27/17.
 */
public class AndroidServiceStartOnBoot extends Service {
    private SqliteDatabase mDatabase;
    private  MediaPlayer player;
    private  boolean ok;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // here you can add whatever you want this service to do
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText,String messageAdress) {
                Log.d("Text",messageText);
                Log.d("Address",messageAdress);
                if(messageAdress.startsWith("+")){
                    messageAdress = messageAdress.substring(3,messageAdress.length());
                    Log.d("Address",messageAdress);
                }

                ok = Boolean.valueOf(mDatabase.findPerson(messageText,messageAdress));

                if(ok){
                    try {
                        AudioManager am =
                                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                        am.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                0);
                        AssetFileDescriptor afd = getAssets().openFd("beep.mp3");
                        player = new MediaPlayer();
                        player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                        player.prepare();
                        player.start();


                    }catch (Exception e){

                    }


                }



            }
        });

    }

}