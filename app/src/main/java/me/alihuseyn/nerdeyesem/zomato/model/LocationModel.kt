package me.alihuseyn.nerdeyesem.zomato.model

import org.json.JSONObject

/**
 * <h1>LocationModel Data Class</h1>
 * <p>
 *  Keep track of location information
 * </p>
 */
data class LocationModel(
    @JsonContent var address: String? = null,
    @JsonContent var locality: String? = null,
    @JsonContent var city: String? = null,
    @JsonContent var latitude: String? = null,
    @JsonContent var longitude: String? = null
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