package me.alihuseyn.nerdeyesem.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.alihuseyn.nerdeyesem.R
import me.alihuseyn.nerdeyesem.adapter.RecycleViewAdapter
import me.alihuseyn.nerdeyesem.service.LocationService
import me.alihuseyn.nerdeyesem.zomato.endpoint.SearchEndpoint
import me.alihuseyn.nerdeyesem.zomato.model.RestaurantModel
import me.alihuseyn.nerdeyesem.zomato.model.SearchModel
import java.io.Serializable
import java.lang.Exception
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), LocationService.Event, RecycleViewAdapter.RestaurantClickListener {

    /**
     * Permission request code
     */
    private val PERMISSION_REQUEST_CODE: Int = 1

    /**
     * GPS|Location Provider enable request code
     */
    private val GPS_ENABLE_RESULT: Int = 2

    /**
     * Recycle View Adapter
     */
    lateinit var recycleViewAdapter: RecycleViewAdapter

    /**
     * Recycle View
     */
    private lateinit var  recycleView: RecyclerView

    /**
     * Data of found Restaurants
     */
    val restaurants:ArrayList<RestaurantModel> = ArrayList()

    /**
     * Detect whether data is loading or not
     */
    var isLoading = false

    /**
     * Detect whether data is finished
     */
    var isFinished = false

    /**
     * Search Endpoint
     */
    var searchEndpoint:SearchEndpoint? = null

    /**
     * Location service
     */
    private lateinit var locationService: LocationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize location service
        locationService = LocationService(applicationContext)
        locationService.listener = this

        // Initialize Search Endpoint
        searchEndpoint  = SearchEndpoint(locationService.latitude, locationService.longitude)

        // Initialize Recycle View
        initializeRecycleView()
    }

    /**
     * Initialize recycle view
     */
    private fun initializeRecycleView() {
        recycleView = findViewById(R.id.restaurants_list)
        recycleViewAdapter = RecycleViewAdapter(applicationContext, restaurants, this)
        recycleView.adapter = recycleViewAdapter

        // Set Scroll Listener
        val self = this
        recycleView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int){
                super.onScrolled(recyclerView, dx, dy)
                val layout: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading && !isFinished) {
                    layout?.let {
                        if (it.findLastCompletelyVisibleItemPosition() == restaurants.size - 1) {
                            isLoading = true
                            // Increment next
                            searchEndpoint?.next()
                            // Execute retrieve request
                            RetrieveData(self).execute()
                        }
                    }
                }
            }
        })
    }

    /**
     * Operate Function check permission
     * and try to apply all the required
     * permission, provider enable and data fetch
     */
    private fun operate() {
        /**
         * Check permission of required services
         */
        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_CODE)
        } else {
            // Already Granted ready for operation
            // Try to detect last known location
            locationService.detectLocation()
        }
    }

    override fun onResume() {
        println("On Resume")
        super.onResume()
        // Check whether the function worked before
        // Location detected
        if (!locationService.isLocationDetected) {
            operate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    // Ready to detect locations
                    locationService.detectLocation()
                }
            }
        }
    }

    override fun onRestaurantItemClick(position: Int) {
        val intent = Intent(applicationContext, RestaurantActivity::class.java)
        intent.putExtra("restaurant", restaurants[position])
        startActivity(intent)
    }

    /**
     * @see LocationService.Event.onResult
     */
    override fun onResult(latitude: Double?, longitude: Double?) {
        // Set search endpoint coordinates
        searchEndpoint?.apply {
            this.latitude = latitude
            this.longitude = longitude
        }

        RetrieveData(this).execute()
    }

    /**
     * @see LocationService.Event.onPermissionRequire
     */
    override fun onPermissionRequire() {
        requestPermissions(arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION
        ), PERMISSION_REQUEST_CODE)
    }

    /**
     * @see LocationService.Event.onProviderDisable
     */
    override fun onProviderDisable() {
        // Show alert dialog for provider enable
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage(R.string.gps_provider_activate)

        builder.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
            dialogInterface.dismiss()
            // Set location detected not show anymore gps dialog
            locationService.isLocationDetected = true
            // Call with latitude and longitude with null
            RetrieveData(this).execute()
        }

        builder.setPositiveButton(R.string.enable) {_, _ ->
            startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_RESULT)
        }

        builder.create().show()
    }

    /**
     * Async class used to retrieve data on other thread
     * for not blocking main UI thread
     */
     private class RetrieveData internal constructor(context: MainActivity): AsyncTask<Unit, Void, SearchModel?>() {
        private val activityReference: WeakReference<MainActivity> = WeakReference(context)

        override fun doInBackground(vararg p0: Unit?): SearchModel? {
            val activity = activityReference.get()
            return try {
                activity?.let {
                    it.searchEndpoint?.retrieve()?.data() as SearchModel?
                }
            } catch (e: Exception) {
                Log.d(this.javaClass.simpleName, e.message!!)
                null
            }
        }

        override fun onPostExecute(result: SearchModel?) {
            super.onPostExecute(result)
            result?.run {
                val activity = activityReference.get()
                if (result.restaurants.isNotEmpty()) {
                    activity?.run {
                        activity.restaurants.addAll(result.restaurants)
                        activity.recycleViewAdapter.notifyDataSetChanged()
                        activity.isLoading = false
                    }
                } else {
                    activity?.run {
                        activity.isLoading = false
                        activity.isFinished = true
                    }
                }
            }
        }
    }
}
