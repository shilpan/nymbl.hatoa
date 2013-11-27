package com.nymbl.hatoa;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

/**
 * Created by mozilla on 11/10/13.
 */
public class CardView extends RelativeLayout {
    boolean showMenu = true;
    View menuList;
    public CardView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        final ViewGroup that = this;
        ImageButton button = new ImageButton(context, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.rightMargin = -25;
        button.setLayoutParams(lp);
        button.setFocusable(false);
        button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        button.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuList == null) {
                    menuList = LayoutInflater.from(context).inflate(R.layout.test_card_menu, (ViewGroup)that.findViewById(R.id.dataLayout), false);
                    ((LinearLayout)findViewById(R.id.dataLayout)).addView(menuList);
                }

                ExpandCollapseAnimation expand;
                if (showMenu) {
                    expand = new ExpandCollapseAnimation(menuList, 250, 0);
                } else {
                    expand = new ExpandCollapseAnimation(menuList, 250, 1);
                }
                showMenu = !showMenu;
                menuList.startAnimation(expand);
            }
        });
        addView(button);

    }
}
