package com.britishbroadcast.frinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 100

    private lateinit var slideAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slideAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)

        open_settings_button.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                val uri = Uri.fromParts("package", packageName, null)
                //package://com.britishbroadcast.frinder/permissions
                //package:com.britishbroadcast.frinder#1
                Log.d("TAG_X", uri.toString())
                it.data = uri
            })
        }
    }

    override fun onStart() {
        super.onStart()
        //1. Check if permission is granted>
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hideOverlay()
            //this is already granted
            //Get location....
            registerLocationListener()
        } else {
            //not granted request permission
            requestLocationPermission()
        }
    }

    private fun registerLocationListener() {

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //this is already granted
                //Get location....
                registerLocationListener()
            } else { //Permission not granted - request again
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
                    requestLocationPermission()
                else//user set the settings to 'Do not ask again'
                    showOverLay()
            }
        }
    }

    private fun hideOverlay() {
        overlay_view.animation = slideOutAnimation
        overlay_view.visibility = View.GONE
    }

    private fun showOverLay() {
        overlay_view.visibility = View.VISIBLE
        overlay_view.animation = slideAnimation
    }
}