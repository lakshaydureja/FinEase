package com.app.finease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.finease.databinding.ActivityWorkBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Work : AppCompatActivity() {

    private lateinit var binding : ActivityWorkBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var number: String
    private lateinit var workArrayList : ArrayList<talentContent>
    private lateinit var databaseL: DatabaseReference

    private lateinit var workContentList2 : ArrayList<workContent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)



        databaseL = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Work")

        workContentList2 = ArrayList()


        val array1 = mutableListOf<Int>()
        val array2 = mutableListOf<String>()
        val array3 = mutableListOf<String>()
        val array4 = mutableListOf<String>()
        val array5 = mutableListOf<String>()
        val array6 = mutableListOf<String>()



        databaseL.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through each child node in the data
                for (child in snapshot.children) {
                    // Get the values of each parameter and add them to the arrays
                    array1.add(child.child("imageId").value.toString().toInt())
                    array2.add(child.child("title").value.toString())
                    array3.add(child.child("desc").value.toString())
                    array4.add(child.child("name").value.toString())
                    array5.add(child.child("fee").value.toString())
                    array6.add(child.child("date").value.toString())
                }
                // Do something with the arrays (e.g. print them)
                println("Array 1: $array1")
                println("Array 2: $array2")
                println("Array 3: $array3")
                println("Array 4: $array4")
                println("Array 5: $array5")
                println("Array 6: $array6")


//               val talentContentList = ArrayList<talentContent>()

                for (i in array1.indices) {
                    val talentContent = workContent(
                        array1[i],
                        array2[i],
                        array4[i],
                        array3[i],
                        array5[i],
                        array6[i]
                    )

                    workContentList2.add(talentContent)

                }

// Print the talentContentList
//               println(talentContentList2)
                binding.listview.adapter = ListingAdapter2(this@Work,workContentList2);


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error (e.g. print an error message)
                println("Failed to retrieve data: ${error.message}")
            }
        })














        binding.button9.setOnClickListener{
            val intent= Intent(this,workPost::class.java)
            startActivity(intent)
        }



//       workArrayList = ArrayList()
//        for(i in name.indices){
//            val TalContent = talentContent(imageId[i],Title[i],name[i],fee[i],date[i],desc[i])
//            workArrayList.add(TalContent)
//        }

        binding.listview.isClickable=true
//        binding.listview.adapter = ListingAdapter(this,workArrayList);
        binding.listview.setOnItemClickListener { parent, view, position, id ->

            val Title =array2[position]
            val name=array4[position]
            val fee=array5[position]
            val date=array6[position]
            val desc=array3[position]
            val imageId = array1[position]


            val i =Intent(this,workClick::class.java)
            i.putExtra("imageId",imageId)
            i.putExtra("Title",Title)
            i.putExtra("name",name)
            i.putExtra("fee",fee)
            i.putExtra("date",date)
            i.putExtra("desc",desc)
            startActivity(i)

        }











        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null) {

            number = user.phoneNumber.toString()
            number = number.drop(3);

        }

        toolbar=binding.topAppBar
        drawerLayout=binding.drawerLayout
        navigationView=binding.navigationView


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



            navigationView.findViewById<TextView>(R.id.name).text = name.toString().replaceFirstChar { it.uppercase() }
            navigationView.findViewById<TextView>(R.id.username).text = phoneNo.toString()


        }.addOnFailureListener {

            Toast.makeText(applicationContext, "error while fetching name", Toast.LENGTH_LONG)
                .show()

        }





    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}