package com.example.converter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;

public class CurrencyConversionActivity extends AppCompatActivity {
	private static final String SERVICE_ENDPOINT_URL =
        "http://api.fixer.io/latest?access_key=67e043a4d46e4fc44811a2d8f263ce92";
	private static final String PREFERENCE_FILE =
        "com.example.converter.values.currency";

	private static final int PAUSE_BEFORE_HIDING_THE_PROGRESS_BAR_IN_MS =
        1000;

	private static final String PREFERENCE_FIRST_VALUE_KEY =
        "firstValue";
	private static final String PREFERENCE_FIRST_UNIT_SELECTION_KEY =
        "firstSelectedPosition";
	private static final String PREFERENCE_SECOND_UNIT_SELECTION_KEY =
        "secondSelectedPosition";

    private static final String RESULT_DECIMAL_FORMAT =
        "#.####";

	private TextWatcher firstTextWatcher,
						secondTextWatcher;

	private EditText firstValueEditText,
					 secondValueEditText;

	private Spinner firstUnitSpinner,
					secondUnitSpinner;

	private ProgressDialog progressDialog;

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

		previousUserValues = getSharedPreferences(PREFERENCE_FILE, 0);
		conversionRatios = new JSONObject();

        progressDialog = new ProgressDialog(CurrencyConversionActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        setupEventHandlers();
		loadConversionData();
	}

	@Override
	protected void onStop() {
		super.onStop();

		saveUserInput();
	}

    @Override
    protected void onPause() {
        super.onPause();

        progressDialog.dismiss();
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
				calculateResult(
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
				calculateResult(
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
				calculateResult(
					firstValueEditText, firstUnitSpinner,
					secondValueEditText, secondUnitSpinner,
					secondTextWatcher
				);
			}
		};

		firstUnitSpinner.setOnItemSelectedListener(listener);
		secondUnitSpinner.setOnItemSelectedListener(listener);
	}

	private void loadConversionData() {
		showProgressDialog();

		NetworkInfo networkInfo = getConnectivityInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			downloadInBackground();
		} else {
            hideProgressDialog();
            reportError(getString(R.string.conversion_data_update_error_message));
		}
	}

	private void showProgressDialog() {
		progressDialog.setMessage(getString(R.string.please_wait_message));
		progressDialog.show();
	}

    private void hideProgressDialog() {
        hideProgressDialogAfterDelay(0);
    }

	private void hideProgressDialogAfterDelay(int delayInMilliseconds) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				progressDialog.hide();
			}
		}, delayInMilliseconds);
	}

	private NetworkInfo getConnectivityInfo() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
	}

	private void downloadInBackground() {
		new AsyncTask<String, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... urls) {
                return fetchJSON(urls[0]);
            }

			@Override
			protected void onPostExecute(JSONObject serviceData) {
				prepareConversionRatios(serviceData);
				populateSpinners();
				loadUserInput();

				hideProgressDialogAfterDelay(
					PAUSE_BEFORE_HIDING_THE_PROGRESS_BAR_IN_MS
				);
			}
		}.execute(SERVICE_ENDPOINT_URL);
	}

	private JSONObject fetchJSON(String serviceEndpointURL) {
		JSONObject result = new JSONObject();

		InputStream inputStream = null;
		try {
			URL url = new URL(serviceEndpointURL);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(15000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();

			int response = connection.getResponseCode();
			if (response == 200) {
				inputStream = connection.getInputStream();
				if (inputStream != null) {
					result = readJSON(inputStream);
				}
			}
		} catch (IOException e) {
			reportError(getString(R.string.conversion_data_update_error_message));

			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	private JSONObject readJSON(InputStream inputStream) {
		JSONObject result = new JSONObject();

		String content = readStream(inputStream);
		try {
			result = new JSONObject(content);
		} catch (JSONException e) {
			reportError(getString(R.string.conversion_data_update_error_message));

			e.printStackTrace();
		}

		return result;
	}

	private String readStream(InputStream inputStream) {
		String content = "";
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder stringBuilder = new StringBuilder(); String nextLine;
			while((nextLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(nextLine);
			}

			content = stringBuilder.toString();
		} catch (IOException e) {
			reportError(getString(R.string.conversion_data_update_error_message));

			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

	private void prepareConversionRatios(JSONObject serviceData) {
		JSONObject rates = serviceData.optJSONObject("rates");
		String baseCurrency = serviceData.optString("base");

		if (rates != null && baseCurrency != null) {
			try {
				rates.put(baseCurrency, 1.0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			conversionRatios = rates;
		}
	}

	private void populateSpinners() {
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

		Iterator<?> keys = conversionRatios.keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			adapter.add(key);
		}

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		firstUnitSpinner.setAdapter(adapter);
		secondUnitSpinner.setAdapter(adapter);
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
		SharedPreferences.Editor editor =
			previousUserValues.edit();

		String firstValue = firstValueEditText.getText().toString();
		int firstSpinnerSelectedPosition = firstUnitSpinner.getSelectedItemPosition();
		int secondSpinnerSelectedPosition = secondUnitSpinner.getSelectedItemPosition();

		editor.putString(PREFERENCE_FIRST_VALUE_KEY, firstValue);
		editor.putInt(PREFERENCE_FIRST_UNIT_SELECTION_KEY, firstSpinnerSelectedPosition);
		editor.putInt(PREFERENCE_SECOND_UNIT_SELECTION_KEY, secondSpinnerSelectedPosition);

		editor.apply();
	}

	private void calculateResult(
		             EditText fromEditText,
					 Spinner fromUnitSpinner,
					 EditText toEditText,
					 Spinner toUnitSpinner,
					 TextWatcher toEditTextTextWatcher
	             ) {
		String fromSelectedCurrency = (String) fromUnitSpinner.getSelectedItem();
		String toSelectedCurrency = (String) toUnitSpinner.getSelectedItem();

		double value = 0.0;
		try {
			value = Double.parseDouble(fromEditText.getText().toString());
		} catch (NumberFormatException ignored) { }

		double fromValue = conversionRatios.optDouble(fromSelectedCurrency, 1.0);
		double toValue = conversionRatios.optDouble(toSelectedCurrency, 1.0);

		double result = (value / fromValue) * toValue;

		toEditText.removeTextChangedListener(toEditTextTextWatcher);
		toEditText.setText(new DecimalFormat(RESULT_DECIMAL_FORMAT).format(result));
		toEditText.addTextChangedListener(toEditTextTextWatcher);
	}

	private void reportError(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
