package com.example.myfirstapp_steve

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.common.api.Api
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_dash.*
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.*
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.activity_main.imageView as imageView1


open class MainActivity : AppCompatActivity(), PermissionCallbacks {
    private lateinit var mAuth: FirebaseAuth
    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)


        val database= Firebase.database("https://my-first-app-steve-default-rtdb.asia-southeast1.firebasedatabase.app/")


        val periodicWorkRequest= PeriodicWorkRequest.Builder(Myservice::class.java,15, TimeUnit.MINUTES).addTag("Myservice").build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Myservice",
            ExistingPeriodicWorkPolicy.REPLACE,periodicWorkRequest)

//realtimedata
        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        val myref=database.getReference("User")
        val dashActivity=DashActivity()

        //wifi
        //wifi
        var ssid="nowifi"
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager




        val curruser= mapOf(user?.displayName.toString() to ssid)
        println ("wifi state ${wifiManager.wifiState}")





        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (user!=null){
                val dashboardIntent  = Intent(this,DashActivity::class.java)
                startActivity(dashboardIntent)
                finish()
            }else{
                val signInIntent = Intent(this,SignActivity2::class.java)
                startActivity(signInIntent)
                finish()
            }
            val wifiInfo: WifiInfo = wifiManager.connectionInfo

            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                ssid = wifiInfo.ssid
                println(ssid)


            }
            else{
                ssid="not connected"

            }
            println(ssid)
            myref.child(user?.displayName.toString()).setValue(ssid)
            // Your Code
        }, 3000)


        setViewVisibility()
        var imagetag=imageView



        requestLocationPermission()
        if (hasLocationPermission()){
            println(hasLocationPermission())
        }

    }



     fun hasLocationPermission() =
        hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

     fun requestLocationPermission() {
        requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {


    


    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            this,
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        setViewVisibility()
    }

    private fun setViewVisibility() {
        if (hasLocationPermission()) {
          println("has location")
        } else {
           println("no location")
        }
    }



}









