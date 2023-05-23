package com.app.finease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.finease.databinding.ActivityAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth : AppCompatActivity() {

    private lateinit var binding :ActivityAuthBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Auth
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()



        binding.buttonLogin.setOnClickListener{
            val intent= Intent(this,login::class.java)
            startActivity(intent)
        }


        binding.buttonSignup.setOnClickListener{
            val intent= Intent(this,Signup::class.java)
            startActivity(intent)
        }



    }



    override fun onStart() {
        super.onStart()
        if(auth.currentUser!= null){
            startActivity(Intent(this@Auth,MainActivity::class.java))
        }
    }
}