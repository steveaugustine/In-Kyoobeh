package com.example.myfirstapp_steve

import android.content.Intent
import android.content.LocusId
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign2.*


class SignActivity2 : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN=200
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign2)
        var gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        mAuth=FirebaseAuth.getInstance()

        button_first.setOnClickListener(){
            signIn()
        }

        }
    private fun signIn(){
        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
        }

    override fun onActivityResult(requestCode: Int,resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)

        if (requestCode== RC_SIGN_IN){
            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception=task.exception
            if (task .isSuccessful){
                try {
                    val account =task.getResult(ApiException::class.java)!!
                    Log.d("Signinactivity","firebaseAuthWithGoogle:" +account.id)
                    firebaseAuthWithGoogle(account.idToken!!)

                } catch(e:ApiException){
                    Log.w("Signinactivity","Google sign in failed",e)
                }
            }
            else{
                Log.w("Signinactivity",exception.toString()   )
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    Log.d("Signinactivity","signinsucces")
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Log.d("signinactivity","siginfailed")
                }
                // ...
            }
    }



    }

