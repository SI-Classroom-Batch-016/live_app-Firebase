package com.example.firebaseliveapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.firebaseliveapp.FirebaseViewModel
import com.example.firebaseliveapp.data.Note
import com.example.firebaseliveapp.databinding.FragmentNotesBinding
import com.example.firebaseliveapp.fragments.adapter.NoteAdapter


class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding

    private val viewModel: FirebaseViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addFAB.setOnClickListener {
            val testNote = Note(
                title = "Title",
                content = "Bla"
            )
            viewModel.addNote(testNote)
        }

        viewModel.notesCollectionReference.addSnapshotListener { value, error ->

            val noteList : MutableList<Note> =  mutableListOf()

            value?.documents?.forEach {
                val note = it.toObject(Note::class.java)!!
                noteList.add(note)
            }

            val adapter = NoteAdapter(noteList)
            binding.notesRV.adapter = adapter



        }
    }


}