package es.uva.inf.espectacle.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import es.uva.inf.espectacle.MainActivity;
import es.uva.inf.espectacle.R;
import es.uva.inf.espectacle.modelo.Audio;

/**
 * Implementacion del servicio de musica que reproduce audio en segundo plano
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener{

    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";


    private MediaPlayer player;
    private ArrayList<Audio> audios;
    private int songPos;
    private final Random r = new Random();
    private boolean foreground = false;
    private final IBinder musicBind = new MusicBinder();

    public MusicService() {
    }

    /**
     * Creación del servicio reproductor de musica
     */
    public void onCreate(){
        super.onCreate();
        songPos = 0;
        if(player==null) {
            player = new MediaPlayer();
            Log.d("MediaPlayer", "New player ");

        }
        initMediaPlayer();

        //create the service
    }

    /**
     * Establece la lista de reproduccion de audios
     * @param audios ArrayList de audios a reproducir
     */
    public void setList(ArrayList<Audio> audios){
        this.audios = audios;
    }

    /**
     * Inicia la ejecucion en segundo plano
     */
    private void startForefround(){
        if(!foreground){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.STARTED_FROM, MainActivity.SFROM_MUSIC_NOTIFICATION);
            notificationIntent.putExtras(bundle);
            //notificationIntent.putExtra(MainActivity.STARTED_FROM, MainActivity.SFROM_MUSIC_NOTIFICATION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AudioPlayerBroadcastReceiver broadcastReceiver = new AudioPlayerBroadcastReceiver();
            broadcastReceiver.setMusicService(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            // set the custom action
            intentFilter.addAction("es.uva.inf.espectacle.ACTION_PLAY");
            intentFilter.addAction("es.uva.inf.espectacle.ACTION_NEXT");
            intentFilter.addAction("es.uva.inf.espectacle.ACTION_PREV");
            // register the receiver
            registerReceiver(broadcastReceiver, intentFilter);


            Intent playIntent = new Intent("es.uva.inf.espectacle.ACTION_PLAY");
            PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 100, playIntent, 0);
            Intent backIntent = new Intent("es.uva.inf.espectacle.ACTION_PREV");
            PendingIntent pendingBackIntent = PendingIntent.getBroadcast(this, 100, backIntent, 0);
            Intent nextIntent = new Intent("es.uva.inf.espectacle.ACTION_NEXT");
            PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 100, nextIntent, 0);




            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logofinal)
                            .setContentTitle("Espectacle")
                            .setContentText("Playing...")
                            .addAction(R.drawable.ic_navigate_before, "", pendingBackIntent)
                            .addAction(R.drawable.ic_play_circle_outline, "", pendingPlayIntent)
                            .addAction(R.drawable.ic_navigate_next, "", pendingNextIntent)
                            .setContentIntent(contentIntent);
            Notification playing = mBuilder.build();
            startForeground(1, playing);
            foreground = true;
        }

    }

    /**
     * Detiene la reproduccion en segundo plano
     */
    private void stopForeground(){
        if(foreground){
            stopForeground(true);
            foreground = false;
        }

    }

    /**
     * Bind para el servicio de audio
     */
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    /**
     * Iniciamos el reproductor y establecemos los listener de los eventos
     */
    private void initMediaPlayer(){
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }




    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }



    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    /**
     * Reproduce pista de audio
     */
    private void playSong(){

        player.reset();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audios.get(songPos).getId());
        Log.d("Music Service","Song played: "+songPos);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    /**
     * Pausa la reproduccion de una pista de audio
     */
    public void pause(){
        if(player.isPlaying()){
            player.pause();
            stopForeground();
        }else{
            player.start();
            if(!player.isPlaying()) playSong();
            startForefround();
        }
    }

    /**
     * Pausamos el servicio
     */
    public void pauseNoForeground(){
        if(player.isPlaying()){
            player.pause();

        }else{
            player.start();
            if(!player.isPlaying()) playSong();

        }
    }


    public boolean isPlaying() {
        return player.isPlaying();
    }



    /**
     * Pasa a siguiente pista de audio
     */
    public void next() {
        setNextSongPos();
        playSong();
    }

    /**
     * Vuelve a la anterior pista de audio
     */
    public  void back(){
        setPrevSongPos();
        playSong();
    }

    /**
     * Reproduce pista aleatoria
     */
    public void shuffle(){
        setRandomSongPos();
        playSong();
    }

    /**
     * Reproduce la pista con posicion pos en la lista
     * @param pos posicion en la lista de la pista
     */
    public void playSongPos(int pos){
        setSongPos(pos);
        playSong();
        startForefround();
    }

    /**
     * Establece la posicion en la lista de la pista en reproduccion
     * @param pos posicionen la lista
     */
    private void setSongPos(int pos) {
        if(pos<audios.size()&&pos>=0){
            songPos=pos;
        }
    }

    /**
     * Establece la posicion de la siguiente pista de audio
     * @return Posicion
     */
    private int setNextSongPos(){
        songPos++;
        if(songPos>=audios.size()) songPos = 0;
        return songPos;
    }
    /**
     * Establece la posicion de la anterior pista de audio
     * @return Posicion
     */
    private int setPrevSongPos(){
        songPos--;
        if(songPos<0) songPos = audios.size()-1;
        return songPos;
    }
    /**
     * Establece la posicion de la pista de audio aleatoria
     * @return Posicion
     */
    private int setRandomSongPos(){
        songPos = r.nextInt(audios.size());
        return songPos;
    }


    /**
     * Establece la cancion a reproducir
     * @param songIndex Indice de la cancion
     */
    public void setSong(int songIndex){
        Log.d("Music Service", "Songindex: "+ songIndex);
        songPos = songIndex;
        Log.d("Music Service", "Songpos: "+ songPos);
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    /**
     * Devuelve el audio en reproduccion
     * @return Audio
     */
    public Audio getPlayingAudio(){
        return audios.get(songPos);
    }
}
