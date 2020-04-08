package com.toksaitov.doodler

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.toksaitov.doodler.data.Doodle
import com.toksaitov.doodler.data.DoodleViewModel
import java.io.File

class DoodleActivity : AppCompatActivity() {

    private lateinit var doodleView : DoodleView
    private var doodlePath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doodle)

        doodlePath = intent.getStringExtra("doodlePath")
        doodleView = findViewById(R.id.doodleView)
        if (doodlePath != null) {
            doodleView.loadDoodle(doodlePath!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.doodle_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val inflater = layoutInflater

        return when (item.itemId) {
            R.id.cancelMenuItem -> {
                finish()
                true
            }
            R.id.saveMenuItem -> {
                if (doodlePath == null) {
                    val view = inflater.inflate(R.layout.name_dialog, null)
                    val nameEditText = view.findViewById<EditText>(R.id.nameEditText)

                    AlertDialog.Builder(this)
                        .setTitle("Save changes?")
                        .setMessage("Choose the name of the doodle")
                        .setView(view)
                        .setPositiveButton(getString(R.string.confirmationDialogPositiveLabel)) { dialog, button ->
                            val id = System.currentTimeMillis().toInt()
                            val name = nameEditText.text.toString()

                            val doodleDir = getDir("doodles", Context.MODE_PRIVATE)
                            doodlePath = "${doodleDir.absoluteFile}${File.separator}${id}.webp"
                            File(doodlePath!!).createNewFile()

                            val doodle = Doodle(id, name, doodlePath!!)
                            ViewModelProvider(this).get(DoodleViewModel::class.java).insert(doodle)

                            doodleView.saveDoodle(doodlePath!!)
                            finish()
                        }
                        .setNegativeButton(getString(R.string.confirmationDialogNegativeLabel), null)
                        .show()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmationDialogTitle))
            .setMessage(getString(R.string.confirmationDialogMessage))
            .setPositiveButton(getString(R.string.confirmationDialogPositiveLabel)) { dialog, button -> finish() }
            .setNegativeButton(getString(R.string.confirmationDialogNegativeLabel), null)
            .show()
    }
}
