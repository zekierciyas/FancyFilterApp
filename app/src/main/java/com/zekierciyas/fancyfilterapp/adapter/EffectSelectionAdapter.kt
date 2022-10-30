package com.zekierciyas.fancyfilterapp.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.model.SelectableEffects


class EffectSelectionAdapter internal constructor(
    context: Context?,
    effects: List<SelectableEffects>
) :
    RecyclerView.Adapter<EffectSelectionAdapter.ViewHolder>() {
    private val mEffects: List<SelectableEffects> = effects
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_effect_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the view and textview in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val effect = mEffects[position].effect
        holder.myView.setImageBitmap(effect)
    }

    // total number of rows
    override fun getItemCount(): Int {
        return mEffects.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var myView: ImageView = itemView.findViewById(R.id.colorView)
        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
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