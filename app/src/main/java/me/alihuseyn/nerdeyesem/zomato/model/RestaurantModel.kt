package me.alihuseyn.nerdeyesem.zomato.model

import org.json.JSONObject

/**
 * <h1>RestaurantModel</h1>
 * <p>
 *  Keep track of restaurant information with
 *  its images and location information
 * </p>
 */
data class RestaurantModel(
    @JsonContent var id:String? = null,
    @JsonContent var name:String? = null,
    @JsonContent var url:String? = null,
    @JsonContent var cuisines:String? = null,
    @JsonContent var currency:String? = null,
    @JsonContent("average_cost_for_two") var averageCostForTwo:String? = null,
    @JsonContent("price_range") var priceRange:String? = null,
    var highlights: ArrayList<String> = ArrayList(),
    @JsonContent var thumb:String? = null,
    var location:LocationModel = LocationModel(),
    @JsonContent("all_reviews_count") var allReviewsCount: String? = null,
    @JsonContent("photo_count") var photoCount: String? = null,
    @JsonContent("phone_numbers") var phoneNumbers: String? = null,
    @JsonContent var timings: String? = null,
    var photos: ArrayList<PhotoModel> = ArrayList(),
    var rating:RatingModel = RatingModel(),
    var distance: Int? = null
) : Model() {

    /**
     * Constructor
     *
     * @param rawJson JSONObject|null raw json content
     */
    constructor(rawJson: JSONObject?): this() {
        rawJson?.let { toModel(it) }
    }

    /**
     * Some content are not json element
     * they are required to handle manually
     *
     * @see Model.toModel for addition information
     */
    override fun toModel(json: JSONObject) {
        // Call super model conversion
        super.toModel(json)

        // Handle highlights
        json.optJSONArray("highlights")?.let {
            for (index in 0 until it.length()) {
                highlights.add(it.getString(index))
            }
        }

        // Handle Locations
        this.location = LocationModel(rawJson = json.optJSONObject("location"))

        // Handle Photos
        json.optJSONArray("photos")?.let {
            for (index in 0 until it.length()) {
                photos.add(PhotoModel(rawJson = it.getJSONObject(index).optJSONObject("photo")))
            }
        }

        // Handle User Ratings
        this.rating = RatingModel(rawJson = json.optJSONObject("user_rating"))
    }
}
