package me.alihuseyn.nerdeyesem.zomato.endpoint

import me.alihuseyn.nerdeyesem.zomato.model.Model
import me.alihuseyn.nerdeyesem.zomato.model.SearchModel

/**
 * <h1>SearchEndpoint class</h1>
 * <p>
 *  Zomato Search endpoint class.
 *  This class helps to make search over Zomato.
 * </p>
 */
@Pagination
class SearchEndpoint(var latitude: Double?, var longitude: Double?): Endpoint("/search") {

    override fun urlQueryBuilder(): String {
        var urlQuery = super.urlQueryBuilder()
        if (latitude != null && longitude != null) {
            urlQuery += "&lat=$latitude&lon=$longitude"
        }
        return urlQuery
    }

    /**
     * @see Endpoint.data for more information
     */
    override fun data() : Model {
        return SearchModel(rawJson = this.data)
    }

}
