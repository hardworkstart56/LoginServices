package com.example.tatastrive.loginservices;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainServices extends Service {

    public MainServices() {     //It is a constructor.it allows background save file.
    }

    //Constant for file for current service
    final static int SAVE_TO_FILE = 1;
    //String which is going to be written in file for current Service
    String passedstring;

    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_TO_FILE:
                    Bundle bundle=(Bundle) msg.obj;
                passedstring=bundle.getString("msg");
                SaveInputToFile(passedstring);
                    Toast.makeText(getApplicationContext(),passedstring,Toast.LENGTH_SHORT).show();
                    break;

                default:
                    super.handleMessage(msg);

            }

        }
    }

    private void SaveInputToFile(String timeStampedInput) {
        String filename = "LoggingServiceLog.txt";
        File externalFile;

        externalFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), filename);
        try {
            FileOutputStream fOutput = new FileOutputStream(externalFile, true);
            OutputStreamWriter wOutput = new OutputStreamWriter(fOutput);
            wOutput.append(timeStampedInput + "\n");
            wOutput.flush();               // it used to clean the text
            wOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //Target we publish for clients to send messages to IncomingHAndler.
    final Messenger mmesege=new Messenger(new IncomingHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mmesege.getBinder();
    }
}
