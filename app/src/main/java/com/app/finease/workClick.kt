package com.app.finease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.finease.databinding.ActivityWorkClickBinding
import com.app.finease.databinding.ActivityWorkPostBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class workClick : AppCompatActivity() {

    private lateinit var binding: ActivityWorkClickBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var number: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_click)

        binding = ActivityWorkClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageId = intent.getIntExtra("imageId",R.drawable.avatar)
        val Title = intent.getStringExtra("Title")
        val name = intent.getStringExtra("name")
        val fee = intent.getStringExtra("fee")
        val date = intent.getStringExtra("date")
        val desc = intent.getStringExtra("desc")

//        binding.profilepic.setImageResource(imageId)
    //    binding.profilepic.setImageResource( R.drawable.avatar)

        binding.title.text=Title
        binding.name.text=name
        binding.fee.text="₹"+fee
        binding.date.text=date
        binding.desc.text=desc

        when(imageId) {
            1 -> binding.profilepic.setImageResource(R.drawable.av1)
            2 ->binding.profilepic.setImageResource(R.drawable.av2)
            3 -> binding.profilepic.setImageResource(R.drawable.av3)
            4 -> binding.profilepic.setImageResource(R.drawable.av4)
            5 -> binding.profilepic.setImageResource(R.drawable.av5)
            6 -> binding.profilepic.setImageResource(R.drawable.av6)
            7 -> binding.profilepic.setImageResource(R.drawable.av7)
            8 ->binding.profilepic.setImageResource(R.drawable.av8)
            9 -> binding.profilepic.setImageResource(R.drawable.av9)
            10 -> binding.profilepic.setImageResource(R.drawable.av10)
            11->binding.profilepic.setImageResource(R.drawable.av11)
            12-> binding.profilepic.setImageResource(R.drawable.av12)
            13-> binding.profilepic.setImageResource(R.drawable.av13)
            else ->        binding.profilepic.setImageResource(R.drawable.avatar)

        }



        binding.button9.setOnClickListener{
            val intent= Intent(this,Work::class.java)
            startActivity(intent)
        }


        //        navigation + auth

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {

            number = user.phoneNumber.toString()
            number = number.drop(3);

        }


        toolbar = binding.topAppBar
        drawerLayout = binding.drawer2
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