package com.nymbl.hatoa;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nymbl.hatoa.dynamicgrid.BaseDynamicGridAdapter;
import com.nymbl.hatoa.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by shilpan on 11/9/13.
 */
public class CardsPanel extends DynamicGridView {

    private CardsPanel that;
    private class CardData {
        private boolean frontFacing = true;
        private int i;

        public CardData(int i) {
            this.i = i;
        }

        public boolean isCardFrontFacing() {
            return frontFacing;
        }

        public void setIsCardFrontFacing(boolean value) {
            frontFacing = value;
        }

        public int getInt() {
            return i;
        }
    }
    private ArrayList<CardData> data = new ArrayList<CardData>();
    public CardsPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        for (int i=0; i < 6; i++) {
            data.add(new CardData(i));
        }
        that = this;
        this.setWobbleInEditMode(false);
        this.setAdapter(new CardAdapter(context, data, 1));
        this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                that.startEditMode();
                return false;
            }
        });
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardData data = (CardData) parent.getAdapter().getItem(position);
                data.setIsCardFrontFacing(!data.isCardFrontFacing());

                ((Card)view).flip();
            }
        });
        this.setOnDropListener(new OnDropListener() {
            @Override
            public void onActionDrop() {
                that.stopEditMode();
            }
        });
    }



    private class CardAdapter extends BaseDynamicGridAdapter {
        private Context mContext;
        public CardAdapter(Context context, List<?> holder, int column) {
            super(context, holder, column);
            mContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                convertView = (Card) LayoutInflater.from(getContext()).inflate(R.layout.default_card, null, false);
            }

            convertView = CardViewBuilder(Integer.toString(((CardData) getItem(position)).getInt()), (Card) convertView, position);
            return convertView;
        }

        private View CardViewBuilder(String title, Card myCard, int position) {
            CardData d = (CardData) getItem(position);
            if (d.isCardFrontFacing() && !myCard.isShowingFront()) {
                myCard.cardSwitch();
            } else if(!d.isCardFrontFacing() && myCard.isShowingFront() && myCard.backView != null) {
                myCard.cardSwitch();
                ((TextView)myCard.frontView.findViewById(R.id.textView)).setText(title);
            }

            if(d.isCardFrontFacing()) ((TextView)myCard.frontView.findViewById(R.id.textView)).setText(title);
            return myCard;
        }
    }
}
