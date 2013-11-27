package com.nymbl.hatoa;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.nymbl.hatoa.R;

/**
 * Created by shilpan on 11/9/13.
 */
public class Card extends LinearLayout {

    private int frontViewId;
    private int backViewId;
    private int menuViewId;

    private boolean canFlip;
    private boolean showingFront = true;
    public boolean mIsAnimationPlaying = false;

    private FlipAnimation flipAnimation;

    View frontView;
    View backView;

    private Animation mFlipCard;
    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Card);
        final boolean canSwipe = a.getBoolean(R.styleable.Card_swipe, false);
        canFlip = a.getBoolean(R.styleable.Card_flip, false);
        final boolean canDragDrop = a.getBoolean(R.styleable.Card_dragDrop, false);

        frontViewId = a.getResourceId(R.styleable.Card_frontView, -1);
        backViewId = a.getResourceId(R.styleable.Card_backView, -1);
        menuViewId = a.getResourceId(R.styleable.Card_menuView, -1);
        a.recycle();

        if (frontViewId == -1) throw new RuntimeException("You must supply the front layout of the Card");
        frontView = (View) LayoutInflater.from(context).inflate(frontViewId, this, false);
        addView(frontView);

        //mFlipCard = AnimationUtils.loadAnimation(context, R.anim.flip_card);
    }

    public void setFrontView(int id) {
        frontViewId = id;
        if (showingFront) {
            final View frontView = (View) LayoutInflater.from(getContext()).inflate(frontViewId, this, false);
            removeAllViews();
            addView(frontView);
        }
    }

    public void setBackView(int id) {
        backViewId = id;
        if (!showingFront) {
            final View backView = (View) LayoutInflater.from(getContext()).inflate(frontViewId, this, false);
            removeAllViews();
            addView(backView);
        }
    }

    public void flipTo(View view) {
        if (!canFlip) return;

        if (showingFront) {
            removeView(backView);
            backView = view;

        } else {
            removeView(frontView);
            frontView = view;
        }
        view.setVisibility(GONE);
        addView(view);
        flip();
    }

    public void flip() {

        if (backViewId != -1 && backView == null) {
            backView = (View) LayoutInflater.from(getContext()).inflate(backViewId, this, false);
            backView.setVisibility(GONE);
            addView(backView);
        }

        if (backView == null) return;

        flipAnimation = new FlipAnimation(frontView, backView, 250);
        flipAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimationPlaying = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimationPlaying = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mIsAnimationPlaying = true;
            }
        });
        if (!showingFront) flipAnimation.reverse();
        showingFront = !showingFront;
        this.startAnimation(flipAnimation);
    }

    public void cardSwitch() {
        if (backView == null) return;

        if (showingFront) {
            frontView.setVisibility(GONE);
            backView.setVisibility(VISIBLE);
        } else {
            frontView.setVisibility(VISIBLE);
            backView.setVisibility(GONE);
        }
        showingFront = !showingFront;
    }

    public boolean isShowingFront() {
        return showingFront;
    }

    public boolean isFlipping() {
        return mIsAnimationPlaying;
    }
}
