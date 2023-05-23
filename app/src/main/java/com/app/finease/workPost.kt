package com.app.finease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.finease.databinding.ActivityTalentPostBinding
import com.app.finease.databinding.ActivityWorkPostBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class workPost : AppCompatActivity() {


    private lateinit var binding: ActivityWorkPostBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var number: String


    private lateinit var title:String
    private lateinit var desc:String
    private lateinit var name:String
    private lateinit var fee:String
    private lateinit var date:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_post)
        binding = ActivityWorkPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val randomNum = (1..13).random()
        var imageId = randomNum


        binding.button9.setOnClickListener{


            title = binding.talInp1b.text.toString()
            desc = binding.talInp2b.text.toString()
            name = binding.talInp3b.text.toString()
            fee = binding.talInp4b.text.toString()
            date = binding.talInp5b.text.toString()

            database = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Work")
            val tal= workContent(imageId,title,name,desc,fee,date)
            database.child(title).setValue(tal).addOnSuccessListener {

                val intent= Intent(this,Work::class.java)
                startActivity(intent)

            }.addOnFailureListener{
                Toast.makeText(this,"Database Error",Toast.LENGTH_SHORT).show()
            }






        }













        //        navigation + auth

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {

            number = user.phoneNumber.toString()
            number = number.drop(3);

        }


        toolbar = binding.topAppBar
        drawerLayout = binding.drawerlayout2
        navigationView = binding.navigationView


        toolbar.setNavigationOnClickListener { view ->
            drawerLayout.openDrawer(GravityCompat.START)

        }



        navigationView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            drawerLayout.closeDrawer(GravityCompat.START)
            when (id) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_message -> {
                    val intent = Intent(this, talent::class.java)
                    startActivity(intent)
                    true
                }

                R.id.settings -> {
                    val intent = Intent(this, Work::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_login -> {
                    auth.signOut()
                    startActivity(Intent(this, Auth::class.java))
                    true

                }
                R.id.nav_share -> {
                    Toast.makeText(this, "Share is clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_rate -> {
                    Toast.makeText(this, "Rate us is clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }


        //        write code from here + change database path
        database =
            FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("User")
        database.child(number).get().addOnSuccessListener {

            val name = it.child("name").value
            val email = it.child("email").value
            val phoneNo = it.child("phoneNo").value



            navigationView.findViewById<TextView>(R.id.name).text =
                name.toString().replaceFirstChar { it.uppercase() }
            navigationView.findViewById<TextView>(R.id.username).text = phoneNo.toString()


        }.addOnFailureListener {

            Toast.makeText(applicationContext, "error while fetching name", Toast.LENGTH_LONG)
                .show()

        }




    }
    override fun onBackPressed() {
        val intent = Intent(this, Work::class.java)
        startActivity(intent)
        finish()
    }
}