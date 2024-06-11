package com.example.firebaseliveapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebaseliveapp.data.UserData
import com.example.firebaseliveapp.databinding.FragmentHomeBinding
import com.example.firebaseliveapp.databinding.FragmentProfileBinding
import com.google.firebase.firestore.toObject

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel: FirebaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser.observe(viewLifecycleOwner) {user ->
            if(user == null){
                //User ist nicht eingeloggt
                findNavController().navigate(R.id.homeFragment)
            } else {
                binding.profileTV.text = user.email
            }
        }

        viewModel.userDataDocumentReference?.addSnapshotListener { value, error ->
            val profile = value?.toObject<UserData>()
            binding.profileTV.text = profile?.status
            binding.statusET.setText(profile?.status)
        }

        binding.statusET.doAfterTextChanged {
            val newProfile = UserData(it.toString())
            viewModel.setProfile(newProfile)
        }

        binding.logoutBTN.setOnClickListener {
            viewModel.signOut()
        }
    }
}