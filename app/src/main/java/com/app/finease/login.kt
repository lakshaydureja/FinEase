package com.app.finease

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.finease.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Auth
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()


binding.textView2.setOnClickListener{
           val intent=Intent(this,Signup::class.java)
           startActivity(intent)
}




        binding.button2.setOnClickListener {
//            val intent= Intent(this,otp::class.java)
//            startActivity(intent)
           number=binding.in1.text.toString().trim()
//            number = binding.editTextPhone.text.trim().toString()
            if (number.isNotEmpty()) {

                database = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")
                database.child(number).get().addOnSuccessListener {

                    if (it.exists()) {
                        if (number.length == 10) {
                            number = "+91$number"

                            val options = PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(number)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                                .build()
                            PhoneAuthProvider.verifyPhoneNumber(options)

                        } else {
                            Toast.makeText(this, "Please enter correct number", Toast.LENGTH_SHORT)
                                .show()
                        }
//                        val firstname = it.child("firstName").value
//                        val lastName = it.child("lastName").value
//                        val age = it.child("age").value
//                        Toast.makeText(this,"Successfuly Read",Toast.LENGTH_SHORT).show()
//                        binding.etusername.text.clear()
//                        binding.tvFirstName.text = firstname.toString()
//                        binding.tvLastName.text = lastName.toString()
//                        binding.tvAge.text = age.toString()

                    } else {

                        Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()


                    }

                }.addOnFailureListener {

                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()


                }


            } else {

                Toast.makeText(this, "PLease enter the Number", Toast.LENGTH_SHORT).show()

            }



//            if (number.isNotEmpty()) {
//                if (number.length == 10) {
//                    number = "+91$number"
//
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//
//                } else {
//                    Toast.makeText(this, "Please enter correct number", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "please enter Number", Toast.LENGTH_SHORT).show()
//            }


        }


    }


    override fun onBackPressed() {
        finishAffinity()
    }





        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Authentication successfull", Toast.LENGTH_SHORT)
                            .show()
                        sendToMain()
//                    val user = task.result?.user
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.d(
                            ContentValues.TAG,
                            "signInWithPhoneAuthCredential: ${task.exception.toString()}"
                        )
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
        }

        private fun sendToMain() {
            startActivity(Intent(this, MainActivity::class.java))
        }


        private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later

                val intent = Intent(this@login, otp::class.java)
                intent.putExtra("OTP", verificationId)
                intent.putExtra("resendToken", token)
                intent.putExtra("phoneNumber", number)
                startActivity(intent)
            }
        }


    }

