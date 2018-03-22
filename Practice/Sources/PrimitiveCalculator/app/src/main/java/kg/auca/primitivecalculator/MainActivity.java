package kg.auca.primitivecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText firstOperandEditText;
    private EditText secondOperandEditText;
    private RadioGroup operationRadioGroup;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstOperandEditText = findViewById(R.id.firstOperandEditText);
        secondOperandEditText = findViewById(R.id.secondOperandEditText);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        };

        firstOperandEditText.addTextChangedListener(textWatcher);
        secondOperandEditText.addTextChangedListener(textWatcher);

        operationRadioGroup = findViewById(R.id.operationRadioGroup);
        operationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                calculate();
            }
        });

        resultTextView = findViewById(R.id.resultTextView);
    }

    private void calculate() {
        double firstOperand;
        try {
            firstOperand = Double.parseDouble(firstOperandEditText.getText().toString());
        } catch(NumberFormatException ignored) {
            resultTextView.setText("");
            return;
        }
        double secondOperand;
        try {
            secondOperand = Double.parseDouble(secondOperandEditText.getText().toString());
        } catch(NumberFormatException ignored) {
            resultTextView.setText("");
            return;
        }

        double result = 0.0;

        int selectedOperation = operationRadioGroup.getCheckedRadioButtonId();
        switch(selectedOperation) {
            case R.id.additionRadioButton:
                result = firstOperand + secondOperand;
                break;
            case R.id.subtractionRadioButton:
                result = firstOperand - secondOperand;
                break;
            case R.id.multiplicationRadioButton:
                result = firstOperand * secondOperand;
                break;
            case R.id.divisionRadioButton:
                if (Math.abs(secondOperand - 0.0) <= 0.000001) {
                    Toast.makeText(this, "Can't divide by zero", Toast.LENGTH_SHORT).show();
                } else {
                    result = firstOperand / secondOperand;
                }
                break;
        }

        resultTextView.setText(
            String.format(Locale.getDefault(), "%.2f", result)
        );
    }
}
