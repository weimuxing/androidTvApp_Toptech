/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.inputmethod.pinyin;

import com.android.inputmethod.pinyin.SoftKeyboard.KeyRow;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class used to show a soft keyboard.
 *
 * A soft keyboard view should not handle touch event itself, because we do bias
 * correction, need a global strategy to map an event into a proper view to
 * achieve better user experience.
 */
public class SoftKeyboardView extends View {
    /**
     * mstar
     * Define the key of keyboard:  UP
     *                 DOWN
     *                 LEFT
     *                 RIGHT
     */
    private static final int KEY_UP    = 0x00000121;
    private static final int KEY_DOWN  = 0x00000122;
    private static final int KEY_LEFT  = 0x00000123;
    private static final int KEY_RIGHT = 0x00000124;
    private Resources r;
    private static boolean isSkbOnFocus = true;
    private static boolean isCandOnShow = false;

    /**
     * The definition of the soft keyboard for the current this soft keyboard
     * view.
     */
    private SoftKeyboard mSoftKeyboard;
    private Drawable mActiveCellDrawable;
    private Drawable mActiveCellNoFocusDrawable;
    static final String TAG = "##SoftKeyboardView";

    /**
     * The popup balloon hint for key press/release.
     */
    private BalloonHint mBalloonPopup;

    /**
     * The on-key balloon hint for key press/release. If it is null, on-key
     * highlight will be drawn on th soft keyboard view directly.
     */
    private BalloonHint mBalloonOnKey;

    /** Used to play key sounds. */
    private SoundManager mSoundManager;

    /** The last key pressed. */
    private SoftKey mSoftKeyDown;

    /** Used to indicate whether the user is holding on a key. */
    private boolean mKeyPressed = false;

    /**
     * The location offset of the view to the keyboard container.
     */
    private int mOffsetToSkbContainer[] = new int[2];

    /**
     * The location of the desired hint view to the keyboard container.
     */
    private int mHintLocationToSkbContainer[] = new int[2];

    /**
     * Text size for normal key.
     */
    private int mNormalKeyTextSize;

    /**
     * Text size for function key.
     */
    private int mFunctionKeyTextSize;

    /**
     * Long press timer used to response long-press.
     */
    private SkbContainer.LongPressTimer mLongPressTimer;

    /**
     * Repeated events for long press
     */
    private boolean mRepeatForLongPress = false;

    /**
     * If this parameter is true, the balloon will never be dismissed even if
     * user moves a lot from the pressed point.
     */
    private boolean mMovingNeverHidePopupBalloon = false;

    /** Vibration for key press. */
    private Vibrator mVibrator;

    /** Vibration pattern for key press. */
    protected long[] mVibratePattern = new long[] {1, 20};

    /**
     * The dirty rectangle used to mark the area to re-draw during key press and
     * release. Currently, whenever we can invalidate(Rect), view will call
     * onDraw() and we MUST draw the whole view. This dirty information is for
     * future use.
     */
    private Rect mDirtyRect = new Rect();

    private Paint mPaint;
    private FontMetricsInt mFmi;
    private boolean mDimSkb;
	// add
	private Context mContext;
	// end add

