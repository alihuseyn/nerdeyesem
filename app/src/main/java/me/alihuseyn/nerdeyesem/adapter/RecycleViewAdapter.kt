package me.alihuseyn.nerdeyesem.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.alihuseyn.nerdeyesem.R
import me.alihuseyn.nerdeyesem.zomato.model.RestaurantModel

class RecycleViewAdapter(
    private val context: Context,
    var data: ArrayList<RestaurantModel>,
    var listener: RestaurantClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    /**
     * View on showing restautants
     */
    private val VIEW_TYPE_RESTAURANTS = 0

    /**
     * View on loading
     */
    private val VIEW_TYPE_LOADING =  1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_loader, parent, false))
            VIEW_TYPE_RESTAURANTS -> ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false), listener)
            else -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_loader, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(data.size <= position) VIEW_TYPE_LOADING else VIEW_TYPE_RESTAURANTS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListViewHolder) {
            val restaurant: RestaurantModel = data[position]
            // Load data to required view objects
            Glide.with(context).load(restaurant.thumb).into(holder.image)
            holder.title.text = restaurant.name
        }
    }

    inner class LoadingViewHolder(view: View): RecyclerView.ViewHolder(view)
    inner class ListViewHolder(
        view: View,
        private var itemClickListener: RestaurantClickListener
    ): RecyclerView.ViewHolder(view), View.OnClickListener {
        var image: ImageView = view.findViewById(R.id.restaurants_list_img)
        var title: TextView = view.findViewById(R.id.restaurants_list_title)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            itemClickListener.onRestaurantItemClick(adapterPosition)
        }
    }

    interface RestaurantClickListener {
        fun onRestaurantItemClick(position:Int)
    }
}