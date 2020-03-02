package kg.auca.example.converter

import android.content.res.TypedArray
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import java.util.*

class UnitConverterActivity : AppCompatActivity() {
    private lateinit var firstValueEditText: EditText
    private lateinit var secondValueEditText: EditText
    private lateinit var firstValueSpinner: Spinner
    private lateinit var secondValueSpinner: Spinner
    private lateinit var conversionUnitValues: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_converter)
        firstValueEditText = findViewById(R.id.firstValueEditText)
        firstValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                convert()
            }
        })
        secondValueEditText = findViewById(R.id.secondValueEditText)
        firstValueSpinner = findViewById(R.id.firstValueSpinner)
        secondValueSpinner = findViewById(R.id.secondValueSpinner)
        val onItemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                convert()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        firstValueSpinner.setOnItemSelectedListener(onItemSelectedListener)
        secondValueSpinner.setOnItemSelectedListener(onItemSelectedListener)
        val intent = intent
        val unit_names = intent.getIntExtra("UNIT_CONVERSION_NAMES", -1)
        val unit_values = intent.getIntExtra("UNIT_CONVERSION_VALUES", -1)
        val adapter = ArrayAdapter.createFromResource(
            this,
            unit_names,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        firstValueSpinner.adapter = adapter
        secondValueSpinner.adapter = adapter
        conversionUnitValues = resources.obtainTypedArray(unit_values)
    }

    private fun convert() {
        val firstValueText = firstValueEditText.text.toString()
        if (firstValueText.trim { it <= ' ' }.isEmpty()) {
            return
        }
        val fromValue: Float
        fromValue = try {
            firstValueText.toFloat()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid first value", Toast.LENGTH_SHORT).show()
            return
        }
        val fromIndex = firstValueSpinner.selectedItemPosition
        val toIndex = secondValueSpinner.selectedItemPosition
        val fromUnitValue = conversionUnitValues.getFloat(fromIndex, 1.0f)
        val toUnitValue = conversionUnitValues.getFloat(toIndex, 1.0f)
        val result = fromValue * fromUnitValue / toUnitValue
        secondValueEditText.setText(String.format(Locale.getDefault(), "%.2f", result))
    }
}