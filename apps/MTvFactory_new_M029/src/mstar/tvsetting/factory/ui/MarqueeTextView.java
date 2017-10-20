package mstar.tvsetting.factory.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context, AttributeSet attrs, int arg2) {
		super(context, attrs, arg2);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MarqueeTextView(Context context) {
		this(context, null);
	}
	
	@Override
	public boolean isFocused() {
		return true;
	}
	
	@Override
	protected void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
	}

}
