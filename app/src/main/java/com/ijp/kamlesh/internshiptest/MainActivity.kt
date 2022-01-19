package com.ijp.kamlesh.internshiptest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ijp.kamlesh.internshiptest.recyclev.CustomAdapter
import com.ijp.kamlesh.internshiptest.recyclev.CustomAdapterForBars
import com.ijp.kamlesh.internshiptest.recyclev.ItemsViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Info.")
        builder.setMessage("InProgress\n 1. Delete Operation\n 2. bar width")
        builder.setIcon(android.R.drawable.btn_star)

        builder.setPositiveButton("close :)"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("Close"){dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()

        val data = mutableListOf<ItemsViewModel>()
        data.add(ItemsViewModel(1,100,Color.rgb(255,0,0)))

        val barAdapter=CustomAdapterForBars(data)
        barPreviewRecView.layoutManager = LinearLayoutManager(this)
        barPreviewRecView.adapter=barAdapter

        val adapter = CustomAdapter(data,object :CustomAdapter.ItemchangeListenerCustom{
            override fun itemChanged() {

                barAdapter.notifyDataSetChanged()

            }

        })

        seekbarRecView.layoutManager = LinearLayoutManager(this)
        seekbarRecView.adapter = adapter

    }
}