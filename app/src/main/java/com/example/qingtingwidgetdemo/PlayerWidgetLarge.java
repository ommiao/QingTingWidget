package com.example.qingtingwidgetdemo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PlayerWidgetLarge extends AppWidgetProvider {

    public static final String NO_PREVIOUS = "No previous lyric to show...";
    public static final String NO_LYRIC = "No lyric to show...";
    public static final String NO_NEXT = "No next lyric to show...";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, Intent intent) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.player_widget_large);
        views.setOnClickPendingIntent(R.id.iv_previous, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_PREVIOUS_TASKBAR"), 0));
        views.setOnClickPendingIntent(R.id.iv_logo, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_TOGGLEPAUSE_TASKBAR"), 0));
        views.setOnClickPendingIntent(R.id.iv_next, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_NEXT_TASKBAR"), 0));
        //set music information

        if(intent != null){

            if(intent.getBooleanExtra(MusicService.SONG_CHANGED, false)
                    || intent.getBooleanExtra(MusicService.FIRST_SEND, false)){
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(Util.getRoundBitmap() != null){
                views.setImageViewBitmap(R.id.iv_album, Util.getRoundBitmap());
            }

            String song = intent.getStringExtra(MusicService.TITLE);
            String singer = intent.getStringExtra(MusicService.ARTIST);
            String album = intent.getStringExtra(MusicService.ALBUM);
            if(song == null){
                song = "未知歌曲";
            }
            if(singer == null){
                singer = "未知歌手";
            }
            if(album == null){
                album = "未知专辑";
            }
            int singerLength = singer.length();
            String sep = " / ";
            int sepLength = sep.length();
            String singerAndAlbum = singer + sep + album;
            int singerAndAlbumLength = singerAndAlbum.length();
            SpannableString singerAndAlbumSpan = new SpannableString(singerAndAlbum);
            singerAndAlbumSpan.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.album_txt_size_small), false), singerLength + sepLength, singerAndAlbumLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            views.setTextViewText(R.id.tv_title, song);
            views.setTextViewText(R.id.tv_singer_album, singerAndAlbumSpan);

            String previousLyric = intent.getStringExtra(MusicService.PREVIOUS_LYRIC);
            String lyric = intent.getStringExtra(MusicService.LYRIC);
            String nextLyric = intent.getStringExtra(MusicService.NEXT_LYRIC);

            if(NO_LYRIC.equals(previousLyric)){
                views.setTextViewText(R.id.tv_lyric_previous, context.getString(R.string.no_lyric_placeholder));
            } else if(!NO_PREVIOUS.equals(previousLyric)){
                views.setTextViewText(R.id.tv_lyric_previous, previousLyric);
            } else {
                views.setTextViewText(R.id.tv_lyric_previous, "");
            }

            views.setTextViewText(R.id.tv_lyric_current, lyric);

            if(NO_LYRIC.equals(nextLyric)){
                views.setTextViewText(R.id.tv_lyric_next, context.getString(R.string.no_lyric_placeholder));
            } else if(!NO_NEXT.equals(nextLyric)){
                views.setTextViewText(R.id.tv_lyric_next, nextLyric);
            } else {
                views.setTextViewText(R.id.tv_lyric_next, "");
            }

        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        onUpdate(context, appWidgetManager, appWidgetIds, null);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Intent intent){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, intent);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(MusicService.OMMIAO.equals(intent.getStringExtra(MusicService.OMMIAO))){
            onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PlayerWidgetLarge.class)), intent);
        } else {
            super.onReceive(context, intent);
        }
    }
}

