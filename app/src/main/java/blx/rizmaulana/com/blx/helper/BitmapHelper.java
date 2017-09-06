package blx.rizmaulana.com.blx.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Dibuat oleh Rizki Maulana pada 04/07/17.
 * rizmaulana@live.com
 * Mobile Apps DeveloperTimeZoneHelper.getWatermarkTimeStamp()+" | "+
 */

public class BitmapHelper {


    public static Bitmap compress(String pathfile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap image = BitmapFactory.decodeFile(pathfile, options);
        int dh = 480, dw = 640;
        if (image.getHeight() > image.getWidth()) {
            // Portrait
            float scale = 640f / image.getHeight();
            dh = 640;
            dw = Math.round(image.getWidth() * scale);
        } else {
            // Landscape
            float scale = 640f / image.getWidth();
            dw = 640;
            dh = Math.round(image.getHeight() * scale);
        }
        // RESIZE
        Log.i("Size", dw + " & " + dh);

        Bitmap images = Bitmap.createScaledBitmap(image, dw, dh, false);
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        return compressBitmap(images, bmpStream);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, ByteArrayOutputStream bmpStream)
    {

        int MAX_IMAGE_SIZE = 45 * 1024;
        int streamLength = MAX_IMAGE_SIZE;
        int decrementQuality = 4;
        int compressQuality = 80 + decrementQuality;

        while (streamLength >= MAX_IMAGE_SIZE && compressQuality > decrementQuality) {
            try {
                bmpStream.flush();//to avoid out of memory error
                bmpStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            compressQuality -= decrementQuality;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            streamLength = bmpStream.size();
            Log.d("fso", "Quality: " + compressQuality);
            Log.d("fso", "Size: " + streamLength);
        }
        return bitmap;
    }

}
