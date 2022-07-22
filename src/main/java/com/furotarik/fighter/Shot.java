package com.furotarik.fighter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Shot {

    Bitmap schuss;
    Context context;
    int shx, shy;

    public Shot(Context context, int shx, int shy) {
        this.context = context;
        schuss = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.shot);
        this.shx = shx;
        this.shy = shy;
    }
    public Bitmap getShot(){
        return schuss;
    }
    public int getShotWidth() {
        return schuss.getWidth();
    }
    public int getShotHeight() {
        return schuss.getHeight();
    }
}
