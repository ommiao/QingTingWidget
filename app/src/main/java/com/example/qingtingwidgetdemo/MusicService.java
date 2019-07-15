package com.example.qingtingwidgetdemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MusicService extends Service {

    public static final String ACTION_METACHANGED = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String OMMIAO = "ommiao";
    public static final String ID = "id";
    public static final String ARTIST = "artist";
    public static final String TITLE = "title";
    public static final String LYRIC = "lyric";
    public static final String NEXT_LYRIC = "nextLyric";
    public static final String PREVIOUS_LYRIC = "previousLyric";
    public static final String ALBUM = "album";
    public static final String TRACK = "track";
    public static final String LIST_SIZE = "ListSize";
    public static final String DURATION = "duration";
    public static final String POSITION = "position";
    public static final String SONG_CHANGED = "songChanged";

    private MusicMetaChangedReceiver musicMetaChangedReceiver;
    private Intent lastIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        musicMetaChangedReceiver = new MusicMetaChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        registerReceiver(musicMetaChangedReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicMetaChangedReceiver);
    }

    private void broadcastMusicChangedInfo(Intent originIntent) {
        Intent intent = new Intent(ACTION_METACHANGED);
        intent.putExtra(OMMIAO, OMMIAO);
        intent.putExtra(ID, originIntent.getLongExtra(ID, 0));
        intent.putExtra(TITLE, originIntent.getStringExtra(TITLE));
        intent.putExtra(LYRIC, originIntent.getStringExtra(LYRIC));
        intent.putExtra(NEXT_LYRIC, originIntent.getStringExtra(NEXT_LYRIC));
        intent.putExtra(PREVIOUS_LYRIC, originIntent.getStringExtra(PREVIOUS_LYRIC));
        intent.putExtra(ARTIST, originIntent.getStringExtra(ARTIST));
        intent.putExtra(ALBUM, originIntent.getStringExtra(ALBUM));
        intent.putExtra(TRACK, originIntent.getStringExtra(TRACK));
        intent.putExtra(LIST_SIZE, originIntent.getIntExtra(LIST_SIZE, 1));
        intent.putExtra(DURATION, originIntent.getLongExtra(DURATION, 0));
        intent.putExtra(POSITION, originIntent.getLongExtra(POSITION, 0));
        if(isIntentNoChanged(intent)){
            lastIntent = intent;
            return;
        }
        if(isSongChanged(intent)){
            intent.putExtra(SONG_CHANGED, true);
            intent.putExtra(PREVIOUS_LYRIC, getString(R.string.no_lyric_placeholder));
            intent.putExtra(NEXT_LYRIC, getString(R.string.no_lyric_placeholder));
        }
        sendBroadcast(intent);
        lastIntent = intent;
    }

    private boolean isIntentNoChanged(Intent intent) {
        if(lastIntent == null){
            return false;
        }
        return intent.getLongExtra(ID, 0) == lastIntent.getLongExtra(ID, 0)
                && intent.getStringExtra(TITLE).equals(lastIntent.getStringExtra(TITLE))
                && intent.getStringExtra(ARTIST).equals(lastIntent.getStringExtra(ARTIST))
                && intent.getStringExtra(PREVIOUS_LYRIC).equals(lastIntent.getStringExtra(PREVIOUS_LYRIC))
                && intent.getStringExtra(LYRIC).equals(lastIntent.getStringExtra(LYRIC))
                && intent.getStringExtra(NEXT_LYRIC).equals(lastIntent.getStringExtra(NEXT_LYRIC));
    }

    private boolean isSongChanged(Intent intent){
        if(lastIntent == null){
            return true;
        } else {
            return intent.getLongExtra(ID, 0) != lastIntent.getLongExtra(ID, 0);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    class MusicMetaChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastMusicChangedInfo(intent);
        }
    }
}
