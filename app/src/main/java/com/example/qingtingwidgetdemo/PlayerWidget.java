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
public class PlayerWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Intent intent) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.player_widget);
        views.setOnClickPendingIntent(R.id.iv_previous, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_PREVIOUS_TASKBAR"), 0));
        views.setOnClickPendingIntent(R.id.iv_logo, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_TOGGLEPAUSE_TASKBAR"), 0));
        views.setOnClickPendingIntent(R.id.iv_next, PendingIntent.getBroadcast(context, 0, new Intent("com.tencent.qqmusicsdk.ACTION_SERVICE_NEXT_TASKBAR"), 0));
        //set music information
        if(intent != null){
            String song = intent.getStringExtra(MusicService.TITLE);
            String singer = intent.getStringExtra(MusicService.ARTIST);
            if(song == null){
                song = "未知歌曲";
            }
            if(singer == null){
                singer = "位置歌手";
            }
//            String album = intent.getStringExtra(MusicService.ALBUM);
            String lyric = intent.getStringExtra(MusicService.LYRIC);

            int songLength = song.length();
            String sep = " / ";
            int sepLength = sep.length();
            String songAndSinger = song + sep + singer;
            int songAndSingerLength = songAndSinger.length();
            SpannableString songAndSingerSpan = new SpannableString(songAndSinger);
            songAndSingerSpan.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.singer_size), false), songLength + sepLength, songAndSingerLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            views.setTextViewText(R.id.tv_title, songAndSingerSpan);
//            views.setTextViewText(R.id.tv_album, album);
            views.setTextViewText(R.id.tv_lyric, lyric);
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
            onUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PlayerWidget.class)), intent);
        } else {
            super.onReceive(context, intent);
        }
    }

}

