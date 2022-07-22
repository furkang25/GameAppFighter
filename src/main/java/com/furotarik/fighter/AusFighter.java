package com.furotarik.fighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class AusFighter {

    Context context;
    Bitmap unserFighter;
    int ox, oy;
    Random random;

    public AusFighter(Context context) {
        this.context = context;
        unserFighter = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket1);
        random = new Random();
        ox = random.nextInt(Fighter.screenWidth);
        oy = Fighter.screenHeight - unserFighter.getHeight();
    }

    public Bitmap getOurFighter(){
        return unserFighter;
    }

    int getOurFighterWidth(){
        return unserFighter.getWidth();
    }
}
