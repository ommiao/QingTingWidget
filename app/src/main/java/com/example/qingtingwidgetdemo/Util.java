package com.example.qingtingwidgetdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

import java.io.File;

public class Util {

    private static String albumPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.qingtingwidgetdemo/cache/album.png";

    public static Bitmap getAlbumBitmap(){
        File file = new File(albumPath);
        if(file.exists()){
            return BitmapFactory.decodeFile(albumPath);
        }
        return null;
    }

    public static Bitmap getRoundBitmap(){

        Bitmap bitmap = getAlbumBitmap();

        if(bitmap == null){
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
