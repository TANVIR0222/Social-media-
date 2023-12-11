package com.example.socialmedia

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.example.socialmedia.databinding.PostDailogeBoxBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    val userList: MutableList<Users> = mutableListOf()

    // post add
    val postList: MutableList<PostWithUser> = mutableListOf()

    // add adapter
    lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserAll()
        setData()

        binding.addPost.setOnClickListener {
            alertDialogBox()
        }

    }

    private fun setData() {
        database.child("Post").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (sn in snapshot.children) {

                    val post = sn.getValue(Post::class.java)

                    setUserWithPost(post!!)

//                    postList.add(post!!)
                }
                adapter = PostAdapter(postList)
                binding.PostRcv.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }




    private fun alertDialogBox() {

        val postDialog = BottomSheetDialog(requireContext())

        postDialog.setCancelable(true)
        postDialog.setCanceledOnTouchOutside(true)
        //layout setup
        val postBinding: PostDailogeBoxBinding = PostDailogeBoxBinding.inflate(layoutInflater)
        postDialog.setContentView(postBinding.root)

         val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!


                    postBinding.uploadImage.setImageURI(fileUri)



                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        postBinding.uploadImage.setOnClickListener {

            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }

        // post
        postBinding.apply {

            addPost.setOnClickListener {

                val content = caption.editText?.text.toString().trim()

                // data base user id
                val postIdUser = database.push().key
                val post = Post(mAuth.uid.toString(), content, " ", postIdUser!!)
                database.child("Post").child(postIdUser).setValue(post).addOnSuccessListener {
                    Toast.makeText(requireContext(), "upload", Toast.LENGTH_SHORT).show()
                    postDialog.dismiss()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "sorry", Toast.LENGTH_SHORT).show()
                    postDialog.dismiss()

                }


            }
        }


        postDialog.show()

    }


// add post and user id
    private fun setUserWithPost(post: Post) {
        database.child("User").child(post.authorId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {


                    val user: Users = snapshot.getValue(Users::class.java)!!
                    val postWithUser = PostWithUser(post, user)
                    // 0 used reverse post L I F S 
                    postList.add(0,postWithUser)


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

// user add database
    private fun getUserAll() {

        database.child("User").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (sn in snapshot.children) {
                    val user: Users = sn.getValue(Users::class.java)!!
                    userList.add(user)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}