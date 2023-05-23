package com.app.finease

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListingAdapter2 (private val context : Activity, private val arrayList :ArrayList<workContent>) : ArrayAdapter<workContent>(
    context,R.layout.list_item,arrayList
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item,null)

        val catPicTal : ImageView =view.findViewById(R.id.category_picTal)
        val titleTal: TextView = view.findViewById(R.id.titleTal)
        val nameTal: TextView = view.findViewById(R.id.nameTal)
        val feeTal: TextView = view.findViewById(R.id.feeTal)
        val dateTal: TextView = view.findViewById(R.id.dateTal)


    //    catPicTal.setImageResource(R.drawable.avatar)
      //  catPicTal.setImageResource(arrayList[position].imageId)
        titleTal.text = arrayList[position].Title
        val desc = arrayList[position].desc
        nameTal.text=arrayList[position].name
        feeTal.text="₹"+arrayList[position].fee
        dateTal.text=arrayList[position].date

        when(arrayList[position].imageId) {
            1 -> catPicTal.setImageResource(R.drawable.av1)
            2 ->catPicTal.setImageResource(R.drawable.av2)
            3 -> catPicTal.setImageResource(R.drawable.av3)
            4 -> catPicTal.setImageResource(R.drawable.av4)
            5 -> catPicTal.setImageResource(R.drawable.av5)
            6 -> catPicTal.setImageResource(R.drawable.av6)
            7 -> catPicTal.setImageResource(R.drawable.av7)
            8 ->catPicTal.setImageResource(R.drawable.av8)
            9 -> catPicTal.setImageResource(R.drawable.av9)
            10 -> catPicTal.setImageResource(R.drawable.av10)
            11->catPicTal.setImageResource(R.drawable.av11)
            12-> catPicTal.setImageResource(R.drawable.av12)
            13-> catPicTal.setImageResource(R.drawable.av13)
            else ->        catPicTal.setImageResource(R.drawable.avatar)

        }

        return view
    }

}