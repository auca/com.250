package com.example.converter;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;

public class ConversionActivity extends AppCompatActivity {

    private final String PREFERENCE_FIRST_VALUE_KEY =
        "firstValue";
	private final String PREFERENCE_FIRST_UNIT_SELECTION_KEY  =
        "firstSelectedPosition";
	private final String PREFERENCE_SECOND_UNIT_SELECTION_KEY =
        "secondSelectedPosition";

    private TextWatcher firstTextWatcher,
						secondTextWatcher;

	private EditText firstValueEditText,
					 secondValueEditText;

	private Spinner firstUnitSpinner,
					secondUnitSpinner;

	private SharedPreferences previousUserValues;
	private TypedArray conversionFactors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        final String PREFERENCE_FILE_SUFFIX = "com.example.converter.values";

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversion);

		firstValueEditText = findViewById(R.id.firstValueEditText);
		secondValueEditText = findViewById(R.id.secondValueEditText);

		firstUnitSpinner = findViewById(R.id.firstUnitSpinner);
		secondUnitSpinner = findViewById(R.id.secondUnitSpinner);

		int resourceId = getIntent().getExtras().getInt(MainActivity.UNITS_INTENT_EXTRA);
        previousUserValues = getSharedPreferences(PREFERENCE_FILE_SUFFIX + resourceId, 0);

		populateSpinners();
		loadConversionData();
		setupEventHandlers();
		loadUserInput();
	}

	@Override
	protected void onStop() {
		super.onStop();

		saveUserInput();
		unloadConversionData();
	}

	private void populateSpinners() {
		int resourceId = getIntent().getExtras().getInt(MainActivity.UNITS_INTENT_EXTRA);

		ArrayAdapter<CharSequence> adapter =
            ArrayAdapter.createFromResource(
                this,
                resourceId,
                android.R.layout.simple_spinner_item
            );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		firstUnitSpinner.setAdapter(adapter);
		secondUnitSpinner.setAdapter(adapter);
	}

	private void loadConversionData() {
		int resourceId = getIntent().getExtras().getInt(MainActivity.FACTORS_INTENT_EXTRA);
		conversionFactors = getResources().obtainTypedArray(resourceId);
	}

	private void unloadConversionData() {
		conversionFactors.recycle();
	}

	private void setupEventHandlers() {
		setupEditTextEventHandlers();
		setupSpinnersEventHandlers();
	}

	private void setupEditTextEventHandlers() {
		firstTextWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }

			@Override
			public void afterTextChanged(Editable arg0) {
				recalculateResult(
				    firstValueEditText,  firstUnitSpinner,
					secondValueEditText, secondUnitSpinner,
					secondTextWatcher
                );
			}
		};
		secondTextWatcher = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }

			@Override
			public void afterTextChanged(Editable s) {
				recalculateResult(
					secondValueEditText, secondUnitSpinner,
					firstValueEditText,  firstUnitSpinner,
					firstTextWatcher
				);
			}
		};

		firstValueEditText.addTextChangedListener(firstTextWatcher);
		secondValueEditText.addTextChangedListener(secondTextWatcher);
	}

	private void setupSpinnersEventHandlers() {
		AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> parent) { }

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int from, long id) {
				recalculateResult(
					firstValueEditText,  firstUnitSpinner,
					secondValueEditText, secondUnitSpinner,
					secondTextWatcher
				);
			}
		};

		firstUnitSpinner.setOnItemSelectedListener(listener);
		secondUnitSpinner.setOnItemSelectedListener(listener);
	}

	private void loadUserInput() {
		String firstValue = previousUserValues.getString(PREFERENCE_FIRST_VALUE_KEY, "");
        int firstSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY, 0);
        int secondSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY, 0);

		firstValueEditText.setText(firstValue);
		if (firstSpinnerSelectedPosition >= 0 && firstSpinnerSelectedPosition < firstUnitSpinner.getAdapter().getCount()) {
			firstUnitSpinner.setSelection(firstSpinnerSelectedPosition);
		}
		if (secondSpinnerSelectedPosition >= 0 && secondSpinnerSelectedPosition < secondUnitSpinner.getAdapter().getCount()) {
			secondUnitSpinner.setSelection(secondSpinnerSelectedPosition);
		}
	}

	private void saveUserInput() {
		SharedPreferences.Editor editor = previousUserValues.edit();

		String firstValue = firstValueEditText.getText().toString();
        int firstSpinnerSelectedPosition = firstUnitSpinner.getSelectedItemPosition();
        int secondSpinnerSelectedPosition = secondUnitSpinner.getSelectedItemPosition();

		editor.putString(PREFERENCE_FIRST_VALUE_KEY, firstValue);
		editor.putInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY, firstSpinnerSelectedPosition);
		editor.putInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY, secondSpinnerSelectedPosition);

		editor.apply();
	}

	private void recalculateResult(
		             EditText fromEditText,
                     Spinner fromUnitSpinner,
                     EditText toEditText,
                     Spinner toUnitSpinner,
                     TextWatcher toEditTextTextWatcher
                 ) {
        final String RESULT_DECIMAL_FORMAT = "#.##";

		int fromSelectedUnitPosition = fromUnitSpinner.getSelectedItemPosition();
		int toSelectedUnitPosition = toUnitSpinner.getSelectedItemPosition();

		double value = 0;
		try {
			value = Double.parseDouble(fromEditText.getText().toString());
		} catch (NumberFormatException ignored) { }

		double fromValue = conversionFactors.getFloat(fromSelectedUnitPosition, 1.0f);
		double toValue = conversionFactors.getFloat(toSelectedUnitPosition, 1.0f);

		double result = value * fromValue / toValue;

		toEditText.removeTextChangedListener(toEditTextTextWatcher);
        toEditText.setText(new DecimalFormat(RESULT_DECIMAL_FORMAT).format(result));
		toEditText.addTextChangedListener(toEditTextTextWatcher);
	}
}
