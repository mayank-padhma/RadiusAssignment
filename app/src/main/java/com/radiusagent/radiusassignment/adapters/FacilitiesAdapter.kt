package com.radiusagent.radiusassignment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.radiusagent.radiusassignment.R
import com.radiusagent.radiusassignment.data.model.Option

class FacilitiesAdapter(val context: Context, val itemList : List<Option>, val facType : Int, private val onItemClickListener: OnClickListener) : RecyclerView.Adapter<FacilitiesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.option_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.fac_img)
        private val text: TextView = itemView.findViewById(R.id.fac_txt)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)

        fun bind(item: Option) {
            // Bind data to views
            when (item.icon) {
                "apartment" -> {
                imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.apartment))
                }
                "condo" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.condo))
                }
                "boat" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.boat))
                }
                "land" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.land))
                }
                "rooms" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.rooms))
                }
                "no-room" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.noroom))
                }
                "swimming" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.swimming))
                }
                "garden" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.garden))
                }
                "garage" -> {
                    imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.garage))
                }
            }
            text.text = item.name
            cardView.setOnClickListener {
                if (adapterPosition!=RecyclerView.NO_POSITION)
                    onItemClickListener.onClick(item.id.toInt() , facType, adapterPosition)
            }
        }
    }
}

// onclick interface for on click
interface OnClickListener {
    fun onClick (optionId: Int, facilityId: Int, recyclerId: Int)
}