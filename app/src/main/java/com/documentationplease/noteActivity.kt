package com.documentationplease

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Discouraged
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class noteActivity : AppCompatActivity() {
    private lateinit var db: db

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note)

        val note = findViewById<AppCompatEditText>(R.id.note)
        val delete_note = findViewById<ShapeableImageView>(R.id.delete_note)

        db = db(this)

        note.setText(db.catch(arrayOf("note")))

        fun note_values (text: String): ContentValues {
            return ContentValues().apply {
                put("note", text)
            }
        }

        delete_note.setOnClickListener {
            db.update(note_values(""))
            note.setText("")
        }


        note.addTextChangedListener {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(500.milliseconds)
                db.update(note_values(note.text.toString()))
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}