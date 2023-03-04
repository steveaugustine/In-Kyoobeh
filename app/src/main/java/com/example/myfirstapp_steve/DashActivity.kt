package com.example.myfirstapp_steve

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import java.util.concurrent.TimeUnit


class DashActivity : AppCompatActivity() {

    var name=""
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val arrayList=ArrayList<String>()
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)


        var myArr =ArrayAdapter<String>(
            this@DashActivity,
            android.R.layout.simple_list_item_1,
            arrayList
        );






        var kyoobah = imageView
        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            kyoobah,
            PropertyValuesHolder.ofFloat("scaleX", 0.8f),
            PropertyValuesHolder.ofFloat("scaleY", 0.8f)
        )
        scaleDown.duration = 2000
        scaleDown.repeatMode = ValueAnimator.REVERSE
        scaleDown.repeatCount = ValueAnimator.INFINITE
        scaleDown.start()
        @IgnoreExtraProperties
        data class User(val username: String? = null, val SSID: String? = null) {
            // Null default values create a no-argument default constructor, which is needed
            // for deserialization from a DataSnapshot.
        }





        val kyooban =User()
        val database =
            Firebase.database("https://my-first-app-steve-default-rtdb.asia-southeast1.firebasedatabase.app/")

        mAuth= FirebaseAuth.getInstance()
        val user= mAuth.currentUser
        val myref=database.getReference("User")
        val db=FirebaseDatabase.getInstance().getReference("User")



        textView2.text = "Number of People"
        val mainactivity = MainActivity()

        //wifi
        var ssid = "nowifi"
        //socket
        sock.SocketHandler.setSocket()
        val mSocket= sock.SocketHandler.getSocket()
        mSocket.connect()

        val name=""
        //click listener

        imageView.setOnClickListener() {

            val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

            val wifiInfo: WifiInfo = wifiManager.connectionInfo
            if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
                ssid = wifiInfo.ssid
                println(ssid)
                textView2.text = ssid
            } else {
                ssid = "no wifi"
            }
            textView2.text = ssid
            println("clickworking")

            myref.child(user?.displayName.toString()).setValue(ssid)

            mSocket.emit("counter")

            myref.child("").get().addOnSuccessListener {
                println("this value is ${it.value}")
                println("readd")
                val yourData: HashMap<String, Any> = it.value as HashMap<String, Any>
                textView2.text= yourData.toString()
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
                println("erooooor")
            }


            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
// This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue(String::class.java)


                    if (value != null) {
                        arrayList.add(value)
                    }
                    myArr.notifyDataSetChanged()
                    //listView.adapter=myArr




                    println( "Value is: $value")
                    if (dataSnapshot !=null && value!=null) {
                        Thread.sleep(1000)
                        val name = value.toString()
                        println("testing $name")
                        textView2.text=name
                    }


                    textView2.text=name




                }



                override fun onCancelled(error: DatabaseError) {
// Failed to read value
                    println("Failed to read value.")
                }
            })


        }







            // }
            //Firebase realtime dtabase


            //   if (hasLocationPermission()){
            //fusedLocationProviderClient.lastLocation.addOnSuccessListener { location-> Log.d("FirstFragment",location.latitude.toString())
            //val geo=Geocoder(this)
            //val currentLocation=geo.getFromLocation(location.latitude,location.longitude,6)
            //Log.d("FirstFragment",currentLocation.first().countryCode)
            //  Log.d("FirstFragment", currentLocation.first().latitude.toString())
            //Log.d("FirstFragment",currentLocation.first().longitude.toString())
            ///     val priority = LocationRequest.QUALITY_HIGH_ACCURACY
            //     val cancellationTokenSource = CancellationTokenSource()


            //   fusedLocationProviderClient.getCurrentLocation(priority, cancellationTokenSource.token)
            //     .addOnSuccessListener { location->
            //          Log.d("Location", "location is found: ${location}")

            /// val geo=Geocoder(this)

            //val currentLocation=geo.getFromLocation(location.latitude,location.longitude,6)


            ///    }}

            ///else{
            ///      requestLocationPermission()
            ///    }





         fun hasLocationPermission() =
            EasyPermissions.hasPermissions(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

         fun requestLocationPermission() {
            EasyPermissions.requestPermissions(
                this,
                "This application cannot work without Location Permission.",
                MainActivity.PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }


        //val intent=Intent(this,Service::class.java)

        val periodicWorkRequest= PeriodicWorkRequest.Builder(Myservice::class.java,15,TimeUnit.MINUTES).addTag("Myservice").build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Myservice",ExistingPeriodicWorkPolicy.REPLACE,periodicWorkRequest)
    }





}


