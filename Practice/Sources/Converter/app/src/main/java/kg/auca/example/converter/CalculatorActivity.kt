package kg.auca.example.converter

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*

class CalculatorActivity : AppCompatActivity() {
    private lateinit var calculator: Calculator
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        calculator = Calculator()
        inputEditText = findViewById(R.id.inputEditText)

        updateInputField()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.lengthUnitConverterMenuItem -> {
                val intent = Intent(this, UnitConverterActivity::class.java)
                intent.putExtra("UNIT_CONVERSION_NAMES", R.array.lengthConversionUnitNames)
                intent.putExtra("UNIT_CONVERSION_VALUES", R.array.lengthConversionValues)
                startActivity(intent)
                true
            }
            R.id.massUnitConverterMenuItem -> {
                val intent = Intent(this, UnitConverterActivity::class.java)
                intent.putExtra("UNIT_CONVERSION_NAMES", R.array.massConversionUnitNames)
                intent.putExtra("UNIT_CONVERSION_VALUES", R.array.massConversionValues)
                startActivity(intent)
                true
            }
            R.id.currencyConverterMenuItem -> {
                val intent = Intent(this, CurrencyConverterActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onClearButtonClick(view: View?) {
        calculator.reset()
        updateInputField()
    }

    fun onChangeSignButtonClick(view: View?) {
        calculator.negate()
        updateInputField()
    }

    fun onNumericButtonClick(view: View) {
        val text = (view as Button).text.toString()
        calculator.addDigit(text.toInt())
        updateInputField()
    }

    fun onDecimalPointButtonClick(view: View?) {
        calculator.addDecimalPoint()
        updateInputField()
    }

    fun onBinaryOperationButtonClick(view: View) {
        val text = view.tag.toString().toUpperCase(Locale.getDefault())
        try {
            calculator.performBinaryOperation(Calculator.Operation.valueOf(text))
        } catch (exception: ArithmeticException) {
            reportError(getString(R.string.DivisionByZeroErrorText))
        }
        updateInputField()
    }

    fun onCalculateResultButtonClick(view: View?) {
        try {
            calculator.calculate()
        } catch (exception: ArithmeticException) {
            reportError(getString(R.string.DivisionByZeroErrorText))
        }
        updateInputField()
    }

    private fun updateInputField() {
        inputEditText.setText(calculator.currentValue.toPlainString())
    }

    private fun reportError(errorMessage: CharSequence) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}