package com.example.myfirstapp_steve

import android.app.IntentService
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Service: IntentService("service"){
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onHandleIntent(p0: Intent?) {



    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        var ssid = "nowifi"
        val database =
            Firebase.database("https://my-first-app-steve-default-rtdb.asia-southeast1.firebasedatabase.app/")

        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        val myref=database.getReference("User")


        val wifiManager1 = getSystemService(WIFI_SERVICE) as WifiManager

        val wifiInfo1: WifiInfo = wifiManager1.connectionInfo
        if (wifiInfo1.supplicantState == SupplicantState.COMPLETED) {
            ssid = wifiInfo1.ssid

            println(ssid)

        }
        else{
            ssid="no wifi"
        }
        myref.child(user?.displayName.toString()).setValue(ssid)
        return START_STICKY




    }


}