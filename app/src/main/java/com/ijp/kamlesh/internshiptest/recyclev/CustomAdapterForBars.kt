package com.ijp.kamlesh.internshiptest.recyclev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ijp.kamlesh.internshiptest.R
import com.ijp.kamlesh.internshiptest.utils.Companion.getRandomColor
import kotlinx.android.synthetic.main.preview_bars_components.view.*
import kotlinx.android.synthetic.main.seekbar_component.view.*

class CustomAdapterForBars(private val mList: MutableList<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapterForBars.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_bars_components, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dynaPos=holder.adapterPosition
//        val perc=mList[dynaPos].end*.01
        val param:LinearLayout.LayoutParams= LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            ((0..100).random()*.01).toFloat()
        )
        holder.singleBar.setBackgroundColor(mList[dynaPos].color)
        holder.singleBar.layoutParams=param


    }
    override fun getItemCount(): Int {
        return mList.size
    }
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val singleBar: LinearLayout =itemView.singleBar
    }
}
