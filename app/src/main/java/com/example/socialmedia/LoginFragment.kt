package com.example.socialmedia

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.databinding.FragmentLoginBinding


class LoginFragment :BaseFragment<FragmentLoginBinding> (FragmentLoginBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (User != null){

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        binding.tsing.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.login.setOnClickListener {
            var email = binding.email.editText?.text.toString().trim()
            var password = binding.password.editText?.text.toString().trim()

            userLogin(email, password)
        }


    }

    private fun userLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)


        }.addOnFailureListener {

            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Error")
                .setMessage(it.message)
            alertDialog.create().show()
        }
    }
}