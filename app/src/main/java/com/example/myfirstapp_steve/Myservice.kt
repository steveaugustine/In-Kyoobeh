package com.example.myfirstapp_steve

import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Myservice(context: Context,workerParmams: WorkerParameters): Worker(context,workerParmams){
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun doWork(): Result {
        var ssid = "nowifi"
        val database =
            Firebase.database("https://my-first-app-steve-default-rtdb.asia-southeast1.firebasedatabase.app/")

        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        val myref=database.getReference("User")


        val wifiManager2= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var wifiInfo: WifiInfo

        val wifiInfo1: WifiInfo = wifiManager2.connectionInfo
        if (wifiInfo1.supplicantState == SupplicantState.COMPLETED) {
            ssid = wifiInfo1.ssid

            println(ssid)

        }
        else{
            ssid="no wifi"
        }
        myref.child(user?.displayName.toString()).setValue(ssid)

        return Result.success()
    }




}