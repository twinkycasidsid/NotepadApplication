package com.example.notepadapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Notes_DB"
        private const val DATABASE_VERSION = 2 // bump version to force recreate
        const val TABLE_NAME = "notes"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_NOTE = "note"
        const val COL_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_NOTE TEXT,
                $COL_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(title: String, message: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_NOTE, message)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COL_CREATED_AT DESC",
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COL_ID))
                    val title = it.getString(it.getColumnIndexOrThrow(COL_TITLE))
                    val message = it.getString(it.getColumnIndexOrThrow(COL_NOTE))
                    val createdAt = it.getString(it.getColumnIndexOrThrow(COL_CREATED_AT))
                    notes.add(Note(id, title, message, createdAt))
                } while (it.moveToNext())
            }
        }
        db.close()
        return notes
    }

    fun updateNote(id: Int, title: String, message: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, title)
            put(COL_NOTE, message)
        }
        val result = db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun deleteNote(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
