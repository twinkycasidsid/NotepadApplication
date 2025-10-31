package com.example.notepadapplication

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dbHelper = NoteHelper(this)

        loadNotes()

        val addBtn = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnAddNote)
        addBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun loadNotes() {
        val notes = dbHelper.getAllNotes()
        val adapter = NoteAdapter(notes)
        recyclerView.adapter = adapter
    }



}