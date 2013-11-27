package com.nymbl.hatoa;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestCardMenuItem extends LinearLayout
                              implements View.OnClickListener {

    private CardMenuItem mCardMenuItem;
    public TestCardMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.card_menu_item_states);
        setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardMenuItem);
        final String text = a.getString(R.styleable.CardMenuItem_android_text);
        a.recycle();

        if (text.equals(null)) throw new RuntimeException("You must supply a string for the menu item to display");
        addView(inflateTextView(context, text));
    }

    public TestCardMenuItem(Context context, String text, CardMenuItem c) {
        super(context);
        setBackgroundResource(R.drawable.card_menu_item_states);
        setOnClickListener(this);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.dimen.menu_item_height);
        setLayoutParams(lp);

        if (text.equals(null)) throw new RuntimeException("You must supply a string for the menu item to display");
        addView(inflateTextView(context, text));

        mCardMenuItem = c;
    }

    public void setCardMenuItem(CardMenuItem c) {
        mCardMenuItem = c;
    }

    @Override
    public void onClick(View v) {
        if(mCardMenuItem != null) mCardMenuItem.OnClick(v, (Card) getParent());
    }

    private View inflateTextView(Context context, String text) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.card_menu_textview, this, false);
        textView.setText(text);
        return textView;
    }
}