    public SoftKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "SoftKeyboardView");
		// add
		mContext = context;
		//end add
        mSoundManager = SoundManager.getInstance(mContext);

        isSkbOnFocus = true;
        r = context.getResources();
        mActiveCellDrawable = r.getDrawable(R.drawable.softkey_bg_select);
        mActiveCellNoFocusDrawable =r.getDrawable(R.drawable.selected_pannel);
        //mSoftKeyboard.getKeyRowForDisplay(0).mSoftKeys.get(0).setKeySelected(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mFmi = mPaint.getFontMetricsInt();
    }

    public boolean setSoftKeyboard(SoftKeyboard softSkb) {
        if (null == softSkb) {
            return false;
        }
        mSoftKeyboard = softSkb;
        Drawable bg = softSkb.getSkbBackground();

        //mSoftKeyboard.getKeyRowForDisplay(0).mSoftKeys.get(0).setKeySelected(true);
        mSoftKeyboard.setOneKeySelected(0,0);

        if (null != bg) setBackgroundDrawable(bg);
        return true;
    }

    public SoftKeyboard getSoftKeyboard() {
        return mSoftKeyboard;
    }

    public void resizeKeyboard(int skbWidth, int skbHeight) {
        mSoftKeyboard.setSkbCoreSize(skbWidth, skbHeight);
    }

    public void setBalloonHint(BalloonHint balloonOnKey,
            BalloonHint balloonPopup, boolean movingNeverHidePopup) {
        mBalloonOnKey = balloonOnKey;
        mBalloonPopup = balloonPopup;
        mMovingNeverHidePopupBalloon = movingNeverHidePopup;
    }

    public void setOffsetToSkbContainer(int offsetToSkbContainer[]) {
        mOffsetToSkbContainer[0] = offsetToSkbContainer[0];
        mOffsetToSkbContainer[1] = offsetToSkbContainer[1];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = 0;
        int measuredHeight = 0;
        if (null != mSoftKeyboard) {
            measuredWidth = mSoftKeyboard.getSkbCoreWidth();
            measuredHeight = mSoftKeyboard.getSkbCoreHeight();
            measuredWidth += mPaddingLeft + mPaddingRight;
            measuredHeight += mPaddingTop + mPaddingBottom;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private void showBalloon(BalloonHint balloon, int balloonLocationToSkb[],
            boolean movePress) {
        long delay = BalloonHint.TIME_DELAY_SHOW;
        if (movePress) delay = 0;
        if (balloon.needForceDismiss()) {
            balloon.delayedDismiss(0);
        }
        if (!balloon.isShowing()) {
            balloon.delayedShow(delay, balloonLocationToSkb);
        } else {
            balloon.delayedUpdate(delay, balloonLocationToSkb, balloon
                    .getWidth(), balloon.getHeight());
        }
        long b = System.currentTimeMillis();
    }

    public void resetKeyPress(long balloonDelay) {
        if (!mKeyPressed) return;
        mKeyPressed = false;
        if (null != mBalloonOnKey) {
            mBalloonOnKey.delayedDismiss(balloonDelay);
        } else {
            if (null != mSoftKeyDown) {
                if (mDirtyRect.isEmpty()) {
                    mDirtyRect.set(mSoftKeyDown.mLeft, mSoftKeyDown.mTop,
                            mSoftKeyDown.mRight, mSoftKeyDown.mBottom);
                }
                invalidate(mDirtyRect);
            } else {
                invalidate();
            }
        }
        mBalloonPopup.delayedDismiss(balloonDelay);
    }

    // If movePress is true, means that this function is called because user
    // moves his finger to this button. If movePress is false, means that this
    // function is called when user just presses this key.
    public SoftKey onKeyPress(int x, int y,
            SkbContainer.LongPressTimer longPressTimer, boolean movePress) {
        mKeyPressed = false;
        boolean moveWithinPreviousKey = false;
        if (movePress) {
            SoftKey newKey = mSoftKeyboard.mapToKey(x, y);
            if (newKey == mSoftKeyDown) moveWithinPreviousKey = true;
            mSoftKeyDown = newKey;
        } else {
            mSoftKeyDown = mSoftKeyboard.mapToKey(x, y);
        }
        if (moveWithinPreviousKey || null == mSoftKeyDown) return mSoftKeyDown;
        mKeyPressed = true;

        if (!movePress) {
            tryPlayKeyDown();
            tryVibrate();
        }

        mLongPressTimer = longPressTimer;

        if (!movePress) {
            if (mSoftKeyDown.getPopupResId() > 0 || mSoftKeyDown.repeatable()) {
                mLongPressTimer.startTimer();
            }
        } else {
            mLongPressTimer.removeTimer();
        }

        int desired_width;
        int desired_height;
        float textSize;
        Environment env = Environment.getInstance();

        if (null != mBalloonOnKey) {
            Drawable keyHlBg = mSoftKeyDown.getKeyHlBg();
            mBalloonOnKey.setBalloonBackground(keyHlBg);

            // Prepare the on-key balloon
            int keyXMargin = mSoftKeyboard.getKeyXMargin();
            int keyYMargin = mSoftKeyboard.getKeyYMargin();
            desired_width = mSoftKeyDown.width() - 2 * keyXMargin;
            desired_height = mSoftKeyDown.height() - 2 * keyYMargin;
            textSize = env
                    .getKeyTextSize(SoftKeyType.KEYTYPE_ID_NORMAL_KEY != mSoftKeyDown.mKeyType.mKeyTypeId);
            Drawable icon = mSoftKeyDown.getKeyIcon();
            if (null != icon) {
                mBalloonOnKey.setBalloonConfig(icon, desired_width,
                        desired_height);
            } else {
                mBalloonOnKey.setBalloonConfig(mSoftKeyDown.getKeyLabel(),
                        textSize, true, mSoftKeyDown.getColorHl(),
                        desired_width, desired_height);
            }

            mHintLocationToSkbContainer[0] = mPaddingLeft + mSoftKeyDown.mLeft
                    - (mBalloonOnKey.getWidth() - mSoftKeyDown.width()) / 2;
            mHintLocationToSkbContainer[0] += mOffsetToSkbContainer[0];
            mHintLocationToSkbContainer[1] = mPaddingTop
                    + (mSoftKeyDown.mBottom - keyYMargin)
                    - mBalloonOnKey.getHeight();
            mHintLocationToSkbContainer[1] += mOffsetToSkbContainer[1];
            showBalloon(mBalloonOnKey, mHintLocationToSkbContainer, movePress);
        } else {
            mDirtyRect.union(mSoftKeyDown.mLeft, mSoftKeyDown.mTop,
                    mSoftKeyDown.mRight, mSoftKeyDown.mBottom);
            invalidate(mDirtyRect);
        }

        // Prepare the popup balloon
        if (mSoftKeyDown.needBalloon()) {
            Drawable balloonBg = mSoftKeyboard.getBalloonBackground();
            mBalloonPopup.setBalloonBackground(balloonBg);

            desired_width = mSoftKeyDown.width() + env.getKeyBalloonWidthPlus();
            desired_height = mSoftKeyDown.height()
                    + env.getKeyBalloonHeightPlus();
            textSize = env
                    .getBalloonTextSize(SoftKeyType.KEYTYPE_ID_NORMAL_KEY != mSoftKeyDown.mKeyType.mKeyTypeId);
            Drawable iconPopup = mSoftKeyDown.getKeyIconPopup();
            if (null != iconPopup) {
                mBalloonPopup.setBalloonConfig(iconPopup, desired_width,
                        desired_height);
            } else {
                mBalloonPopup.setBalloonConfig(mSoftKeyDown.getKeyLabel(),
                        textSize, mSoftKeyDown.needBalloon(), mSoftKeyDown
                                .getColorBalloon(), desired_width,
                        desired_height);
            }

            // The position to show.
            mHintLocationToSkbContainer[0] = mPaddingLeft + mSoftKeyDown.mLeft
                    + -(mBalloonPopup.getWidth() - mSoftKeyDown.width()) / 2;
            mHintLocationToSkbContainer[0] += mOffsetToSkbContainer[0];
            mHintLocationToSkbContainer[1] = mPaddingTop + mSoftKeyDown.mTop
                    - mBalloonPopup.getHeight();
            mHintLocationToSkbContainer[1] += mOffsetToSkbContainer[1];
            showBalloon(mBalloonPopup, mHintLocationToSkbContainer, movePress);
        } else {
            mBalloonPopup.delayedDismiss(0);
        }

        if (mRepeatForLongPress) longPressTimer.startTimer();
        return mSoftKeyDown;
    }

    public SoftKey onKeyRelease(int x, int y) {
        mKeyPressed = false;
        if (null == mSoftKeyDown) return null;

        mLongPressTimer.removeTimer();

        if (null != mBalloonOnKey) {
            mBalloonOnKey.delayedDismiss(BalloonHint.TIME_DELAY_DISMISS);
        } else {
            mDirtyRect.union(mSoftKeyDown.mLeft, mSoftKeyDown.mTop,
                    mSoftKeyDown.mRight, mSoftKeyDown.mBottom);
            invalidate(mDirtyRect);
        }

        if (mSoftKeyDown.needBalloon()) {
            mBalloonPopup.delayedDismiss(BalloonHint.TIME_DELAY_DISMISS);
        }

        if (mSoftKeyDown.moveWithinKey(x - mPaddingLeft, y - mPaddingTop)) {
            return mSoftKeyDown;
        }
        return null;
    }

    public SoftKey onKeyMove(int x, int y) {
        if (null == mSoftKeyDown) return null;

        if (mSoftKeyDown.moveWithinKey(x - mPaddingLeft, y - mPaddingTop)) {
            return mSoftKeyDown;
        }

        // The current key needs to be updated.
        mDirtyRect.union(mSoftKeyDown.mLeft, mSoftKeyDown.mTop,
                mSoftKeyDown.mRight, mSoftKeyDown.mBottom);

        if (mRepeatForLongPress) {
            if (mMovingNeverHidePopupBalloon) {
                return onKeyPress(x, y, mLongPressTimer, true);
            }

            if (null != mBalloonOnKey) {
                mBalloonOnKey.delayedDismiss(0);
            } else {
                invalidate(mDirtyRect);
            }

            if (mSoftKeyDown.needBalloon()) {
                mBalloonPopup.delayedDismiss(0);
            }

            if (null != mLongPressTimer) {
                mLongPressTimer.removeTimer();
            }
            return onKeyPress(x, y, mLongPressTimer, true);
        } else {
            // When user moves between keys, repeated response is disabled.
            return onKeyPress(x, y, mLongPressTimer, true);
        }
    }

    private void tryVibrate() {
        if (!Settings.getVibrate()) {
            return;
        }
        if (mVibrator == null) {
            //mVibrator = new Vibrator();
			mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(mVibratePattern, -1);
    }

    private void tryPlayKeyDown() {
        if (Settings.getKeySound()) {
            mSoundManager.playKeyDown();
        }
    }

    public void dimSoftKeyboard(boolean dimSkb) {
        mDimSkb = dimSkb;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == mSoftKeyboard) return;

        canvas.translate(mPaddingLeft, mPaddingTop);

        Environment env = Environment.getInstance();
        mNormalKeyTextSize = env.getKeyTextSize(false);
        mFunctionKeyTextSize = env.getKeyTextSize(true);
        // Draw the last soft keyboard
        int rowNum = mSoftKeyboard.getRowNum();
        int keyXMargin = mSoftKeyboard.getKeyXMargin();
        int keyYMargin = mSoftKeyboard.getKeyYMargin();
        for (int row = 0; row < rowNum; row++) {
            KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(row);
            if (null == keyRow) continue;
            List<SoftKey> softKeys = keyRow.mSoftKeys;
            int keyNum = softKeys.size();
            for (int i = 0; i < keyNum; i++) {
                SoftKey softKey = softKeys.get(i);
                if (SoftKeyType.KEYTYPE_ID_NORMAL_KEY == softKey.mKeyType.mKeyTypeId) {
                    mPaint.setTextSize(mNormalKeyTextSize);
                } else {
                    mPaint.setTextSize(mFunctionKeyTextSize);
                }
                drawSoftKey(canvas, softKey, keyXMargin, keyYMargin);
            }
        }

        if (mDimSkb) {
            mPaint.setColor(0xa0000000);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }

        mDirtyRect.setEmpty();
    }

    private void drawSoftKey(Canvas canvas, SoftKey softKey, int keyXMargin,
            int keyYMargin) {
        Drawable bg;
        int textColor;
        if (mKeyPressed && softKey == mSoftKeyDown) {
            bg = softKey.getKeyHlBg();
            textColor = Color.BLACK;
        } else {
            bg = softKey.getKeyBg();
            bg = r.getDrawable(R.drawable.softkey_bg_unselect);
            textColor = Color.BLACK;
        }

        if (null != bg) {
            if (softKey.isKeySelected()) {
                if (isSkbOnFocus) {
                    mActiveCellDrawable.setBounds(softKey.mLeft + keyXMargin+1, softKey.mTop + keyYMargin+1,
                                                  softKey.mRight - keyXMargin-1, softKey.mBottom - keyYMargin-1);
                    mActiveCellDrawable.draw(canvas);
                } else {
                    mActiveCellNoFocusDrawable.setBounds(softKey.mLeft + keyXMargin+1, softKey.mTop + keyYMargin+1,
                                                         softKey.mRight - keyXMargin-1, softKey.mBottom - keyYMargin-1);
                    mActiveCellNoFocusDrawable.draw(canvas);
                }
            } else {
                bg.setBounds(softKey.mLeft + keyXMargin+1, softKey.mTop + keyYMargin+1,
                             softKey.mRight - keyXMargin-1, softKey.mBottom - keyYMargin-1);
                bg.draw(canvas);
            }
        }

        String keyLabel = softKey.getKeyLabel();
        Drawable keyIcon = softKey.getKeyIcon();
        if (null != keyIcon) {
            Drawable icon = keyIcon;
            int marginLeft = (softKey.width() - icon.getIntrinsicWidth()) / 2;
            int marginRight = softKey.width() - icon.getIntrinsicWidth()
                    - marginLeft;
            int marginTop = (softKey.height() - icon.getIntrinsicHeight()) / 2;
            int marginBottom = softKey.height() - icon.getIntrinsicHeight()
                    - marginTop;
            icon.setBounds(softKey.mLeft + marginLeft,
                    softKey.mTop + marginTop, softKey.mRight - marginRight,
                    softKey.mBottom - marginBottom);
            icon.draw(canvas);
        } else if (null != keyLabel) {
            mPaint.setColor(textColor);
            float x = softKey.mLeft
                    + (softKey.width() - mPaint.measureText(keyLabel)) / 2.0f;
            int fontHeight = mFmi.bottom - mFmi.top;
            float marginY = (softKey.height() - fontHeight) / 2.0f;
            float y = softKey.mTop + marginY - mFmi.top + mFmi.bottom / 1.5f;
            canvas.drawText(keyLabel, x, y + 1, mPaint);
        }
    }

    /**************************************************************
     * get the current selected key.
     * @author mstar
     * @param  null
     * @return SoftKey null
     *             No key is selected.
     *                 Softkey
     *                     The key is selected.
     ***************************************************************/
    public SoftKey getCurrentSelectKey() {
        //Log.d(TAG, "Get current selected softkey!");
        if (mSoftKeyboard == null) return null;

        int rowNum = mSoftKeyboard.getRowNum();
        for (int row = 0; row < rowNum; row++) {
            KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(row);
            if (null == keyRow)
                continue;
            List<SoftKey> softKeys = keyRow.mSoftKeys;
            int keyNum = softKeys.size();
            //Log.d(TAG, "onDraw---->(row,index):(" + row + "," + keyNum + ")");
            for (int i = 0; i < keyNum; i++) {
                SoftKey softKey = softKeys.get(i);
                if (softKey.isKeySelected()) {
                    return softKey ;
                }
            }
        }
        return null;
    }

    /**************************************************************
     * get the current selected key's index int the row.
     * @author mstar
     * @param  null
     * @return int
     *          the row number.
     ***************************************************************/
    public int getIndexCurrentSelectedKey() {
        //mRows = mSoftKeyboard.getRowNum();
        if (mSoftKeyboard == null) {
            return -1;
        }

        SoftKey softKey = getCurrentSelectKey();
        if (softKey == null) {
            return -1;
        }

        int rowNum = mSoftKeyboard.getRowNum();
        for (int row = 0; row < rowNum; row++) {
            KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(row);
            if (null == keyRow)
                continue;
            List<SoftKey> softKeys = keyRow.mSoftKeys;
            int keyNum = softKeys.size();
            //Log.d(TAG, "onDraw---->(row,index):(" + row + "," + keyNum + ")");
            for (int i = 0; i < keyNum; i++) {
                SoftKey tempsoftKey = softKeys.get(i);
                if (tempsoftKey == softKey) {
                    return i ;
                }
            }
        }
        return -1;
    }

    /**************************************************************
     * get the current selected key row number.
     * @author mstar
     * @param  null
     * @return int
     *         the row number of the selected key.
     ***************************************************************/
    public int getRowCurrentSelectedKey() {
        //Log.d(TAG, "getRowCurrentSelectedKey");
        if (mSoftKeyboard == null) {
            Log.d(TAG, "1111111");
            return -1;
        }

        SoftKey softKey = getCurrentSelectKey();
        if (softKey == null) {
            Log.d(TAG, "222222");
            return -1;
        }

        int rowNum = mSoftKeyboard.getRowNum();
        for (int row = 0; row < rowNum; row++) {
            KeyRow keyRow = mSoftKeyboard.getKeyRowForDisplay(row);
            if (null == keyRow)
                continue;
            List<SoftKey> softKeys = keyRow.mSoftKeys;
            int keyNum = softKeys.size();
            //Log.d(TAG, "onDraw---->(row,index):(" + row + "," + keyNum + ")");
            for (int i = 0; i < keyNum; i++) {
                SoftKey tempsoftKey = softKeys.get(i);
                if (tempsoftKey == softKey) {
                    return row ;
                }
            }
        }
        return -1;
    }

    /**************************************************************
     * get the max index of the specfic row.
     * @author mstar
     * @param  mRow
     *         the specfic row.
     * @return int
     *         the max index of the row.
     ***************************************************************/
    public int getMaxIndexOfRow(int mRow) {
        if (mSoftKeyboard == null) {
            return -1;
        }

        SoftKey softKey = getCurrentSelectKey();
        KeyRow keyRow;
        if (softKey == null) {
            return -1;
        }
        keyRow = mSoftKeyboard.getKeyRowForDisplay(mRow);
        if (keyRow == null) {
            //Log.d(TAG, "~~~~~~~~~~keyRow size:"+mSoftKeyboard.getRowNum());
            //Log.d(TAG, "~~~~~~~~~~keyRow is null:"+mRow);
            return -1;
        }
        return keyRow.mSoftKeys.size()-1;
    }

    /**************************************************************
     * get the next valid row.
     * @author mstar
     * @param  currentRow
     *         the current row.
     *        increase
     *         true row++   false row--
     * @return int
     *         the valid row.
     ***************************************************************/
    public int getValidRow(int currentRow, boolean increase) {
        //Log.d(TAG, "getValidRow");
        if (currentRow < 0 || currentRow > mSoftKeyboard.getRowNum()) {
            return currentRow;
        }
        int rowNum = currentRow;
        while (mSoftKeyboard.getKeyRowForDisplay(rowNum) == null) {
            //Log.d(TAG, "rowNmu:"+rowNum+" is null!");
            if (increase) {
                //Log.d(TAG, "getValidRow1:"+rowNum);
                rowNum = rowNum + 1;
                if (rowNum >= 7) {
                    break;
                }
            } else {
                //Log.d(TAG, "getValidRow2:"+rowNum);
                rowNum = rowNum - 1;
                if (rowNum <= 0) {
                    break;
                }
            }
        }
        return rowNum;
    }

    /**************************************************************
     * set the specifc softkey(int mRow, int mIndex ) selected.
     * @author mstar
     * @param  mRow
     *         the specfic row.
     *        mIndex
     *         the specfic index
     * @return boolean
     *         if true, set success, else set fall.
     ***************************************************************/
    public boolean setSoftKeySelected(int mRow, int mIndex) {
        //Log.d(TAG, "setSoftKeySelected (row,index)=("+mRow+","+mIndex+")");
        if (mSoftKeyboard == null) {
            return false;
        }

        if (mRow < 0 || mRow > mSoftKeyboard.getRowNum()-1) {
            return false;
        }

        if (mIndex < 0 || mIndex > mSoftKeyboard.getKeyRowForDisplay(mRow).mSoftKeys.size()) {
            return false;
        }

        return mSoftKeyboard.setOneKeySelected(mRow, mIndex);
    }

    /**************************************************************
     * According current selected key ,move to next key.
     * @author mstar
     * @param direction
     *             the specific direction to move.
     * @return void
     ***************************************************************/
    public void moveToNextKey(int direction) {
        int currentIndex = getIndexCurrentSelectedKey();
        int currentRow = getRowCurrentSelectedKey();
        int nextRow;

        SoftKey mSoftKey = mSoftKeyboard.getKey(currentRow, currentIndex);
        if (null == mSoftKey) {
            return;
        }

        switch (direction) {
            case KEY_UP:
                nextRow = getValidRow(currentRow - 1, false);
                if (currentRow <= 0) {
                    if (isCandOnShow) {
                        isSkbOnFocus = false;
                    } else {
                        isSkbOnFocus = true;
                    }
                } else {
                    int index = mSoftKeyboard.getmapToKey(mSoftKey.mLeft+mSoftKey.width()/2,
                                                          mSoftKey.mTop-mSoftKey.height()/2, nextRow);
                    if (index < 0) index = getMaxIndexOfRow(nextRow);
                    setSoftKeySelected(nextRow,index);
                }
                invalidate();
                break;
            case KEY_DOWN:
                if (currentRow < mSoftKeyboard.getRowNum() - 1) {
                    if (currentRow >= 3) {
                        return;
                    }

                    nextRow = getValidRow(currentRow + 1, true);
                    //        if (currentIndex <= getMaxIndexOfRow(nextRow)) {
                    //        //    Log.d(TAG, "***1*****");
                    //        setSoftKeySelected(nextRow, currentIndex);
                    //        } else {
                    //        //    Log.d(TAG, "***2*****:" + getMaxIndexOfRow(nextRow));
                    //        setSoftKeySelected(nextRow, getMaxIndexOfRow(nextRow));
                    //        }
                    int index = mSoftKeyboard.getmapToKey(mSoftKey.mLeft+mSoftKey.width()/2,
                                                          mSoftKey.mTop+mSoftKey.height()*3/2, nextRow);
                    if (index < 0) index = getMaxIndexOfRow(nextRow);
                    setSoftKeySelected(nextRow, index);
                } else {
                    // Nothing to do.
                }
                invalidate();
                break;
            case KEY_LEFT:
                //Log.d(TAG, "KEY_LEFT_current:(" + currentRow + "," + currentIndex
                //+ ")" + "==>(" + mSoftKeyboard.getRowNum() + ","
                //+ getMaxIndexOfRow(currentRow) + ")");
                if (currentIndex > 0) {
                    setSoftKeySelected(currentRow, currentIndex - 1);
                } else {
                    if (currentRow > 0) {
                        nextRow = getValidRow(currentRow - 1, false);
                        setSoftKeySelected(nextRow, getMaxIndexOfRow(nextRow));
                    } else {
                        // to do something:lost focus?
                    }
                }
                invalidate();
                break;
            case KEY_RIGHT:
                //Log.d(TAG, "KEY_RIGHT_current:(" + currentRow + "," +
                //currentIndex
                //+ ")" + "==>(" + mSoftKeyboard.getRowNum() + ","
                //+ getMaxIndexOfRow(currentRow) + ")");
                if (currentIndex < getMaxIndexOfRow(currentRow)) {
                    setSoftKeySelected(currentRow, currentIndex + 1);
                } else {
                    if (currentRow < mSoftKeyboard.getRowNum() - 1) {
                        if (currentRow >= 3) {
                            return;
                        }
                        setSoftKeySelected(getValidRow(currentRow + 1, true), 0);
                    }
                }
                invalidate();
                break;
            default:
                break;
        }
    }

    /*************************************************************
     * Obtain current skb view weather on focus.
     * @return
     *
     ************************************************************/
    public boolean getSkbViewFocusable() {
        return isSkbOnFocus;
    }

    /*************************************************************
     * Set current skb view weather on focus.
     * @param focusable
     *
     ************************************************************/
    public void setSkbViewFocusable(boolean focusable) {
        isSkbOnFocus = focusable;
        //invalidate();
    }

    /*************************************************************
     * Set current candidate view weather on show.
     * @param focusable
     *
     ************************************************************/
    public void setCandViewOnShow(boolean onShow) {
        isCandOnShow = onShow;
        //invalidate();
    }
}
