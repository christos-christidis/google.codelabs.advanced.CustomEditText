package com.advanced.customedittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

class EditTextWithClear extends android.support.v7.widget.AppCompatEditText {

    private Drawable mClearAllDrawable;

    public EditTextWithClear(Context context) {
        super(context);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearAllDrawable = ResourcesCompat.getDrawable(getResources(),
                R.drawable.ic_clear_semitransparent, null);

        // SOS: getX and getY are relative to the view!
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // SOS: checks if drawable exists at end of text...
                if ((getCompoundDrawablesRelative()[2] != null)) {
                    boolean drawableClicked = false;

                    if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
                        float drawableStart = getWidth() - getPaddingEnd() -
                                mClearAllDrawable.getIntrinsicWidth();

                        if (event.getX() > drawableStart) {
                            drawableClicked = true;
                        }
                    } else {    // RTL
                        float drawableEnd = getPaddingStart() + mClearAllDrawable.getIntrinsicWidth();

                        if (event.getX() < drawableEnd) {
                            drawableClicked = true;
                        }
                    }

                    if (drawableClicked) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // this just shows for a fraction of a second..
                            mClearAllDrawable = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_clear_black, null);
                            showClearAllDrawable();
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            Editable editable = getText();
                            if (editable != null) {
                                editable.clear();
                            }

                            mClearAllDrawable = ResourcesCompat.getDrawable(getResources(),
                                    R.drawable.ic_clear_semitransparent, null);
                            hideClearAllDrawable();
                            return true;
                        }
                    }
                }

                return false;
            }
        });

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // SOS: must change to String so that equals works properly...
                String str = s.toString();
                if (str.equals("")) {
                    hideClearAllDrawable();
                } else {
                    showClearAllDrawable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void showClearAllDrawable() {
        // SOS: this convoluted name means: put drawables in places inside the EditText
        setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, // Start of text.
                null, // Above text.
                mClearAllDrawable, // End of text.
                null); // Below text.
    }

    private void hideClearAllDrawable() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }
}
