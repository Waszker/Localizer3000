package org.honorato.multistatetogglebutton;


import java.util.ArrayList;
import java.util.List;

import pl.papistudio.localizer3000.R;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MultiStateToggleButton extends ToggleButton {
	public static enum ToggleStates {
		On("On", 1), Off("Off", 0), Not_specified("N/S", 2);
		
		private String mText;
		private int mIdentifier;
		private ToggleStates(String txt, int id) { mText = txt; mIdentifier = id; }
		public String getText() { return mText; }
		public int getIdentifier() { return mIdentifier; }
	}
	private static final String TAG = MultiStateToggleButton.class.getSimpleName();	
	private static final String KEY_BUTTON_STATES = "button_states";
	private static final String KEY_INSTANCE_STATE = "instance_state";
	
	List<Button> buttons;
	boolean mMultipleChoice = false;

	public MultiStateToggleButton(Context context) {
		super(context, null);
		if (this.isInEditMode()) {
			return;
		}
	}

	public MultiStateToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		if (this.isInEditMode()) {
			return;
		}
		
		// TODO: this was added by me and is only applicable inside my project
		// in EditLocationFragments. Any other usage of it is not recomended.
		String[] array = { ToggleStates.On.getText(), 
						   ToggleStates.Off.getText(), 
						   ToggleStates.Not_specified.getText() };
		setElements(array, new boolean[array.length]);
	}

	/**
	 * If multiple choice is enabled, the user can select multiple
	 * values simultaneously.
	 * @param enable
	 */
	public void enableMultipleChoice(boolean enable) {
		this.mMultipleChoice = enable;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
		bundle.putBooleanArray(KEY_BUTTON_STATES, getStates());
		return bundle;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			setStates(bundle.getBooleanArray(KEY_BUTTON_STATES));
			state = bundle.getParcelable(KEY_INSTANCE_STATE);
		}
		super.onRestoreInstanceState(state);
	}

	/**
	 * Set multiple buttons with the specified texts and default
	 * initial values. Initial states are allowed, but both
	 * arrays must be of the same size.
	 * 
	 * @param texts An array of CharSequences for the buttons
	 * @param selected The default value for the buttons
	 */
	public void setElements(CharSequence[] texts, boolean[] selected) {
		// TODO: Add an exception
		if (texts == null || texts.length < 1) {
			Log.d(TAG, "Minimum quantity: 1");
			return;
		}

		boolean enableDefaultSelection = true;
		if (selected == null || texts.length != selected.length) {
			Log.d(TAG, "Invalid selection array");
			enableDefaultSelection = false;
		}

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
		mainLayout.removeAllViews();

		this.buttons = new ArrayList<Button>();
		for (int i = 0; i < texts.length; i++) {
			Button b = null;
			if (i == 0) {
				// Add a special view when there's only one element
				if (texts.length == 1) {
					b = (Button) inflater.inflate(R.layout.view_single_toggle_button, mainLayout, false);
				} else {
					b = (Button) inflater.inflate(R.layout.view_left_toggle_button, mainLayout, false);
				}
			} else if (i == texts.length - 1) {
				b = (Button) inflater.inflate(R.layout.view_right_toggle_button, mainLayout, false);
			} else {
				b = (Button) inflater.inflate(R.layout.view_center_toggle_button, mainLayout, false);
			}
			b.setText(texts[i]);
			final int position = i;
			b.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					setValue(position);
				}

			});
			mainLayout.addView(b);
			if (enableDefaultSelection) {
				setButtonState(b, selected[i]);
			}
			this.buttons.add(b);
		}
		mainLayout.setBackgroundResource(R.drawable.button_section_shape);
	}

	public void setElements(CharSequence[] elements) {
		int size = elements == null ? 0 : elements.length;
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> elements) {
		int size = elements == null ? 0 : elements.size();
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> elements, Object selected) {
		int size = 0;
		int index = -1;
		if (elements != null) {
			size = elements.size();
			index = elements.indexOf(selected);
		}
		boolean[] selectedArray = new boolean[size];
		if (index != -1 && index < size) {
			selectedArray[index] = true;
		}
		setElements(elements, new boolean[size]);
	}

	public void setElements(List<?> texts, boolean[] selected) {
		int size = texts == null ? 0 : texts.size();
		setElements(texts.toArray(new String[size]), selected);
	}

	public void setElements(int arrayResourceId, int selectedPosition) {
		// Get resources
		String[] elements = this.getResources().getStringArray(arrayResourceId);

		// Set selected boolean array
		int size = elements == null ? 0 : elements.length;
		boolean[] selected = new boolean[size];
		if (selectedPosition >= 0 && selectedPosition < size) {
			selected[selectedPosition] = true;
		}

		// Super
		setElements(elements, selected);
	}

	public void setElements(int arrayResourceId, boolean[] selected) {
		setElements(this.getResources().getStringArray(arrayResourceId), selected);
	}

	public void setButtonState(Button button, boolean selected) {
		if (button == null) {
			return;
		}
		button.setSelected(selected);
		if (selected) {
			button.setBackgroundResource(R.drawable.button_pressed);
			button.setTextAppearance(this.context, R.style.WhiteBoldText);
		} else {
			button.setBackgroundResource(R.drawable.button_not_pressed);
			button.setTextAppearance(this.context, R.style.BlackNormalText);
		}
	}

	public int getValue() {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (buttons.get(i).isSelected()) {
				return i;
			}
		}
		return -1;
	}
	
	public ToggleStates getStateValue() {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (buttons.get(i).isSelected()) {
				String text = buttons.get(i).getText().toString();
				return (text.contentEquals("Off") ? ToggleStates.Off :
						text.contentEquals("On") ?  ToggleStates.On :
								 				    ToggleStates.Not_specified);
			}
		}
		return ToggleStates.Not_specified;
	}
	
	public void setSelection(ToggleStates state) {
		int id = state.getIdentifier();
		if(id == 1 || id == 0) id = Math.abs(id-1);
		setValue(id);
	}

	public void setValue(int position) {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (mMultipleChoice) {
				if (i == position) {
					Button b = buttons.get(i);
					if (b != null) {
						setButtonState(b, !b.isSelected());
					}
				}
			} else {
				if (i == position) {
					setButtonState(buttons.get(i), true);
				} else if (!mMultipleChoice) {
					setButtonState(buttons.get(i), false);
				}
			}
		}
		super.setValue(position);
	}
	
	public boolean[] getStates() {
		int size = this.buttons == null ? 0 : this.buttons.size();
		boolean[] result = new boolean[size];
		for (int i = 0; i < size; i++) {
			result[i] = this.buttons.get(i).isSelected();
		}
		return result;
	}
	
	public void setStates(boolean[] selected) {
		if (this.buttons == null || selected == null || 
				this.buttons.size() != selected.length) {
			return;
		}
		int count = 0;
		for (Button b : this.buttons) {
			setButtonState(b, selected[count]);
			count++;
		}
	}
}