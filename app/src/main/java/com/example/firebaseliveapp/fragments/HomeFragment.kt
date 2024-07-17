package com.example.firebaseliveapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebaseliveapp.FirebaseViewModel
import com.example.firebaseliveapp.R
import com.example.firebaseliveapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        viewModel.signOut()

        binding.loginBTN.setOnClickListener {
            val email = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()

            viewModel.login(email, password)
        }

        binding.registerBTN.setOnClickListener {
            val email = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()

            viewModel.register(email, password)
        }

        viewModel.currentUser.observe(viewLifecycleOwner){user ->

            if(user == null){
                //User ist nicht eingeloggt
                Log.d("CurrentUser", "Kein User eingeloggt")
            } else {
                //User ist eingeloggt
                Log.d("CurrentUser", user.uid)
                findNavController().navigate(R.id.profileFragment)
            }

        }
    }
}