package com.app.finease


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.finease.databinding.ActivitySignupBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)





        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //        binding.button.setOnClickListener{
//          val intent= Intent(this,otp::class.java)
//          startActivity(intent)
//        }

        // Initialize Firebase Auth
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()




        database = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("User")


        binding.button.setOnClickListener {

            val name = binding.ti1.text.toString()
            val eMail = binding.ti2.text.toString()
            val phoneNo = binding.ti3.text.toString()

//            val name = binding.editTextTextPersonName.text.toString()
//            val eMail = binding.editTextTextEmailAddress.text.toString()
//            val phoneNo = binding.editTextPhone.text.toString()


            val user= User(name,eMail,phoneNo)
            database.child(phoneNo).setValue(user).addOnSuccessListener {

            }.addOnFailureListener{
                Toast.makeText(this,"Database Error",Toast.LENGTH_SHORT).show()
            }







            number = binding.ti3.text.toString().trim()
            if (number.isNotEmpty()) {
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
                    Toast.makeText(this, "Please enter correct number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "please enter Number", Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onBackPressed() {
        val intent = Intent(this, Auth::class.java)
        startActivity(intent)
        finish()
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authentication successfull", Toast.LENGTH_SHORT).show()
                    sendToMain()
//                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(TAG, "signInWithPhoneAuthCredential: ${task.exception.toString()}")
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

            val intent = Intent(this@Signup, otp::class.java)
            intent.putExtra("OTP", verificationId)
            intent.putExtra("resendToken", token)
            intent.putExtra("phoneNumber", number)
            startActivity(intent)
        }
    }


//    override fun onStart() {
//        super.onStart()
//        if(auth.currentUser!= null){
//            startActivity(Intent(this@Signup,MainActivity::class.java))
//        }
//    }
}