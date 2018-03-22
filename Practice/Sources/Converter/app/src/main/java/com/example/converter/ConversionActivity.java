package com.example.converter;

import java.text.DecimalFormat;

import android.content.Intent;
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

public class ConversionActivity extends AppCompatActivity {
	private final String PREFERENCE_FILE_SUFFIX = "com.example.converter.values";

	private final String PREFERENCE_FIRST_VALUE_KEY           = "firstValue";
	private final String PREFERENCE_FIRST_UNIT_SELECTION_KEY  = "firstSelectedPosition";
	private final String PREFERENCE_SECOND_UNIT_SELECTION_KEY = "secondSelectedPosition";

	private final String RESULT_DECIMAL_FORMAT = "#.##";

	private TextWatcher firstTextWatcher,
						secondTextWatcher;

	private EditText firstValueEditText,
					 secondValueEditText;

	private Spinner firstUnitSpinner,
					secondUnitSpinner;

	private SharedPreferences previousUserValues;

	private Intent activityIntent;
	private TypedArray conversionFactors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversion);

		firstValueEditText = findViewById(R.id.firstValueEditText);
		secondValueEditText = findViewById(R.id.secondValueEditText);

		firstUnitSpinner = findViewById(R.id.firstUnitSpinner);
		secondUnitSpinner = findViewById(R.id.secondUnitSpinner);

		activityIntent = getIntent();

		int resourceId = activityIntent.getExtras().getInt(MainActivity.UNITS_INTENT_EXTRA);
		previousUserValues = getSharedPreferences(PREFERENCE_FILE_SUFFIX + resourceId, 0);

		populateSpinners();
		loadConversionData();

		setupEventHandlers();
		loadPreviousUserValues();
	}

	@Override
	protected void onStop() {
		super.onStop();

		savePreviousUserValues();
		unloadConversionData();
	}

	private void populateSpinners() {
		Intent intent = getIntent();

		int resourceId = intent.getExtras().getInt(MainActivity.UNITS_INTENT_EXTRA);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resourceId, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		firstUnitSpinner.setAdapter(adapter);
		secondUnitSpinner.setAdapter(adapter);
	}

	private void loadConversionData() {
		int resourceId = activityIntent.getExtras().getInt(MainActivity.FACTORS_INTENT_EXTRA);
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
			public void afterTextChanged(Editable arg0) {
				recalculateResult(
					firstValueEditText,  firstUnitSpinner,
					secondValueEditText, secondUnitSpinner,
					secondTextWatcher
				);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		};

		firstValueEditText.addTextChangedListener(firstTextWatcher);

		secondTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				recalculateResult(
					secondValueEditText, secondUnitSpinner,
					firstValueEditText,  firstUnitSpinner,
					firstTextWatcher
				);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		};

		secondValueEditText.addTextChangedListener(secondTextWatcher);
	}

	private void setupSpinnersEventHandlers() {
		AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int from, long id) {
				recalculateResult(
					firstValueEditText,  firstUnitSpinner,
					secondValueEditText, secondUnitSpinner,
					secondTextWatcher
				);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
		};

		firstUnitSpinner.setOnItemSelectedListener(listener);
		secondUnitSpinner.setOnItemSelectedListener(listener);
	}

	private void loadPreviousUserValues() {
		String firstValue = previousUserValues.getString(PREFERENCE_FIRST_VALUE_KEY, "");
		firstValueEditText.setText(firstValue);

		int firstSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY,0);
		firstUnitSpinner.setSelection(firstSpinnerSelectedPosition);

		int secondSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY,0);
		secondUnitSpinner.setSelection(secondSpinnerSelectedPosition);
	}

	private void savePreviousUserValues() {
		SharedPreferences.Editor editor = previousUserValues.edit();

		String firstValue = firstValueEditText.getText().toString();
		editor.putString(PREFERENCE_FIRST_VALUE_KEY, firstValue);

		int firstSpinnerSelectedPosition = firstUnitSpinner.getSelectedItemPosition();
		editor.putInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY, firstSpinnerSelectedPosition);

		int secondSpinnerSelectedPosition = secondUnitSpinner.getSelectedItemPosition();
		editor.putInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY, secondSpinnerSelectedPosition);

		editor.apply();
	}

	private void recalculateResult(
		             EditText fromEditText, Spinner fromUnitSpinner,
					 EditText toEditText,   Spinner toUnitSpinner,
					 TextWatcher toEditTextTextWatcher
	             ) {
		int fromSelectedUnitPosition = fromUnitSpinner.getSelectedItemPosition();
		int toSelectedUnitPosition = toUnitSpinner.getSelectedItemPosition();

		double value = 0;
		try {
			value = Double.parseDouble(fromEditText.getText().toString());
		} catch (NumberFormatException ignored) {}

		double fromValue = conversionFactors.getFloat(fromSelectedUnitPosition, 1.0f);
		double toValue = conversionFactors.getFloat(toSelectedUnitPosition, 1.0f);

		double result = value * fromValue / toValue;

		toEditText.removeTextChangedListener(toEditTextTextWatcher);
		toEditText.setText(new DecimalFormat(RESULT_DECIMAL_FORMAT).format(result));
		toEditText.addTextChangedListener(toEditTextTextWatcher);
	}
}
