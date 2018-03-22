package com.example.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CurrencyConversionActivity extends AppCompatActivity {

	private final String PREFERENCE_FIRST_VALUE_KEY           = "firstValue";
	private final String PREFERENCE_FIRST_UNIT_SELECTION_KEY  = "firstSelectedPosition";
	private final String PREFERENCE_SECOND_UNIT_SELECTION_KEY = "secondSelectedPosition";

	private final String CONVERSION_RATES_FILE_NAME = "currency_rates.json";

	private TextWatcher firstTextWatcher,
						secondTextWatcher,
						currencyRatioTextWatcher;

	private EditText firstValueEditText,
					 secondValueEditText;

	private Spinner firstUnitSpinner,
					secondUnitSpinner;
	
	private EditText currencyRatioValueEditText;

	private SharedPreferences previousUserValues;

	private JSONObject conversionRatios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currency_conversion);

		firstValueEditText = findViewById(R.id.firstCurrencyValueEditText);
		secondValueEditText = findViewById(R.id.secondCurrencyValueEditText);

		firstUnitSpinner = findViewById(R.id.firstCurrencyUnitSpinner);
		secondUnitSpinner = findViewById(R.id.secondCurrencyUnitSpinner);

		currencyRatioValueEditText = findViewById(R.id.currencyRatioValueEditText);

		final String PREFERENCE_FILE = "com.example.converter.values.currency";
		previousUserValues = getSharedPreferences(PREFERENCE_FILE, 0);

		populateSpinners();
		loadConversionData();

		setupEventHandlers();
		loadPreviousUserValues();
	}

	@Override
	protected void onStop() {
		super.onStop();

		savePreviousUserValues();
		saveConversionData();
	}

	private void populateSpinners() {
		ArrayAdapter<CharSequence> adapter =
			ArrayAdapter.createFromResource(
				this,
				R.array.currency_conversion_unit_names,
				android.R.layout.simple_spinner_item
			);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		firstUnitSpinner.setAdapter(adapter);
		secondUnitSpinner.setAdapter(adapter);
	}

	private void loadConversionData() {
		File conversionRatiosFile;
		InputStream inputStream = null;

		if ((conversionRatiosFile = getFileStreamPath(CONVERSION_RATES_FILE_NAME)).exists()) {
			try {
				inputStream = new FileInputStream(conversionRatiosFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			inputStream = getResources().openRawResource(R.raw.initial_currency_rates);
		}

		if (inputStream != null) {
			String result = "";
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder stringBuilder = new StringBuilder(); String nextLine;
				while((nextLine = bufferedReader.readLine()) != null) {
					stringBuilder.append(nextLine);
				}

				result = stringBuilder.toString();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				conversionRatios = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveConversionData() {
		FileOutputStream outputStream = null;
		try {
			outputStream = openFileOutput(CONVERSION_RATES_FILE_NAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (outputStream != null) {
			try {
				outputStream.write(conversionRatios.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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

		currencyRatioTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String firstSelectedCurrency  = (String) firstUnitSpinner.getSelectedItem();
				String secondSelectedCurrency = (String) secondUnitSpinner.getSelectedItem();

				String key = firstSelectedCurrency + "-" + secondSelectedCurrency;

				try {
					double ratio = Double.parseDouble(s.toString());

					try {
						conversionRatios.put(key, ratio);
					} catch (JSONException jsonException) {
						jsonException.printStackTrace();
					}
				} catch (NumberFormatException numberFormatException) {
					numberFormatException.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
		};
	}

	private void setupSpinnersEventHandlers() {
		AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int from, long id) {
				recalculateResult(
					firstValueEditText, firstUnitSpinner,
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

		int firstSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY, 0);
		if (firstSpinnerSelectedPosition >= 0 && firstSpinnerSelectedPosition < firstUnitSpinner.getAdapter().getCount()) {
            firstUnitSpinner.setSelection(firstSpinnerSelectedPosition);
        }

		int secondSpinnerSelectedPosition = previousUserValues.getInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY, 0);
        if (secondSpinnerSelectedPosition >= 0 && secondSpinnerSelectedPosition < secondUnitSpinner.getAdapter().getCount()) {
            secondUnitSpinner.setSelection(secondSpinnerSelectedPosition);
        }
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

	private void recalculateResult(EditText fromEditText, Spinner fromUnitSpinner,
								   EditText toEditText,   Spinner toUnitSpinner,
								   TextWatcher toEditTextTextWatcher) {		
		String firstSelectedCurrency  = (String) fromUnitSpinner.getSelectedItem();
		String secondSelectedCurrency = (String) toUnitSpinner.getSelectedItem();

		String key = firstSelectedCurrency + "-" + secondSelectedCurrency;

		double ratio = 1;
		if (!firstSelectedCurrency.equals(secondSelectedCurrency)) {
			ratio = 0;
			try {
				ratio = conversionRatios.getDouble(key);

				currencyRatioValueEditText.removeTextChangedListener(currencyRatioTextWatcher);
				currencyRatioValueEditText.setText(String.valueOf(ratio));
				currencyRatioValueEditText.addTextChangedListener(currencyRatioTextWatcher);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

		double value = 0;
		try {
			value = Double.parseDouble(fromEditText.getText().toString());
		} catch (NumberFormatException ignored) {}

		double result = value * ratio;

		toEditText.removeTextChangedListener(toEditTextTextWatcher);
		String RESULT_DECIMAL_FORMAT = "#.##";
		toEditText.setText(new DecimalFormat(RESULT_DECIMAL_FORMAT).format(result));
		toEditText.addTextChangedListener(toEditTextTextWatcher);
	}
}
