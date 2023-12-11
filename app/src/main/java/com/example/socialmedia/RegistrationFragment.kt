package com.example.socialmedia

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.socialmedia.databinding.FragmentRegistrationBinding

class RegistrationFragment :
    BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tsing.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registration.setOnClickListener {
            val name = binding.name.editText?.text.toString().trim()
            val email = binding.Semail.editText?.text.toString().trim()
            val password = binding.Spassword.editText?.text.toString().trim()

            userRegistration(name, email, password)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun userRegistration(name: String, email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
// add data fire base storage real time data base
                val user = Users(
                    name = name,
                    email = email,
                    password = password,
                    profileImage = " ",
                    userId = result.user!!.uid
                )
                // folder name User
                database.child("User").child(user.userId).setValue(user).addOnSuccessListener {
                    mAuth.signOut()
                    findNavController().popBackStack()


                }.addOnFailureListener { error ->

                    val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Error")
                        .setMessage(error.message)
                    alertDialog.create().show()

                }


            }.addOnFailureListener { error ->
                val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Error")
                    .setMessage(error.message)
                alertDialog.create().show()
            }

    }
}