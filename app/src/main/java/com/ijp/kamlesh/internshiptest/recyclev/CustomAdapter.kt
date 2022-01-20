package com.ijp.kamlesh.internshiptest.recyclev

import android.content.Context
import android.graphics.Color
import android.util.Log
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

class CustomAdapter(private val mList: MutableList<ItemsViewModel>,private val listener:ItemChangeListenerCustom,private val context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var itemChangeListenerCustom: ItemChangeListenerCustom? = null
    interface ItemChangeListenerCustom{
        fun itemChanged()
    }
    init{

        itemChangeListenerCustom=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seekbar_component, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dynaPos=holder.adapterPosition
        val currentBar = mList[dynaPos]

        holder.startValueButton.text="${currentBar.start}"
        holder.startValueButton.setBackgroundColor(currentBar.color)

        holder.endValueButton.text="${currentBar.end}"
        holder.endValueButton.setBackgroundColor(currentBar.color)

        holder.seekbar.min=currentBar.start
        holder.seekbar.max=currentBar.end

        holder.seekbar.progress=currentBar.end
        //
        fun hideTrashIconFromButtons(){
            holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            holder.endValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
        //Clean if already set
        hideTrashIconFromButtons()
        holder.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // Show right delete button only when user reach start value of first seekbar
                    // Special icon for 1st bar to clear all bars
                    if(progress==1 && holder.adapterPosition==0){
                        holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_delete,0,0,0)
                        holder.endValueButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_delete,0,0,0)

                    }else if(progress==currentBar.start)
                        holder.endValueButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete,0,0,0)
                    else{
                        //Any better way?
                        holder.endValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        holder.endValueButton.text=progress.toString()
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_delete,0,0,0)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                    val actualpos=holder.adapterPosition

                    // Case 1: First bar clean => remove all bars except 1st
                    if(seekBar.progress==1 && actualpos==0){

                        mList.clear()
                        mList.add(ItemsViewModel(1,100, Color.rgb(255,0,0)))

                        hideTrashIconFromButtons()

                        notifyDataSetChanged()
                        itemChangeListenerCustom?.itemChanged()

                        return
                    }

                    //Case 2: Delete any other bar except 1st..
                    val leftDragDeleteCondition=( seekBar.progress==currentBar.start || seekBar.progress==currentBar.start-1)

                    if(leftDragDeleteCondition && mList.size>1){

                        val previousBar=mList[actualpos-1]

                        mList.set(actualpos-1,ItemsViewModel(previousBar.start,currentBar.end,previousBar.color))
                        mList.removeAt(actualpos)

                        notifyDataSetChanged()

                        holder.startValueButton.text=seekBar.progress.toString()
                        itemChangeListenerCustom?.itemChanged()
                        //deleted bar's end value should be end value of previous bar
                        // 0 24
                        // 25 50 < delete this, new prev bar value will be 0 to 50
                        // 51 100
                        hideTrashIconFromButtons()
                        return
                    }
                    //Case 3: Dont allow segment below or qual 2
                    if(2>(currentBar.end - seekBar.progress)){
                        Toast.makeText(context,"Minimum Segment length is 2!",Toast.LENGTH_SHORT).show()

                        //Reset all button to old value..

                        holder.startValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        holder.startValueButton.text=currentBar.start.toString()

                        holder.endValueButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        holder.endValueButton.text=currentBar.end.toString()
                        holder.seekbar.progress=currentBar.end
                        return
                    }
                    seekBar.progress.also { seekBar.max = it }
                    mList[dynaPos] = ItemsViewModel(currentBar.start,seekBar.progress,currentBar.color)

                    val nextMin=if(mList.size>dynaPos+1){
                        mList[dynaPos+1].start-1
                    }else{
                        100
                    }

                    mList.add(dynaPos+1,ItemsViewModel(seekBar.progress+1,nextMin,getRandomColor()))
                    holder.startValueButton.text=seekBar.progress.toString()
                    holder.endValueButton.text=seekBar.max.toString()

                    notifyDataSetChanged()
                    itemChangeListenerCustom?.itemChanged()
                    hideTrashIconFromButtons()
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
