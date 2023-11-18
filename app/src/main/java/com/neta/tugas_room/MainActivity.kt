package com.neta.tugas_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.neta.tugas_room.database.Note
import com.neta.tugas_room.database.NoteDao
import com.neta.tugas_room.database.NoteRoomDatabase
import com.neta.tugas_room.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        noteAdapter = NoteAdapter(emptyList()) { selectedNote ->
        }

        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.nodeDao()!!

        with(binding) {

            val intentToNoteActivity = Intent(this@MainActivity, NoteActivity::class.java)

            buttonAdd.setOnClickListener(View.OnClickListener {
                startActivity(intentToNoteActivity)
            })

            rvNote.layoutManager = GridLayoutManager(this@MainActivity,1)
            rvNote.adapter = noteAdapter
            getNotes()

            noteAdapter.onClickNote = { clickedNote ->
                delete(clickedNote)
            }

        }

    }
    private fun getNotes() {
        mNoteDao.allNotes.observe(this) { notes ->
            noteAdapter.setData(notes)
            noteAdapter.notifyDataSetChanged()
        }
    }

    private fun delete(note: Note) {
        executorService.execute { mNoteDao.delete(note) }
    }

}