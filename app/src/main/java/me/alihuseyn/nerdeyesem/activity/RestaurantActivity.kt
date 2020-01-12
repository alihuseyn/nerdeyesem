package me.alihuseyn.nerdeyesem.activity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import com.synnapps.carouselview.CarouselView
import me.alihuseyn.nerdeyesem.R
import me.alihuseyn.nerdeyesem.view.Badge
import me.alihuseyn.nerdeyesem.zomato.model.RestaurantModel
import java.util.*

/**
 * <h1>Restaurant Activity</h1>
 * <p>
 *     Restaurant related information
 *     generated the content view
 * </p>
 */
class RestaurantActivity : AppCompatActivity() {

    /**
     * Restaurant data information content
     */
    private var restaurant: RestaurantModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        // Set back button
        setSupportActionBar(findViewById(R.id.restaurant_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get restaurant information from serializable
        restaurant = intent.getSerializableExtra("restaurant") as RestaurantModel?

        // Initialize required view contents
        this.initializeCarousel()
        this.initializeTitleAndDistance()
        this.initializeRatingAndPrice()
        this.initializeContactAndWorking()
        this.initializeHighlights()
    }

    /**
     *  Initialize Carousel Image View
     */
    private fun initializeCarousel() {
        restaurant?.run {
            val carousel: CarouselView = findViewById(R.id.restaurant_images)
            carousel.pageCount = this.photos.size
            carousel.setImageListener { position, imageView ->
                Glide.with(applicationContext)
                    .load(this.photos[position].url)
                    .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                    .into(imageView)
            }
        }
    }

    /**
     * Set Title & Distance Information
     */
    private fun initializeTitleAndDistance() {
        restaurant?.run {
            val title: TextView = findViewById(R.id.restaurant_title)
            title.text = this.name

            val distance: TextView = findViewById(R.id.restaurant_distance)
            distance.text = if (this.distance != null) "${this.distance}m" else getText(R.string.not_any_info)
        }
    }

    /**
     * Set Rating & Price Information
     */
    private fun initializeRatingAndPrice() {
        restaurant?.run {
            val rating: RatingBar = findViewById(R.id.restaurant_rating)
            rating.rating = this.rating.aggregateRating?.toFloat()!!

            val ratingText: TextView = findViewById(R.id.restaurant_rating_text)
            ratingText.text = "${this.rating.aggregateRating} (${this.rating.ratingText})"
            ratingText.setTextColor(Color.parseColor("#${this.rating.ratingColor}"))

            val priceInfo: TextView = findViewById(R.id.restaurant_price_info)
            priceInfo.text = getString(R.string.price_for_two, this.averageCostForTwo, this.currency)
        }
    }

    /**
     * Initialize contact and working hour information
     */
    private fun initializeContactAndWorking() {
        restaurant?.run {
            // Set Contact information
            val phone: TextView = findViewById(R.id.restaurant_phone)
            phone.text = if (this.phoneNumbers.isNullOrEmpty()) getString(R.string.not_any_info) else this.phoneNumbers

            // Set Working information
            val workingHour: TextView = findViewById(R.id.restaurant_work_hour)
            workingHour.text = if (this.timings.isNullOrEmpty()) getString(R.string.not_any_info) else this.timings

            // Set Location information
            val locationInfo: TextView = findViewById(R.id.restaurant_location)
            locationInfo.text = this.location.address
        }
    }


    /**
     * Initialize highlights
     */
    private fun initializeHighlights() {
        restaurant?.run {
            // Set Highlights
            val highlights: FlexboxLayout = findViewById(R.id.restaurant_highlights)
            for (item in this.highlights) {
                // Badge Instance
                val highlight = Badge(applicationContext)
                highlight.setBadgeVariant(
                    listOf(
                        "primary",
                        "secondary",
                        "success",
                        "danger",
                        "info",
                        "dark",
                        "warning"
                    )[Random().nextInt(7)]
                )
                highlight.text = item
                highlights.addView(highlight)
                (highlight.layoutParams as ViewGroup.MarginLayoutParams).setMargins(10, 0, 0, 10)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}