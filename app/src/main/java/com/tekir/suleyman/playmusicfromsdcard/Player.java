package com.tekir.suleyman.playmusicfromsdcard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> songs;
    SeekBar seekBar;
    int position;
    Button btPlay,btFB,btFF,btPV,btNXT;
    Uri u;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button) findViewById(R.id.btPlay);
        btFB = (Button) findViewById(R.id.btFB);
        btFF = (Button) findViewById(R.id.btFF);
        btPV = (Button) findViewById(R.id.btPV);
        btNXT = (Button) findViewById(R.id.btNXT);

        btPlay.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btPV.setOnClickListener(this);
        btNXT.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }catch (Exception e){

                    }
                }
            }
        };

        if(mp != null) {
            mp.stop();
            mp.release();
        }

        Intent ı = getIntent();
        Bundle b = ı.getExtras();
        songs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        u = Uri.parse(songs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        seekBar.setMax(mp.getDuration());
        updateSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()) {
                    //btPlay.setText(">");
                    mp.pause();
                }
                else {
                    //btPlay.setText("||");
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNXT:
                mp.stop();
                mp.release();
                position = (position+1)%songs.size();
                u = Uri.parse(songs.get( position ).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;
            case R.id.btPV:
                mp.stop();
                mp.release();
                position = (position-1<0) ? songs.size()-1 : position-1;
                u = Uri.parse(songs.get( position ).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                break;
        }
    }
}