package me.alihuseyn.nerdeyesem.zomato.endpoint

import android.util.Log
import me.alihuseyn.nerdeyesem.zomato.model.Model
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * <h1>Connection class</h1>
 * <p>
 *  Connection class is the helper class
 *  to connect and fetch json content
 *  when it is required. The connection part
 *  with the api will be handled under this class
 * </p>
 * <p>
 *  Note: Currently Connection class only support GET method.
 *  Because all the available endpoints under Zomato require
 *  GET method currently. (See: https://developers.zomato.com/documentation)
 * </p>
 */
abstract class Endpoint(val endpoint: String) {

    /**
     * TAG is used for logging purpose
     */
    val TAG = Endpoint::class.java.simpleName

    /**
     * API_BASE_URL is the base url to connect Zomato
     */
    private val API_BASE_URL: String = "https://developers.zomato.com/api/v2.1"

    /**
     * API_USER_KEY is the api key for interaction
     */
    private val API_USER_KEY: String = "32874034aa354b88553d0f61fd6f237a"

    /**
     * Total data retrieved on each request if pagination supported
     */
    val TOTAL_DATA_COUNT_ON_PAGINATE: Int = 10

    /**
     * The result after retrieve operations
     */
    protected var data: JSONObject = JSONObject()

    /**
     * Offset to arrange for pagination start
     */
    var offset: Int = 0


    /**
     * Generate url path with the help of endpoint
     * and return it also append query parameters
     *
     * @return String path
     */
    open fun urlQueryBuilder() : String {
        var path = API_BASE_URL + "/" + this.endpoint.trimStart('/')

        // Check whether pagination support or not
        if (isPaginationSupported()) {
            path += "?start=$offset&count=$TOTAL_DATA_COUNT_ON_PAGINATE"
        }

        return path
    }
    /**
     * Try to retrieve information from given endpoint
     * If the request failed the exception will be thrown
     *
     * @exception Exception request exception
     * @return Endpoint itself for chain
     */
    fun retrieve(): Endpoint? {

        with(URL(urlQueryBuilder()).openConnection() as HttpURLConnection) {
            // Append API Key
            addRequestProperty("user-key", API_USER_KEY)

            Log.d(TAG, "$requestMethod $url | Code [$responseCode] Status [$responseMessage]")
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream.bufferedReader().use { data = JSONObject(it.readLine()) }
            } else {
                throw Exception("$responseCode - $content")
            }
        }
        return this
    }

    /**
     * Determine whether endpoint support pagination
     * and return boolean flag
     *
     * @return Boolean result of check
     */
    private fun isPaginationSupported(): Boolean {
        return this.javaClass.isAnnotationPresent(Pagination::class.java)
    }

    /**
     * Next Page Increment
     *
     * @return Endpoint itself for chain
     */
    fun next(): Endpoint {
        offset += TOTAL_DATA_COUNT_ON_PAGINATE

        return this
    }

    /**
     * Convert retrieved data to json object for easy access
     * and after that convert it to required model and return it
     *
     * @return Model result as a model
     */
    abstract fun data() : Model
}