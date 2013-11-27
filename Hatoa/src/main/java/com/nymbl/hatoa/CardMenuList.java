package com.nymbl.hatoa;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mozilla on 11/10/13.
 */
public class CardMenuList extends LinearLayout {
    public CardMenuList(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= 11) {
            //setShowDividers(SHOW_DIVIDER_MIDDLE);
        }
    }

    public void addMenuItem(CardMenuItem item) {

    }
}
