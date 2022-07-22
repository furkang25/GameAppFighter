package com.furotarik.fighter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartUp extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        mediaPlayer = MediaPlayer.create(StartUp.this, R.raw.mysong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void startGame(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
