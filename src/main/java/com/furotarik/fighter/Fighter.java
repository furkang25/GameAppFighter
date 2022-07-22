package com.furotarik.fighter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class Fighter extends View {

    Context context;
    Bitmap hintergrund, lebenBild;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int punkte = 0;
    int leben = 3;
    Paint scoreAusgabe;
    int TEXT_SIZE = 80;
    boolean angehalten = false;
    AusFighter ourSpaceship;
    FeindlichesFighter feindFighter;
    Random random;
    ArrayList<Shot> feindSchüsse, ausSchüsse;
    Explosion explosion;
    ArrayList<Explosion> explosions;
    boolean feindschussaktion = false;
    final Runnable runnable = new Runnable() {

        @Override
        public void run() {
           invalidate();
        }
    };


    public Fighter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        feindSchüsse = new ArrayList<>();
        ausSchüsse = new ArrayList<>();
        explosions = new ArrayList<>();
        ourSpaceship = new AusFighter(context);
        feindFighter = new FeindlichesFighter(context);
        handler = new Handler();
        hintergrund = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        lebenBild = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        scoreAusgabe = new Paint();
        scoreAusgabe.setColor(Color.RED);
        scoreAusgabe.setTextSize(TEXT_SIZE);
        scoreAusgabe.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Hintergrund, Punkte und Leben auf Leinwand zeichnen
        canvas.drawBitmap(hintergrund, 0, 0, null);
        canvas.drawText("Pt: " + punkte, 0, TEXT_SIZE, scoreAusgabe);

        for(int i=leben; i>=1; i--){
            canvas.drawBitmap(lebenBild, screenWidth - lebenBild.getWidth() * i, 0, null);
        }
        // Wenn das Leben 0 wird, stoppe das Spiel und starte die GameOver-Aktivität mit Punkten
        if(leben == 0){
            angehalten = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", punkte);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        // Bewege das feindliche Raumschiff
        feindFighter.ex += feindFighter.feindGeschwindigkeit;
        // Wenn das feindliche Raumschiff mit der rechten Wand kollidiert, kehre die feindliche Geschwindigkeit um
        if(feindFighter.ex + feindFighter.getEnemyFighterWidth() >= screenWidth){
            feindFighter.feindGeschwindigkeit *= -1;
        }

        // Wenn das feindliche Raumschiff mit der linken Wand kollidiert, wieder die feindliche Geschwindigkeit umkehren
        if(feindFighter.ex <=0){
            feindFighter.feindGeschwindigkeit *= -1;
        }

        // Bis die feindlicheShotAktion falsch ist, sollte der Feind Schüsse aus einer zufällig zurückgelegten Entfernung abfeuern
        if(feindschussaktion == false){
            if(feindFighter.ex >= 200 + random.nextInt(400)){
                Shot enemyShot = new Shot(context, feindFighter.ex + feindFighter.getEnemyFighterWidth() / 2, feindFighter.ey );
                feindSchüsse.add(enemyShot);
                // Wir setzen feindlicheShotAktion auf wahr, damit der Feind einen Kurzschluss nach dem anderen machen kann
                feindschussaktion = true;
            }

            if(feindFighter.ex >= 400 + random.nextInt(800)){
                Shot enemyShot = new Shot(context, feindFighter.ex + feindFighter.getEnemyFighterWidth() / 2, feindFighter.ey );
                feindSchüsse.add(enemyShot);
                // Wir setzen feindlicheShotAktion auf wahr, damit der Feind einen Kurzschluss nach dem anderen machen kann
                feindschussaktion = true;
            }
            else{
                Shot enemyShot = new Shot(context, feindFighter.ex + feindFighter.getEnemyFighterWidth() / 2, feindFighter.ey );
                feindSchüsse.add(enemyShot);
                // Wir setzen feindlicheShotAktion auf wahr, damit der Feind einen Kurzschluss nach dem anderen machen kann
                feindschussaktion = true;
            }
        }

        // Zeichne das feindliche Raumschiff
        canvas.drawBitmap(feindFighter.getEnemyFighter(), feindFighter.ex, feindFighter.ey, null);
        // Zeichne unser Raumschiff zwischen den linken und rechten Rand des Bildschirms
        if(ourSpaceship.ox > screenWidth - ourSpaceship.getOurFighterWidth()){
            ourSpaceship.ox = screenWidth - ourSpaceship.getOurFighterWidth();
        }else if(ourSpaceship.ox < 0){
            ourSpaceship.ox = 0;
        }

        // Zeichne unser Raumschiff
        canvas.drawBitmap(ourSpaceship.getOurFighter(), ourSpaceship.ox, ourSpaceship.oy, null);
        // Ziehen Sie den feindlichen Schuss nach unten auf unser Raumschiff und wenn er getroffen wird, verringern Sie das Leben, entfernen Sie ihn
        // das erschossene Objekt aus der ArrayList der FeindeSchüsse und zeige eine Explosion.
        // Sonst, wenn es durch den unteren Rand des Bildschirms verschwindet, auch entfernen
        // das geschossene Objekt von feindShots.
        // Wenn es auf dem Bildschirm keine feindlichen Schüsse gibt, ändere die feindliche Schussaktion auf falsch, damit der Feind
        // kann schießen.
        for(int i=0; i < feindSchüsse.size(); i++){
            feindSchüsse.get(i).shy += 15;
            canvas.drawBitmap(feindSchüsse.get(i).getShot(), feindSchüsse.get(i).shx, feindSchüsse.get(i).shy, null);
            if((feindSchüsse.get(i).shx >= ourSpaceship.ox)
                && feindSchüsse.get(i).shx <= ourSpaceship.ox + ourSpaceship.getOurFighterWidth()
                && feindSchüsse.get(i).shy >= ourSpaceship.oy
                && feindSchüsse.get(i).shy <= screenHeight){
                leben--;
                feindSchüsse.remove(i);
                explosion = new Explosion(context, ourSpaceship.ox, ourSpaceship.oy);
                explosions.add(explosion);
            }else if(feindSchüsse.get(i).shy >= screenHeight){
                feindSchüsse.remove(i);
            }
            if(feindSchüsse.size() < 1){
                feindschussaktion = false;
            }
        }

        // Richten Sie unsere Raumschiffschüsse auf den Feind. Wenn es zu einer Kollision zwischen unserem Schuss und dem Feind kommt
        // Raumschiff, Punkte erhöhen, Schuss aus ourShots entfernen und neues Explosionsobjekt erstellen.
        // Andernfalls, wenn unser Schuss durch den oberen Rand des Bildschirms geht, entfernen Sie ihn ebenfalls
        // das erschossene Objekt aus der ArrayList von finestShots.
        for(int i=0; i < ausSchüsse.size(); i++){
            ausSchüsse.get(i).shy -= 15;
            canvas.drawBitmap(ausSchüsse.get(i).getShot(), ausSchüsse.get(i).shx, ausSchüsse.get(i).shy, null);
            if((ausSchüsse.get(i).shx >= feindFighter.ex)
               && ausSchüsse.get(i).shx <= feindFighter.ex + feindFighter.getEnemyFighterWidth()
               && ausSchüsse.get(i).shy <= feindFighter.getEnemyFighterWidth()
               && ausSchüsse.get(i).shy >= feindFighter.ey){
                punkte++;
                ausSchüsse.remove(i);
                explosion = new Explosion(context, feindFighter.ex, feindFighter.ey);
                explosions.add(explosion);
            }else if(ausSchüsse.get(i).shy <=0){
                ausSchüsse.remove(i);
            }
        }

        // Mach die Explosion
        for(int i=0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionsrahmen), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionsrahmen++;
            if(explosions.get(i).explosionsrahmen > 8){
                explosions.remove(i);
            }
        }

        // Wenn nicht angehalten, rufen wir die Methode postDelayed() für das Handler-Objekt auf, wodurch die
        // Run-Methode innerhalb von Runnable, die nach 30 Millisekunden ausgeführt werden soll, das ist der Wert darin
        // UPDATE_MILLIS.
        if(!angehalten)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();

        // Wenn event.getAction() MotionEvent.ACTION_UP ist, wenn die Größe der ourShots-Arrayliste < 1 ist,
        // einen neuen Schuss erstellen.
        // Auf diese Weise beschränken wir uns darauf, jeweils nur eine Aufnahme auf dem Bildschirm zu machen.
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ausSchüsse.size() < 1){
                Shot ourShot = new Shot(context, ourSpaceship.ox + ourSpaceship.getOurFighterWidth() / 2, ourSpaceship.oy);
                ausSchüsse.add(ourShot);
            }
        }

        // Wenn event.getAction() MotionEvent.ACTION_DOWN ist, steuern Sie unser Raumschiff
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            ourSpaceship.ox = touchX;
        }

        // Wenn event.getAction() MotionEvent.ACTION_MOVE ist, steuern Sie unser Raumschiff
        // zusammen mit der Berührung.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            ourSpaceship.ox = touchX;
        }

        // Die Rückgabe von true in einem onTouchEvent() teilt dem Android-System mit, dass Sie es bereits bearbeitet haben
        // das Touch-Ereignis und es ist keine weitere Behandlung erforderlich.
        return true;
    }
}
