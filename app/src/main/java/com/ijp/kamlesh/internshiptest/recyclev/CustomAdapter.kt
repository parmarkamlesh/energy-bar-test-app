package com.ijp.kamlesh.internshiptest.recyclev

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ijp.kamlesh.internshiptest.R
import com.ijp.kamlesh.internshiptest.utils.Companion.getRandomColor
import kotlinx.android.synthetic.main.seekbar_component.view.*

class CustomAdapter(private val mList: MutableList<ItemsViewModel>,private val listner:ItemchangeListenerCustom) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var itemchangeListenerCustom: ItemchangeListenerCustom? = null
    interface ItemchangeListenerCustom{
        fun itemChanged()
    }
    init{
        itemchangeListenerCustom=listner
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seekbar_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dynaPos=holder.adapterPosition
        val ItemsViewModel = mList[dynaPos]

        holder.startValueButton.text="${ItemsViewModel.start}"
        holder.endValueButton.text="${ItemsViewModel.end}"
        holder.startValueButton.setBackgroundColor(ItemsViewModel.color)
        holder.endValueButton.setBackgroundColor(ItemsViewModel.color)

        holder.seekbar.min=ItemsViewModel.start
        holder.seekbar.max=ItemsViewModel.end

        holder.seekbar.progress=ItemsViewModel.end
        //
        holder.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    holder.endValueButton.text=progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    holder.startValueButton.text="X"
                    holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete,0,0,0)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val actualpos=holder.adapterPosition

                    if(seekBar.progress==1 && actualpos==0){

                        mList.clear()
                        mList.add(ItemsViewModel(1,100, Color.rgb(255,0,0)))
                        notifyDataSetChanged()
                        itemchangeListenerCustom?.itemChanged()
                        return

                    }

                    if(seekBar.progress==ItemsViewModel.start && mList.size>1){

                        mList.removeAt(actualpos)
                        notifyItemRemoved(actualpos)

                    }

                    holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    holder.startValueButton.text=seekBar.progress.toString()

                    seekBar.progress.also { seekBar.max = it }

                    mList[dynaPos] = ItemsViewModel(ItemsViewModel.start,seekBar.progress,ItemsViewModel.color)

                    val nextMin=if(mList.size>dynaPos+1){
                        mList[dynaPos+1].start-1
                    }else{
                        100
                    }

                    mList.add(dynaPos+1,ItemsViewModel(seekBar.progress+1,nextMin,getRandomColor()))

                    holder.endValueButton.text=seekBar.max.toString()

                    notifyDataSetChanged()
                    itemchangeListenerCustom?.itemChanged()

                }

            })
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val startValueButton: Button =itemView.startval
        val endValueButton: Button=itemView.endval
        val seekbar: SeekBar=itemView.percentageSeekbar

    }
}
