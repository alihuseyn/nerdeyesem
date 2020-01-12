package me.alihuseyn.nerdeyesem.zomato.model

import android.location.Location
import org.json.JSONObject

/**
 * <h1>SearchModel</h1>
 * <p>
 * Carry search result data
 * </p>
 */
data class SearchModel(
    @JsonContent("results_found") var total: String? = null,
    @JsonContent("results_start") var offset: String? = null,
    @JsonContent("results_shown") var count: String? = null,
    var restaurants: ArrayList<RestaurantModel> = ArrayList()
) : Model() {

    /**
     * Current latitude location
     */
    var currentLatitude: Double? = null

    /**
     * Current longitude location
     */
    var currentLongitude: Double? = null

    /**
     * Constructor
     *
     * @param rawJson JSONObject|null raw json content
     */
    constructor(rawJson: JSONObject?, latitude: Double?, longitude: Double?): this() {
        this.currentLatitude = latitude
        this.currentLongitude = longitude

        // Modeling
        rawJson?.let { toModel(it) }
    }

    /**
     * @see Model.toModel for addition information
     */
    override fun toModel(json: JSONObject) {
        super.toModel(json)
        // Detect restaurants and collect data
        json.optJSONArray("restaurants")?.let {
            for (index in 0 until it.length()) {
                val restaurant = RestaurantModel(rawJson = it.getJSONObject(index).optJSONObject("restaurant"))

                if (currentLongitude != null
                    && currentLatitude != null
                    && restaurant.location.longitude != null
                    && restaurant.location.latitude != null
                ) {
                    val starPoint = Location("Start")
                    starPoint.longitude = this.currentLongitude!!
                    starPoint.latitude = this.currentLatitude!!

                    val endPoint = Location("End")
                    endPoint.longitude = restaurant.location.longitude!!.toDouble()
                    endPoint.latitude = restaurant.location.latitude!!.toDouble()
                    // Calculate distance
                    restaurant.distance = Math.round(starPoint.distanceTo(endPoint))
                }

                restaurants.add(restaurant)
            }
        }
    }
}
