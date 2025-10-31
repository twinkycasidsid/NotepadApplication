package com.example.notepadapplication


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var txtTitle: TextInputEditText
    private lateinit var txtMessage: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnView: Button
    private lateinit var dbHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        txtTitle = findViewById(R.id.txtTitle)
        txtMessage = findViewById(R.id.txtMessage)
        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)
        dbHelper = NoteHelper(this)

        val noteId = intent.getIntExtra("note_id", -1)
        if (noteId != -1) {
            txtTitle.setText(intent.getStringExtra("note_title"))
            txtMessage.setText(intent.getStringExtra("note_message"))
            btnSave.text = "Update"
        }

        btnSave.setOnClickListener {
            val title = txtTitle.text.toString().trim()
            val message = txtMessage.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (noteId == -1) {
                // New note
                val result = dbHelper.insertNote(title, message)
                Toast.makeText(this,
                    if (result != -1L) "Note saved successfully" else "Error saving note",
                    Toast.LENGTH_SHORT).show()
            } else {
                // Update existing
                val result = dbHelper.updateNote(noteId, title, message)
                Toast.makeText(this,
                    if (result > 0) "Note updated successfully" else "Error updating note",
                    Toast.LENGTH_SHORT).show()
            }

            txtTitle.text?.clear()
            txtMessage.text?.clear()
            Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }

        btnView.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }
    }
}