package com.zekierciyas.fancyfilterapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.model.SelectableEffects
import de.hdodenhof.circleimageview.CircleImageView


class EffectSelectionAdapter internal constructor(
    context: Context?,
    effects: List<SelectableEffects>
) :
    RecyclerView.Adapter<EffectSelectionAdapter.ViewHolder>() {
    private val mEffects: List<SelectableEffects> = effects
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private var previousSelected: Int? = null
    private var viewHolder: ViewHolder? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_effect_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the view and textview in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder = holder
        val effect = mEffects[position].effect
        holder.myView.setImageBitmap(effect)

        previousSelected?.let {
            if (position == it) {
                holder.myView.also {
                    it.borderWidth = 8
                    it.borderColor = ContextCompat.getColor(holder.myView.context, R.color.white)
                }
            } else {
                // Removing previous selected filter's border, when another filter is selected
                holder.myView.borderWidth = 0

            }
        }
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mEffects.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myView: CircleImageView = itemView.findViewById(R.id.colorView)
        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
            previousSelected = adapterPosition
            notifyDataSetChanged()
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}