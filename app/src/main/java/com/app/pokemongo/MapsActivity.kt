package com.app.pokemongo

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.app.pokemongo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val USER_LOCATION_REQUEST_CODE = 1000
    private var playerLocation:Location? = null
    private var locationManager:LocationManager? = null
    private var locationListener:PlayerLocationListener? = null
    private var pokemonCharacter:ArrayList<PokemonCharacter> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = PlayerLocationListener()
        requestLocationPermission()
        initializePokemonCharacters()


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the a+pp.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }
    private fun requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    USER_LOCATION_REQUEST_CODE
                )
                return

            }
        }
        accessUserLocation()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == USER_LOCATION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                accessUserLocation()
            }
        }

    }


    private fun initializePokemonCharacters(){
        pokemonCharacter.add(PokemonCharacter("Hello I'm Pikcachu'","I m cute",R.drawable.player1,
                    1.65729,31.996134))

        pokemonCharacter.add(PokemonCharacter("Hello I'm Charijaad'","I m cute",R.drawable.player1,
                    27.404523,29.647654))
        pokemonCharacter.add(PokemonCharacter("Hello I'm Squadly'","I m cute",R.drawable.player1,
                    10.492783,10.709112))
        pokemonCharacter.add(PokemonCharacter("Hello I'm poki1'","I m cute",R.drawable.player1,
                    28.220750,1.898764))
    }



    fun accessUserLocation(){
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER ,
            2000,2f,locationListener!!)

        val newThread = NewThread()
        newThread.start()
    }

    inner class PlayerLocationListener :LocationListener {
        constructor(){
            playerLocation = Location("MyProvider")
            playerLocation?.latitude = 26.8467
            playerLocation?.longitude = 80.9462
        }
        override fun onLocationChanged(updatelocation : Location) {
            playerLocation = updatelocation

        }

    }
    inner class NewThread:Thread{
        //you can called super constructor like Thread()
        //and You can called constructor with super class like below
        //below constructor you no need to define super its called expcility
        constructor():super(){

        }

        override fun run() {
            super.run()
            runOnUiThread {
                //30.893777836805423, 75.83482886842377
                // Add a marker in player location and move the camera
                val pLocation = LatLng(playerLocation!!.latitude, playerLocation!!.longitude)
                mMap.addMarker(MarkerOptions().position(pLocation).title("Marker Player Location")
                    .snippet("Let's go").icon(BitmapDescriptorFactory.fromResource(R.drawable.player0)))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pLocation))
            }
        }
    }
}