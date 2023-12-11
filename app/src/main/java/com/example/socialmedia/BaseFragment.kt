package com.example.socialmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFragment<vB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> vB


) : Fragment() {

    private var _binding: vB? = null

    val binding: vB
        get() = _binding as vB

    lateinit var mAuth: FirebaseAuth
    var User: FirebaseUser? = null
    lateinit var database: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = bindingInflater.invoke(inflater)

        mAuth= FirebaseAuth.getInstance()
        User=  FirebaseAuth.getInstance().currentUser
        database= FirebaseDatabase.getInstance().reference


        return binding.root


    }

}
