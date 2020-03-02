package kg.auca.example.converter

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class CurrencyConverterActivity : AppCompatActivity() {
    companion object {
        private const val FIXER_IO_API_KEY = "3209678a149bde99881ef1e7faf871b3"
        private const val CONVERSION_RATES_FILE_NAME = "custom_conversion_rates.json"
    }

    private lateinit var firstValueEditText: EditText
    private lateinit var secondValueEditText: EditText
    private lateinit var currencyRateEditText: EditText
    private lateinit var firstValueSpinner: Spinner
    private lateinit var secondValueSpinner: Spinner
    private lateinit var networkRequestQueue: RequestQueue
    private lateinit var conversionRatios: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        networkRequestQueue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_converter)
        firstValueEditText = findViewById(R.id.firstValueEditText)
        firstValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                convert()
            }
        })
        secondValueEditText = findViewById(R.id.secondValueEditText)
        currencyRateEditText = findViewById(R.id.currencyRateEditText)
        currencyRateEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                updateConverstionRatio()
                convert()
            }
        })
        firstValueSpinner = findViewById(R.id.firstValueSpinner)
        secondValueSpinner = findViewById(R.id.secondValueSpinner)
        val onItemSelectedListener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                loadConversionRatio()
                convert()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        firstValueSpinner.setOnItemSelectedListener(onItemSelectedListener)
        secondValueSpinner.setOnItemSelectedListener(onItemSelectedListener)

        loadSpinnerValues()
        loadConversionData()
    }

    private fun loadSpinnerValues() {
        val url = String.format(
            "https://data.fixer.io/api/latest?access_key=%s&base=USD&symbols=USD,EUR,RUB",
            FIXER_IO_API_KEY
        )
        val context: Context = this
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val items = ArrayList<String>()
                    val keys = response.getJSONObject("rates").keys()
                    while (keys.hasNext()) {
                        items.add(keys.next())
                    }
                    val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                            context,
                            android.R.layout.simple_spinner_item,
                            items.toTypedArray()
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    firstValueSpinner.adapter = adapter
                    secondValueSpinner.adapter = adapter
                } catch (e: JSONException) {
                    reportError("Failed to load currency rates.")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { reportError("Failed to load currency rates.") }
        )
        networkRequestQueue.add(jsonObjectRequest)
    }

    override fun onStop() {
        saveConversionData()
        super.onStop()
    }

    fun updateCurrency(view: View?) {
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from == to) {
            currencyRateEditText.setText(String.format(Locale.getDefault(), "%.2f", 1.0))
        } else {
            val url = String.format(
                "https://data.fixer.io/api/latest?access_key=%s&base=%s&symbols=USD,EUR,RUB",
                FIXER_IO_API_KEY,
                from
            )
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        currencyRateEditText.setText(String.format(
                            Locale.getDefault(),
                            "%.2f",
                            response.getJSONObject("rates").getDouble(to)
                        ))
                    } catch (e: JSONException) {
                        reportError("Failed to load currency rates.")
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { reportError("Failed to load currency rates.") }
            )
            networkRequestQueue.add(jsonObjectRequest)
        }
    }

    private fun loadConversionData() {
        var conversionRatiosFile: File?
        var inputStream: InputStream? = null
        if (getFileStreamPath(CONVERSION_RATES_FILE_NAME).also { conversionRatiosFile = it }.exists()) {
            try {
                inputStream = FileInputStream(conversionRatiosFile!!)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } else {
            inputStream = resources.openRawResource(R.raw.initial_currency_rates)
        }
        if (inputStream != null) {
            var result = ""
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var nextLine: String?
                while (bufferedReader.readLine().also { nextLine = it } != null) {
                    stringBuilder.append(nextLine)
                }
                result = stringBuilder.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            try {
                conversionRatios = JSONObject(result)
            } catch (e: JSONException) {
                reportError("Failed to load initial currency ratios.")
                e.printStackTrace()
            }
        }
    }

    private fun saveConversionData() {
        var outputStream: FileOutputStream? = null
        try {
            outputStream = openFileOutput(CONVERSION_RATES_FILE_NAME, Context.MODE_PRIVATE)
        } catch (e: FileNotFoundException) {
            reportError("Failed to save custom conversion ratios.")
            e.printStackTrace()
        }
        if (outputStream != null) {
            try {
                outputStream.write(conversionRatios.toString().toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadConversionRatio() {
        var currencyRatio = 1.0
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to) {
            val key = "$from-$to"
            currencyRatio = try {
                conversionRatios.getDouble(key)
            } catch (e: JSONException) {
                reportError("Invalid conversion ratio")
                return
            }
        }
        currencyRateEditText.setText(String.format(Locale.getDefault(), "%.2f", currencyRatio))
    }

    private fun updateConverstionRatio() {
        var ratioText = currencyRateEditText.text.toString()
        if (ratioText.trim { it <= ' ' }.isEmpty()) {
            return
        }
        ratioText = ratioText.replace(',', '.')
        val ratio: Float
        ratio = try {
            ratioText.toFloat()
        } catch (e: NumberFormatException) {
            reportError("Invalid ratio")
            return
        }
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to) {
            val key = "$from-$to"
            try {
                conversionRatios.put(key, ratio.toDouble())
            } catch (e: JSONException) {
                reportError("Failed to update the currency ratio")
            }
        }
    }

    private fun convert() {
        var firstValueText = firstValueEditText.text.toString()
        if (firstValueText.trim { it <= ' ' }.isEmpty()) {
            return
        }
        firstValueText = firstValueText.replace(',', '.')
        val fromValue: Float
        fromValue = try {
            firstValueText.toFloat()
        } catch (e: NumberFormatException) {
            reportError("Invalid first value")
            return
        }
        var currencyRatio = 1.0
        val from = firstValueSpinner.selectedItem.toString()
        val to = secondValueSpinner.selectedItem.toString()
        if (from != to) {
            val key = "$from-$to"
            currencyRatio = try {
                conversionRatios.getDouble(key)
            } catch (e: JSONException) {
                reportError("Invalid conversion ratio")
                return
            }
        }
        val result = fromValue * currencyRatio
        secondValueEditText.setText(String.format(Locale.getDefault(), "%.2f", result))
    }

    private fun reportError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}