package me.alihuseyn.nerdeyesem.zomato.model

import org.json.JSONObject

/**
 * <h1>PhotoModel</h1>
 * <p>
 *  Keep track of photo information
 * </p>
 */
data class PhotoModel(
    @JsonContent var id: String? = null,
    @JsonContent var url: String? = null,
    @JsonContent("thumb_url") var thumb: String? = null,
    @JsonContent("res_id") var resId: String? = null,
    @JsonContent var caption: String? = null,
    @JsonContent var width: String? = null,
    @JsonContent var height: String? = null,
    @JsonContent var timestamp: String? = null
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