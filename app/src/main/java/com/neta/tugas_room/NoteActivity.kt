package com.neta.tugas_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.ArrayAdapter
import com.neta.tugas_room.database.Note
import com.neta.tugas_room.database.NoteDao
import com.neta.tugas_room.database.NoteRoomDatabase
import com.neta.tugas_room.databinding.ActivityNoteBinding
import com.neta.tugas_room.databinding.ActivityNoteBinding.inflate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var NoteAdapter: NoteAdapter
    private var updateId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.nodeDao()!!


        NoteAdapter = NoteAdapter(emptyList()) { selectedNote ->

        }

        with(binding){
            val intentToMainActivity = Intent(this@NoteActivity, MainActivity::class.java)
            noteButton.setOnClickListener(View.OnClickListener {
                insert(
                    Note(
                        title = titleNoteInput.text.toString(),
                        description = titleDetailInput.text.toString(),
                        date = dateDetailInput.text.toString()
                    )
                )
                resetForm()
                startActivity(intentToMainActivity)
            })
            }
    }

    private fun getNotes(){
        mNoteDao.allNotes.observe(this) { notes ->
            NoteAdapter.setData(notes)
        }
    }

    private fun insert(note:Note){
        executorService.execute{mNoteDao.insert(note)}
    }

    private fun update(note:Note){
        executorService.execute{mNoteDao.update(note)}
    }

    private fun delete(note:Note){
        executorService.execute{mNoteDao.delete(note)}
    }

    override fun onResume() {
        super.onResume()
        getNotes()
    }

    private fun resetForm(){
        with(binding){
            titleNoteInput.setText("")
            titleDetailInput.setText("")
            dateDetailInput.setText("")
        }
    }
}