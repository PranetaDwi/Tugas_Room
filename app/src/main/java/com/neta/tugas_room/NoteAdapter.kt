package com.neta.tugas_room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neta.tugas_room.database.Note
import com.neta.tugas_room.databinding.ItemNoteBinding

class NoteAdapter (private var listNote: List<Note>, var
onClickNote: (Note)-> Unit): RecyclerView.Adapter<NoteAdapter.ItemNoteViewHolder>(){


    inner class ItemNoteViewHolder(private val binding: ItemNoteBinding):
            RecyclerView.ViewHolder(binding.root){

                init {
                    itemView.setOnLongClickListener {
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            val clickedNote = listNote[position]
                            onClickNote(clickedNote)
                            true
                        } else {
                            false
                        }
                    }
                }
                fun bind(data: Note){
                    with(binding){
                        txtTitle.text = data.title
                        txtDetail.text = data.description
                        txtDate.text = data.date
                    }
                }
            }

    fun setData(newData: List<Note>) {
        listNote = newData
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNoteViewHolder {
        val binding = ItemNoteBinding.inflate(
        LayoutInflater.from(
            parent.context
        ), parent, false
        )
        return ItemNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemNoteViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

    override fun getItemCount(): Int = listNote.size

}