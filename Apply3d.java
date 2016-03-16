package com.jlu.voicehome;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by ErRa on 2016/3/3.
 */
public class Apply3d {
    private int mDuration = 500;
    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private float mDepthZ = 0.0f;
    private int mIndex = 0;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private ImageView mStartAnimView = null;
    private View mContainer = null;
    private boolean mIfOn;

    public void setmIfOn(boolean a)
    {
        mIfOn=a;
    }
    public boolean getmIfOn()
    {
        return mIfOn;
    }
    public Apply3d(View container,ImageView imageView1,ImageView imageView2,ImageView imageView3)
    {
        mIfOn=false;
        mStartAnimView=imageView1;
        mContainer=container;
        mCenterX = imageView1.getWidth() / 2;
        mCenterY = imageView1.getHeight() / 2;

        mDepthZ = 300;
        mImageView1=imageView1;
        mImageView2=imageView2;
        mImageView3=imageView3;

    }
    public void applyRotation(ImageView startImage, float startAngle, float toAngle)
    {
        mStartAnimView=startImage;
        mCenterX = mStartAnimView.getWidth()/2;
        mCenterY = mStartAnimView.getHeight()/2;
        Rotate3dAnimation rotation = new Rotate3dAnimation(
                startAngle, toAngle, mCenterX, mCenterY, mDepthZ, true);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());
        mStartAnimView.startAnimation(rotation);
    }
    public void DeeperImage(int level)//level 越高越亮 alpha数值越小
    {
        mImageView3.setVisibility(View.VISIBLE);
        switch (level)
        {
            case 0:
                mImageView3.setImageAlpha(200);
                break;
            case 1:
                mImageView3.setImageAlpha(180);
                break;
            case 2:
                mImageView3.setImageAlpha(160);
                break;
            case 3:
                mImageView3.setImageAlpha(140);
                break;
            case 4:
                mImageView3.setImageAlpha(120);
                break;
            case 5:
                mImageView3.setImageAlpha(100);
                break;
            case 6:
                mImageView3.setImageAlpha(80);
                break;
            case 7:
                mImageView3.setImageAlpha(60);
                break;
            case 8:
                mImageView3.setImageAlpha(40);
                break;
            case 9:
                mImageView3.setImageAlpha(20);
                break;
            case 10:
                mImageView3.setImageAlpha(0);
                break;
            default:
                break;
        }
    }
    private final class DisplayNextView implements Animation.AnimationListener {
        public void onAnimationStart(Animation animation) {
        }
        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews());
        }
        public void onAnimationRepeat(Animation animation) {
        }
    }
    private final class SwapViews implements Runnable
    {
        @Override
        public void run()
        {
            mImageView1.setVisibility(View.GONE);
            mImageView2.setVisibility(View.GONE);
            mIndex++;
            if (0 == mIndex % 2)
            {
                mStartAnimView = mImageView1;
            }
            else
            {
                mStartAnimView = mImageView2;
            }
            mStartAnimView.setVisibility(View.VISIBLE);
            mStartAnimView.requestFocus();
            //Log.v("haha",mStartAnimView.getWidth()/2 + "," + mStartAnimView.getHeight()/2);
            Rotate3dAnimation rotation = new Rotate3dAnimation(-90, 0,mCenterX, mCenterY, mDepthZ, false);
            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            mStartAnimView.startAnimation(rotation);
        }
    }
}
