package com.example.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
	public final static String UNITS_INTENT_EXTRA   = "com.example.converter.units";
	public final static String FACTORS_INTENT_EXTRA = "com.example.converter.factors";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupButtonsEventHandlers();
	}

	private void setupButtonsEventHandlers() {
		Button lengthButton = findViewById(R.id.lengthButton);
		lengthButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), ConversionActivity.class);
				intent.putExtra(UNITS_INTENT_EXTRA,   R.array.length_conversion_unit_names);
				intent.putExtra(FACTORS_INTENT_EXTRA, R.array.length_conversion_factors);

				startActivity(intent);
			}

		});

		Button massButton = findViewById(R.id.massButton);
		massButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), ConversionActivity.class);
				intent.putExtra(UNITS_INTENT_EXTRA,   R.array.mass_conversion_unit_names);
				intent.putExtra(FACTORS_INTENT_EXTRA, R.array.length_conversion_factors);

				startActivity(intent);
			}

		});

		Button currencyButton = findViewById(R.id.currencyButton);
		currencyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), CurrencyConversionActivity.class);
				startActivity(intent);
			}

		});
	}
}
