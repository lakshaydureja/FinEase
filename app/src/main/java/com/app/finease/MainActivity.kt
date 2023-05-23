package com.app.finease

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.app.finease.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.security.AccessController.getContext
import java.util.Objects


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference


    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    private lateinit var number: String
    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    lateinit var option: Spinner

    private lateinit var databaseL: DatabaseReference
    private lateinit var talentContentList2 : ArrayList<talentContent>


    private lateinit var databaseL2: DatabaseReference
    private lateinit var workContentList2 : ArrayList<workContent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        toolbar = binding.topAppBar
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView



//talent list 5 start
        databaseL = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Talent")



        talentContentList2 = ArrayList()


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
                for (i in array1.indices) {
                    if (i == 5) {
                        break
                    }
                    val talentContent = talentContent(
                        array1[i],
                        array2[i],
                        array4[i],
                        array5[i],
                        array6[i],
                        array3[i]
                    )

                    talentContentList2.add(talentContent)

                }


                binding.listview.adapter = ListingAdapter(this@MainActivity,talentContentList2);
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error (e.g. print an error message)
                println("Failed to retrieve data: ${error.message}")
            }
        })



        binding.listview.isClickable=true
//      binding.listview.adapter = ListingAdapter(this,talentArrayList);
        binding.listview.setOnItemClickListener { parent, view, position, id ->

            val Title =array2[position]
            val name=array4[position]
            val fee=array5[position]
            val date=array6[position]
            val desc=array3[position]
            val imageId = array1[position]

            val i =Intent(this,talentClick::class.java)
            i.putExtra("imageId",imageId)
            i.putExtra("Title",Title)
            i.putExtra("name",name)
            i.putExtra("fee",fee)
            i.putExtra("date",date)
            i.putExtra("desc",desc)
            startActivity(i)

        }

//talent list 5 end

//work list 5 start
        databaseL2 = FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Work")

        workContentList2 = ArrayList()


        val array1w = mutableListOf<Int>()
        val array2w = mutableListOf<String>()
        val array3w = mutableListOf<String>()
        val array4w = mutableListOf<String>()
        val array5w = mutableListOf<String>()
        val array6w = mutableListOf<String>()



        databaseL2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Loop through each child node in the data
                for (child in snapshot.children) {
                    // Get the values of each parameter and add them to the arrays
                    array1w.add(child.child("imageId").value.toString().toInt())
                    array2w.add(child.child("title").value.toString())
                    array3w.add(child.child("desc").value.toString())
                    array4w.add(child.child("name").value.toString())
                    array5w.add(child.child("fee").value.toString())
                    array6w.add(child.child("date").value.toString())
                }

//
                for (i in array1w.indices) {
                    if (i == 5) {
                        break
                    }

                    val talentContentw = workContent(
                        array1w[i],
                        array2w[i],
                        array4w[i],
                        array3w[i],
                        array5w[i],
                        array6w[i]
                    )

                    workContentList2.add(talentContentw)

                }



                binding.listview2.adapter = ListingAdapter2(this@MainActivity,workContentList2);


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error (e.g. print an error message)
                println("Failed to retrieve data: ${error.message}")
            }
        })

        binding.listview2.isClickable=true
        binding.listview2.setOnItemClickListener { parent, view, position, id ->

            val Title =array2w[position]
            val name=array4w[position]
            val fee=array5w[position]
            val date=array6w[position]
            val desc=array3w[position]
            val imageId = array1w[position]


            val i =Intent(this,workClick::class.java)
            i.putExtra("imageId",imageId)
            i.putExtra("Title",Title)
            i.putExtra("name",name)
            i.putExtra("fee",fee)
            i.putExtra("date",date)
            i.putExtra("desc",desc)
            startActivity(i)

        }
//work list 5 end







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





        option = binding.spinner2 as Spinner


        val options = arrayOf("Choose your location", "Delhi", "Sonipat", "Gurugram", "Faridabad")
        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // on select
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //on not select
            }

        }




        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        if (user != null) {

            number = user.phoneNumber.toString()
            number = number.drop(3);

        }



        database =
            FirebaseDatabase.getInstance("https://finease-188dc-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("User")
        database.child(number).get().addOnSuccessListener {

            val name = it.child("name").value
            val email = it.child("email").value
            val phoneNo = it.child("phoneNo").value

            binding.textView8.text = name.toString().replaceFirstChar { it.uppercase() } + ","


            navigationView.findViewById<TextView>(R.id.name).text =
                name.toString().replaceFirstChar { it.uppercase() }
            navigationView.findViewById<TextView>(R.id.username).text = phoneNo.toString()


        }.addOnFailureListener {
            binding.textView8.text = "database error 97"

            Toast.makeText(applicationContext, "error while fetching name", Toast.LENGTH_LONG)
                .show()

        }















        binding.button5.setOnClickListener {
            val intent = Intent(this, Work::class.java)
            startActivity(intent)
        }


        binding.button4.setOnClickListener {
            val intent = Intent(this, talent::class.java)
            startActivity(intent)
        }


//       signOutBtn = binding.signOutBtn
//
//        signOutBtn.setOnClickListener {
//            auth.signOut()
//            startActivity(Intent(this, Auth::class.java))
//        }


    }


    override fun onBackPressed() {
        finishAffinity()
    }

}
