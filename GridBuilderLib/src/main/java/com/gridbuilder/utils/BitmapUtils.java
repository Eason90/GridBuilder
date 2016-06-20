package com.gridbuilder.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

public class BitmapUtils {

    private static final int PART_NUM = 4;

    /**
     * 将View转换成Bitmap
     */
    public static Bitmap convertViewToBitmap(View view, int outMargin, int baseHeight) {
        Bitmap bitmap;
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        if (baseHeight == 0) {
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            bitmap = view.getDrawingCache();
        } else {
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            view.draw(c);
        }

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        return Bitmap.createBitmap(bitmap
                , outMargin
                , height - height / PART_NUM - baseHeight + (baseHeight > 0 ? outMargin / PART_NUM : 0)
                , width - outMargin * 2
                , (height - outMargin * 2) / PART_NUM);
    }


    public static Bitmap draw(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        // 反转
        Matrix matrix = new Matrix();
        // 第一个参数为1表示x方向上以原比例为准保持不变，正数表示方向不变。
        // 第二个参数为-1表示y方向上以原比例为准保持不变，负数表示方向取反。
        matrix.preScale(1, -1);
        // reflectionImage就是下面透明的那部分,可以设置它的高度为原始的3/4,这样效果会更好些
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, false);
        Canvas canvasRef = new Canvas(reflectionImage);
        // 画被反转以后的图片
        canvasRef.drawBitmap(reflectionImage, 0, height, null);
        // 创建一个渐变的蒙版放在下面被反转的图片上面
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                0, 0,
                originalImage.getHeight(),
                0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // 将蒙板画上
        canvasRef.drawRect(0, 0, width, originalImage.getHeight(), paint);

        return reflectionImage;
    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, PorterDuff.Mode mode) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
