package com.example.notepadapplication

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.message.text = note.message

        // Edit Button
        holder.editBtn.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("note_id", note.id)
            intent.putExtra("note_title", note.title)
            intent.putExtra("note_message", note.message)
            context.startActivity(intent)
        }

        // Delete Button
        holder.deleteBtn.setOnClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    val dbHelper = NoteHelper(context)
                    dbHelper.deleteNote(note.id)
                    if (context is ListActivity) {
                        context.loadNotes()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int = notes.size

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtCardTitle)
        val message: TextView = itemView.findViewById(R.id.txtCardMessage)
        val editBtn: Button = itemView.findViewById(R.id.btnEdit)
        val deleteBtn: Button = itemView.findViewById(R.id.btnDelete)
    }
}
