package com.example.firebaseliveapp.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseliveapp.data.Note
import com.example.firebaseliveapp.databinding.NoteItemBinding

class NoteAdapter(
    private val dataSet: List<Note>,
): RecyclerView.Adapter<NoteAdapter.ItemViewHolder>() {


    class ItemViewHolder(val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]

        holder.binding.textView.animate()

    }

}