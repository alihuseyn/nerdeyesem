package me.alihuseyn.nerdeyesem.zomato.model

import org.json.JSONObject

/**
 * <h1>RatingModel Data Class</h1>
 * <p>
 *  Keep track of user rating information
 * </p>
 */
data class RatingModel(
    @JsonContent("aggregate_rating") var aggregateRating: String? = null,
    @JsonContent("rating_text") var ratingText: String? = null,
    @JsonContent("rating_color") var ratingColor: String? = null,
    @JsonContent var votes: String? = null
): Model() {

    /**
     * Constructor
     *
     * @param rawJson JSONObject|null raw json content
     */
    constructor(rawJson: JSONObject?): this() {
        rawJson?.let { toModel(it) }
    }
}
