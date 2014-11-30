package org.apmem.tools.layouts;

import android.view.View;
import java.util.ArrayList;
import java.util.List;

class LineDefinition {
	private final List<ViewContainer> views = new ArrayList<ViewContainer>();
	private final LayoutConfiguration config;
	private final int maxLength;
	private int lineLength;
	private int lineThickness;
	private int lineLengthWithSpacing;
	private int lineThicknessWithSpacing;
	private int lineStartThickness;
	private int lineStartLength;

	public LineDefinition(int maxLength, LayoutConfiguration config) {
		this.lineStartThickness = 0;
		this.lineStartLength = 0;
		this.maxLength = maxLength;
		this.config = config;
	}

	public void addView(View child) {
		this.addView(this.views.size(), child);
	}

	public void addView(int i, View child) {
		final FlowLayout.LayoutParams lp = (FlowLayout.LayoutParams) child
				.getLayoutParams();
		final int hSpacing = lp.horizontalSpacingSpecified() ? lp.horizontalSpacing
				: this.config.getHorizontalSpacing();
		final int vSpacing = lp.verticalSpacingSpecified() ? lp.verticalSpacing
				: this.config.getVerticalSpacing();
		final int childLength;
		final int childThickness;
		final int spacingLength;
		final int spacingThickness;
		if (this.config.getOrientation() == FlowLayout.HORIZONTAL) {
			childLength = child.getMeasuredWidth();
			childThickness = child.getMeasuredHeight();
			spacingLength = hSpacing;
			spacingThickness = vSpacing;
		} else {
			childLength = child.getMeasuredHeight();
			childThickness = child.getMeasuredWidth();
			spacingLength = vSpacing;
			spacingThickness = hSpacing;
		}
		final ViewContainer container = new ViewContainer(child, spacingLength,
				spacingThickness);
		container.setLength(childLength);
		container.setThickness(childThickness);
		this.views.add(i, container);
		this.lineLength = this.lineLengthWithSpacing + childLength;
		this.lineLengthWithSpacing = this.lineLength + spacingLength;
		this.lineThicknessWithSpacing = Math.max(this.lineThicknessWithSpacing,
				childThickness + spacingThickness);
		this.lineThickness = Math.max(this.lineThickness, childThickness);
	}

	public boolean canFit(View child) {
		final int childLength;
		if (this.config.getOrientation() == FlowLayout.HORIZONTAL) {
			childLength = child.getMeasuredWidth();
		} else {
			childLength = child.getMeasuredHeight();
		}
		return lineLengthWithSpacing + childLength <= maxLength;
	}

	public int getLineStartThickness() {
		return lineStartThickness;
	}

	public int getLineThickness() {
		return lineThicknessWithSpacing;
	}

	public int getLineLength() {
		return lineLength;
	}

	public int getLineStartLength() {
		return lineStartLength;
	}

	public List<ViewContainer> getViews() {
		return views;
	}

	public void setThickness(int thickness) {
		int thicknessSpacing = this.lineThicknessWithSpacing
				- this.lineThickness;
		this.lineThicknessWithSpacing = thickness;
		this.lineThickness = thickness - thicknessSpacing;
	}

	public void setLength(int length) {
		int lengthSpacing = this.lineLengthWithSpacing - this.lineLength;
		this.lineLength = length;
		this.lineLengthWithSpacing = length + lengthSpacing;
	}

	public void addLineStartThickness(int extraLineStartThickness) {
		this.lineStartThickness += extraLineStartThickness;
	}

	public void addLineStartLength(int extraLineStartLength) {
		this.lineStartLength += extraLineStartLength;
	}
}