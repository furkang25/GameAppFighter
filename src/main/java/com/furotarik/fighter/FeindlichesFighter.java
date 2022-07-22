package com.furotarik.fighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class FeindlichesFighter {

    Context context;
    Bitmap feindFighter;
    int ex, ey;
    int feindGeschwindigkeit;
    Random random;

    public FeindlichesFighter(Context context) {
        this.context = context;
        feindFighter = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket2);
        random = new Random();
        ex = 200 + random.nextInt(400);
        ey = 0;
        feindGeschwindigkeit = 14 + random.nextInt(10);
    }

    public Bitmap getEnemyFighter(){
        return feindFighter;
    }

    int getEnemyFighterWidth(){
        return feindFighter.getWidth();
    }

    int getEnemyFighterHeight(){
        return feindFighter.getHeight();
    }
}
