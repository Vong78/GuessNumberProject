package com.example.GuessNumberApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class LoginScreen extends AppCompatActivity {
    private Timer timer = null;
    private TimerTask task;
    private int a =  0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        timer = new Timer();
        task =new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        a++;
                        if(a==2){
                            enter();
                        }

                    }
                });

            }
        };
        timer.schedule(task,1000,1000);
    }

    private void enter(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}