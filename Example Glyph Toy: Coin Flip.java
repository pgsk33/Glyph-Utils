package com.example.yourapp;

import android.animation.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import androidx.annotation.NonNull;
import com.nothing.ketchum.*;

public class CoinFlipGT extends android.app.Service implements Animator.AnimatorListener {
    private GlyphMatrixManager mGM;
    Context context = this;
    private long ANIMATION_INTERVAL = 42;
    private int numberOfFrames = 70;

    ValueAnimator animator = ValueAnimator.ofInt(0, numberOfFrames - 1);

    private final GlyphMatrixManager.Callback mCallback = new GlyphMatrixManager.Callback() {
        @Override
        public void onServiceConnected(ComponentName componentName) {
            if (mGM != null) {
                mGM.register(Glyph.DEVICE_23112);
                FramesToAnimation.renderImageToGlyphMatrix(mGM, R.drawable.number, context);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        initAnimation();
        return serviceMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mGM != null) {
            mGM.unInit();
            mGM = null;
        }
        return super.onUnbind(intent);
    }

    private final Handler serviceHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GlyphToy.MSG_GLYPH_TOY) {
                Bundle bundle = msg.getData();
                String event = bundle.getString(GlyphToy.MSG_GLYPH_TOY_DATA);
                if (GlyphToy.EVENT_CHANGE.equals(event)) {
                    // Logik for the Long Press
                    animator.start();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    };
    private final Messenger serviceMessenger = new Messenger(serviceHandler);

    private void initAnimation() {
        mGM = GlyphMatrixManager.getInstance(this);
        mGM.init(mCallback);
        animator.addListener(this);
        FramesToAnimation.renderAnimation(mGM, "frame", 70, 2, context, (int) ANIMATION_INTERVAL, animator);
    }


    @Override
    public void onAnimationCancel(@NonNull Animator animator) {
        Log.d("CoinFlipGT", "Animation cancelled");

    }

    @Override
    public void onAnimationEnd(@NonNull Animator animator) {
        Log.d("CoinFlipGT", "Animation ended");
        if (Math.random()*2 > 1) {FramesToAnimation.renderImageToGlyphMatrix(mGM, R.drawable.number, context);}
        else {FramesToAnimation.renderImageToGlyphMatrix(mGM, R.drawable.head, context);}
    }

    @Override
    public void onAnimationRepeat(@NonNull Animator animator) {
        Log.d("CoinFlipGT", "Animation repeated");

    }

    @Override
    public void onAnimationStart(@NonNull Animator animator) {
        Log.d("CoinFlipGT", "Animation started");

    }
}
