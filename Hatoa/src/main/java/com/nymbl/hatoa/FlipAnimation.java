package com.nymbl.hatoa;

import android.app.Activity;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Shilpan on 11/9/13.
 */
public class FlipAnimation extends Animation {
    private Camera camera;

    private View fromView;
    private View toView;

    private int fromViewHeight;
    private int toViewHeight;

    private float centerX;
    private float centerY;

    private boolean forward = true;
    private boolean animationPlaying = false;

    /**
     * Creates a 3D flip animation between two views.
     *
     * @param fromView First view in the transition.
     * @param toView Second view in the transition.
     * @param duration Duration of the animation
     */
    public FlipAnimation(View fromView, View toView, int duration) {
        this.fromView = fromView;
        this.toView = toView;

        this.fromViewHeight = fromView.getLayoutParams().height;
        this.toViewHeight = toView.getLayoutParams().height;

        setDuration(duration);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse() {
        forward = false;
        View switchView = toView;
        toView = fromView;
        fromView = switchView;

        setHeightForWrapContent((Activity) fromView.getContext(), fromView);
        setHeightForWrapContent((Activity) toView.getContext(), toView);

        this.fromViewHeight = fromView.getLayoutParams().height;
        this.toViewHeight = toView.getLayoutParams().height;
    }

    public boolean isAnimationPlaying() {
        return animationPlaying;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width/2;
        centerY = height/2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Angle around the y-axis of the rotation at the given time
        // calculated both in radians and degrees.
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around
        if (interpolatedTime >= 0.5f)
        {
            degrees -= 180.f;
            fromView.setVisibility(View.GONE);
            toView.setVisibility(View.VISIBLE);
        }

        if (toView.getHeight() != 0) {
            if (interpolatedTime < 1.0f) {
                int diffHeight = toViewHeight - fromViewHeight;
                if (diffHeight != 0) {
                    toView.getLayoutParams().height = fromViewHeight + (int)(diffHeight * interpolatedTime);;
                    toView.requestLayout();
                }
            } else {
                toView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        if (forward)
            degrees = -degrees; //determines direction of rotation when flip begins

        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.translate(0, 0, Math.abs(degrees) * 2);
        camera.getMatrix(matrix);
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public static void setHeightForWrapContent(Activity activity, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;

        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY);

        view.measure(widthMeasureSpec, heightMeasureSpec);
        int height = view.getMeasuredHeight();
        view.getLayoutParams().height = height;
    }
}
