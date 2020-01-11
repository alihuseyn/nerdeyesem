package me.alihuseyn.nerdeyesem.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * <h1>LocationService class</h1>
 * <p>
 *  LocationService is the helper service to detect
 *  last location of user and trigger callback function
 *  over event triggers
 * </p>
 */
class LocationService(val context: Context) {

    /**
     * Location Latitude Information
     */
    var latitude: Double? = null

    /**
     * Location Longitude Information
     */
    var longitude: Double? = null

    /**
     * Define whether location detected or not
     */
    var isLocationDetected = false

    /**
     * Fused Location Client information
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Event Listener
     */
    lateinit var listener: Event

    /**
     * Check whether location provider is enabled
     * If the location provider is turned off the last known
     * location will be null
     * See https://developer.android.com/training/location/retrieve-current
     *
     * @return Boolean status of location provider
     */
    private fun isLocationProviderEnabled(): Boolean {
        var status = false

        val locationManager: LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationManager?.let {
            status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        return status
    }
    /**
     * Detect location and trigger callback function
     *
     * @return Unit
     */
    fun detectLocation () {
        // Check Permission each time
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationProviderEnabled()) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    it?.let {
                        this.latitude = it.latitude
                        this.longitude = it.longitude
                        // Trigger result found
                        isLocationDetected = true
                        listener.onResult(latitude, longitude)
                    }
                }
            } else {
                // trigger listener dialog
                isLocationDetected = false
                listener.onProviderDisable()
            }
        } else {
            isLocationDetected = false
            listener.onPermissionRequire()
        }
    }

    /**
     * Event Service Listener
     */
    interface Event {
        /**
         * This function is triggered when the result is ready
         * from last known location
         */
        fun onResult(latitude: Double?, longitude: Double?)

        /**
         * This function is called when the permission for
         * location provider is required
         */
        fun onPermissionRequire()

        /**
         * This function is called when the location
         * provider is disabled
         */
        fun onProviderDisable()
    }

}