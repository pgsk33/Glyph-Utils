import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import androidx.core.content.ContextCompat;
import com.nothing.ketchum.*;
import java.text.DecimalFormat;

public class GlyphUtils{
    //returns a GlyphMatrixFrame where the foreground is drawn from a resource
    public static GlyphMatrixFrame createFrameOutOfDrawable(Context context, String foregroundResName) {

        // Create the foreground object with the passed resource name
        GlyphMatrixObject foregroundObject = createObjectFromResourceName(context, foregroundResName);

        // If one of the objects could not be created, return null
        if (foregroundObject == null) {
            return null;
        }

        // Build the frame
        return new GlyphMatrixFrame.Builder()
                .addTop(foregroundObject)
                .build(context);
    }

    // returns Glyph Matrix Object from resource name
    private static GlyphMatrixObject createObjectFromResourceName(Context context, String resName) {
        if (resName == null || resName.isEmpty()) {
            return null;
        }

        int resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());

        // Error handling: If resource not found, return null
        if (resId == 0) {
            // Logging a warning makes sense here
            Log.w("FramesToAnimation", "Drawable resource not found: " + resName);
            return null;
        }

        Drawable drawable = ContextCompat.getDrawable(context, resId);
        if (drawable == null) {
            return null;
        }

        return new GlyphMatrixObject.Builder()
                .setImageSource(GlyphMatrixUtils.drawableToBitmap(drawable))
                .build();
    }

    //returns an array of GlyphMatrixFrames where the foreground is drawn from a resource. The amount of Frames is determined by numberOfFrames.
    //If your frames are named frame1, frame2, frame3... You have to use mode 1. If your frames are named frame0001, frame0002, frame0003... You have to use mode 2.
    public static GlyphMatrixFrame[] setAllAnimationFramesAsArray(Context context, String frameResName, int numberOfFrames, int mode) {
        GlyphMatrixFrame[] frames = new GlyphMatrixFrame[numberOfFrames];
        DecimalFormat df = new DecimalFormat("0000");
        if (mode == 1) {
            for (int i = 0; i < numberOfFrames; i++) {
                frames[i] = createFrameOutOfDrawable(context, frameResName + i + 1);
            }
        } else if (mode == 2) {
            for (int i = 0; i < numberOfFrames; i++) {
                frames[i] = createFrameOutOfDrawable(context, frameResName + df.format(i + 1));
            }
        }
        return frames;
    }
// Renders an image to the Glyph Matrix.
    public static void renderImageToGlyphMatrix(GlyphMatrixManager mGM, int drawableId, Context context) {
        Bitmap bitmap = drawableToBitmap(context, drawableId);
        GlyphMatrixObject startObject = new GlyphMatrixObject.Builder()
                .setImageSource(bitmap)
                .build();
        GlyphMatrixFrame frame = new GlyphMatrixFrame.Builder().addTop(startObject).build(context);
        renderFrameToGlyphMatrix(mGM, frame);
    }

//renders a Glyph Matrix Frame to the Glyph Matrix
    static void renderFrameToGlyphMatrix(GlyphMatrixManager mGM, GlyphMatrixFrame frame) {
        try {
            mGM.setMatrixFrame(frame.render());
        } catch (GlyphException e) {
            Log.e("FramesToAnimation", "Error displaying start image", e);
        }
    }

    
    public static Bitmap drawableToBitmap(Context context, int drawableId){
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        return GlyphMatrixUtils.drawableToBitmap(drawable);
    }



    //If your frames are named frame1, frame2, frame3... You have to use mode 1. If your frames are named frame0001, frame0002, frame0003... You have to use mode 2.
    //Animation intervall is in ms
    //THIS METHODE JUST PREPARES THE ANIMATION!!! YOU CAN TO RUN IT WITH ANIMATOR.START()
    public static void renderAnimation(GlyphMatrixManager mGM, String resNameWithoutNumber, int numberOfFrames, int mode, Context context, int ANIMATION_INTERVAL, ValueAnimator animation) {
        if (mGM == null) return;

        // Pre-load frames, just like before
        GlyphMatrixFrame[] frames = setAllAnimationFramesAsArray(context, resNameWithoutNumber, numberOfFrames, mode);

        // If there are no frames, don't start the animator
        if (frames == null || frames.length == 0) return;

        // 2. Set the total duration of the animation
        animation.setDuration((long) numberOfFrames * ANIMATION_INTERVAL);

        // 3. Use a LinearInterpolator to ensure frames change at a constant speed
        animation.setInterpolator(new LinearInterpolator());

        // 4. Listen for updates on each "tick" of the animation
        animation.addUpdateListener(updatedAnimation -> {
            int frameIndex = (Integer) updatedAnimation.getAnimatedValue();
            try {
                // Render the current frame
                mGM.setMatrixFrame(frames[frameIndex].render());
            } catch (GlyphException e) {
                Log.e("FramesToAnimation", "Error rendering animation frame.", e);
                // Stop the animation if an error occurs
                updatedAnimation.cancel();
            }
        });

    }
}
