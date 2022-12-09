package com.zekierciyas.fancyfilterapp.adapter

import android.content.Context
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.zekierciyas.fancyfilterapp.R
import com.zekierciyas.fancyfilterapp.model.SelectableEffects


class EffectSelectionAdapter internal constructor(
    context: Context?,
    private val effects: List<SelectableEffects>
) :RecyclerView.Adapter<EffectSelectionAdapter.ViewHolder>() {

    /*Inflating layout*/
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    /*Click listener when filter is selected*/
    private var clickListener: ItemClickListener? = null

    /*Position of selected item */
    private var selectedItemPosition: Int? = null

    /*Position of previous selected item */
    private var previousSelectedItemPosition: Int? = null

    /*Holder of selectable effects */
    private var viewHolder: ViewHolder? = null

    /*Original size of selectable filter view before selected */
    private var originalSize: Size = Size(0,0)

    /*Layout Param of selectable filters */
    private var params: ViewGroup.LayoutParams = ViewGroup.LayoutParams(0,0)


    enum class ViewState{
        EXPANDED,
        NARROW,
        EMPTY
    }
    companion object {
        private const val INCREASE_AMOUNT = 1.2
        private var viewState: ViewState = ViewState.EMPTY
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.item_effect_layout, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the view and textview in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder = holder
        val effect = effects[position].effect
        holder.imageView.setImageBitmap(effect)

        if (originalSize.width == 0) {
            /* Required to define once time the original time
             * checking its default value */
            params = holder.imageView.layoutParams
            originalSize = Size(params.width, params.height)
        }

        selectedItemPosition?.let { selectedItemPosition ->
            if (position == selectedItemPosition) {
                previousSelectedItemPosition = selectedItemPosition
                if (previousSelectedItemPosition == selectedItemPosition) {

                    when(viewState) {
                        ViewState.EXPANDED -> {
                            holder.imageView.narrowView()
                            holder.cardView.removeStroke()
                            return

                        }
                        ViewState.NARROW -> {
                            holder.cardView.also {
                                holder.imageView.expandView()
                                it.addStroke()
                                return
                            }
                        }
                        else -> {

                        }
                    }
                }
            } else {
                holder.imageView.narrowView()
                holder.cardView.removeStroke()
            }
        }
    }

    // total number of rows
    override fun getItemCount(): Int {
        return effects.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView = itemView.findViewById(R.id.image_view)
        var cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
        override fun onClick(view: View) {
            if (clickListener != null) clickListener!!.onItemClick(view, adapterPosition)
            selectedItemPosition = adapterPosition
            notifyDataSetChanged()
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    private fun View.expandView() {
        viewState = ViewState.EXPANDED
        params.width = (originalSize.width * INCREASE_AMOUNT).toInt()
        params.height = (originalSize.height * INCREASE_AMOUNT).toInt()
        this.layoutParams = params
    }

    private fun View.narrowView() {
        if (originalSize.height != 0) {
            viewState = ViewState.NARROW
            params.height = originalSize.height
            params.width = originalSize.width
            this.layoutParams = params
        }
    }

    private fun MaterialCardView.addStroke() {
        this.strokeWidth = 10
        this.strokeColor = ContextCompat.getColor(this.context, R.color.white)
    }

    private fun MaterialCardView.removeStroke() {
        this.strokeWidth = 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}