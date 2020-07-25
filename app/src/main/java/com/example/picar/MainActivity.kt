package com.example.picar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.faizkhan.mjpegviewer.MjpegView

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        this.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        val view: MjpegView? = findViewById(R.id.piCam)
        view!!.isAdjustHeight = true
        view.setUrl("http://192.168.0.109:8080/?action=stream")
        view.mode1 = MjpegView.MODE_STRETCH
        view.isRecycleBitmap1 = true
    }

    override fun onStop() {
        super.onStop()
        val view: MjpegView? = findViewById(R.id.piCam)
        Log.i(TAG, "Stopping stream")
        view!!.stopStream()
    }

    override fun onStart() {
        super.onStart()
        val view: MjpegView? = findViewById(R.id.piCam)
        Log.i(TAG, "Starting stream")
        view!!.startStream()
    }
}