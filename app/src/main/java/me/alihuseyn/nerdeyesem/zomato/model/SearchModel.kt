package me.alihuseyn.nerdeyesem.zomato.model

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
     * Constructor
     *
     * @param rawJson JSONObject|null raw json content
     */
    constructor(rawJson: JSONObject?): this() {
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
                restaurants.add(RestaurantModel(rawJson = it.getJSONObject(index).optJSONObject("restaurant")))
            }
        }
    }
}
