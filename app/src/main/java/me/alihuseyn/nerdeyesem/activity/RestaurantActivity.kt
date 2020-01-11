package me.alihuseyn.nerdeyesem.activity

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import me.alihuseyn.nerdeyesem.R
import me.alihuseyn.nerdeyesem.zomato.model.RestaurantModel

class RestaurantActivity: AppCompatActivity() {

    var restaurant:RestaurantModel? = null
    var distanceInMeter: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        // Set back button
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get restaurant information from serializable
        restaurant = intent.getSerializableExtra("restaurant") as RestaurantModel?

        // Set View Content
        setViewData()
    }

    private fun setViewData() {

        restaurant?.run {
            // Set Carousel Image View
            val carousel: CarouselView = findViewById(R.id.restaurant_images)
            carousel.pageCount = this.photos.size
            carousel.setImageListener(imageListener)

            // Set Title
            val title: TextView = findViewById(R.id.restaurant_title)
            title.text = this.name

            // Set Distance Information
            val distance: TextView = findViewById(R.id.restaurant_distance)
            distance.text = "${distanceInMeter.toInt()}m"

            // Set Star Information
            val rating: RatingBar = findViewById(R.id.restaurant_rating)
            rating.rating = this.rating.aggregateRating?.toFloat()!!

            val ratingText: TextView = findViewById(R.id.restaurant_rating_text)
            ratingText.text = "${this.rating.aggregateRating} (${this.rating.ratingText})"
        }

    }

    private var imageListener: ImageListener =
        ImageListener {
                position, imageView -> Glide.with(applicationContext).load(restaurant?.photos?.get(position)?.url).into(imageView)
        }
}