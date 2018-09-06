package com.rezaduty.mmmm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.rezaduty.mmmm.database.SqliteDatabase;

/**
 * Created by client on 8/27/17.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static SmsListener mListener;
    private SqliteDatabase mDatabase;
    public   static String messageBody;
    public   static String messageMessage;
    SmsMessage smsMessage;
    private  MediaPlayer player;
    private  boolean ok;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageAddress = smsMessage.getOriginatingAddress();

            String messageBody = smsMessage.getMessageBody();

            //Pass on the text to our listener.
            try {
                mListener.messageReceived(messageBody,messageAddress);
            }catch (Exception e){
                mDatabase = new SqliteDatabase(context);
                if(messageAddress.startsWith("+")){
                    messageAddress = messageAddress.substring(3,messageAddress.length());
                }
                Log.d("zxcxc",messageAddress);
                Log.d("zxcaaaaxc",messageBody);
                ok = Boolean.valueOf(mDatabase.findPerson(messageAddress,messageBody));

                Log.d("ok", String.valueOf(ok));
                if(ok){
                    try {
                        AudioManager am =
                                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                        am.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                0);
                        AssetFileDescriptor afd = context.getAssets().openFd("beep.mp3");
                        player = new MediaPlayer();
                        player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                        player.prepare();
                        player.start();
                        //Toast.makeText(MainActivity.this,messageAdress +" "+messageText,Toast.LENGTH_LONG).show();

                    }catch (Exception e1){

                    }


                }
                Log.d("Error",e.toString());
            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
