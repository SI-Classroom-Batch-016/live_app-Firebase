package com.example.firebaseliveapp.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.firebaseliveapp.FirebaseViewModel
import com.example.firebaseliveapp.R
import com.example.firebaseliveapp.data.UserData
import com.example.firebaseliveapp.databinding.FragmentProfileBinding
import com.google.firebase.firestore.toObject

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    private val getContent =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadImage(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileIV.setOnClickListener {
            getContent.launch("image/*")
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                //User ist nicht eingeloggt
                findNavController().navigate(R.id.homeFragment)
            } else {
                binding.profileTV.text = user.email
            }
        }

        viewModel.userDataDocumentReference?.addSnapshotListener { value, error ->

            value?.toObject(UserData::class.java)?.let { profile ->

                binding.profileTV.text = profile.status
                if (binding.statusET.text.toString().isBlank() && profile.status.isNotBlank()) {
                    binding.statusET.setText(profile.status)
                }

                if(profile.profilePic != "") {
                    binding.profileIV.load(profile.profilePic)
                }

            }
        }

        binding.statusET.doAfterTextChanged {
            val newProfile = UserData(it.toString())
            viewModel.setProfile(newProfile)
        }

        binding.logoutBTN.setOnClickListener {
            viewModel.signOut()
        }

        binding.noteBTN.setOnClickListener {
            findNavController().navigate(R.id.notesFragment)
        }


    }

}