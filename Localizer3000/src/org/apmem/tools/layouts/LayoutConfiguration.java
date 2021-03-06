package org.apmem.tools.layouts;

import pl.papistudio.localizer3000.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;

class LayoutConfiguration {
	private int horizontalSpacing = 0;
	private int verticalSpacing = 0;
	private int orientation = FlowLayout.HORIZONTAL;
	private boolean debugDraw = false;
	private float weightDefault = 0;
	private int gravity = Gravity.START | Gravity.TOP;
	private int layoutDirection = FlowLayout.LAYOUT_DIRECTION_LTR;

	public LayoutConfiguration(Context context, AttributeSet attributeSet) {
		TypedArray a = context.obtainStyledAttributes(attributeSet,
				R.styleable.FlowLayout);
		try {
			this.setHorizontalSpacing(a.getDimensionPixelSize(
					R.styleable.FlowLayout_horizontalSpacing, 0));
			this.setVerticalSpacing(a.getDimensionPixelSize(
					R.styleable.FlowLayout_verticalSpacing, 0));
			this.setOrientation(a.getInteger(
					R.styleable.FlowLayout_orientation, FlowLayout.HORIZONTAL));
			this.setDebugDraw(a.getBoolean(R.styleable.FlowLayout_debugDraw,
					false));
			this.setWeightDefault(a.getFloat(
					R.styleable.FlowLayout_weightDefault, 0.0f));
			this.setGravity(a.getInteger(
					R.styleable.FlowLayout_android_gravity, Gravity.NO_GRAVITY));
			this.setLayoutDirection(a.getInteger(
					R.styleable.FlowLayout_layoutDirection,
					FlowLayout.LAYOUT_DIRECTION_LTR));
		} finally {
			a.recycle();
		}
	}

	public int getHorizontalSpacing() {
		return this.horizontalSpacing;
	}

	public void setHorizontalSpacing(int horizontalSpacing) {
		this.horizontalSpacing = Math.max(0, horizontalSpacing);
	}

	public int getVerticalSpacing() {
		return this.verticalSpacing;
	}

	public void setVerticalSpacing(int verticalSpacing) {
		this.verticalSpacing = Math.max(0, verticalSpacing);
	}

	public int getOrientation() {
		return this.orientation;
	}

	public void setOrientation(int orientation) {
		if (orientation == FlowLayout.VERTICAL) {
			this.orientation = orientation;
		} else {
			this.orientation = FlowLayout.HORIZONTAL;
		}
	}

	public boolean isDebugDraw() {
		return this.debugDraw;
	}

	public void setDebugDraw(boolean debugDraw) {
		this.debugDraw = debugDraw;
	}

	public float getWeightDefault() {
		return this.weightDefault;
	}

	public void setWeightDefault(float weightDefault) {
		this.weightDefault = Math.max(0, weightDefault);
	}

	public int getGravity() {
		return this.gravity;
	}

	public void setGravity(int gravity) {
		if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == 0) {
			gravity |= Gravity.START;
		}
		if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
			gravity |= Gravity.TOP;
		}
		this.gravity = gravity;
	}

	public int getLayoutDirection() {
		return layoutDirection;
	}

	public void setLayoutDirection(int layoutDirection) {
		if (layoutDirection == FlowLayout.LAYOUT_DIRECTION_RTL) {
			this.layoutDirection = layoutDirection;
		} else {
			this.layoutDirection = FlowLayout.LAYOUT_DIRECTION_LTR;
		}
	}
}