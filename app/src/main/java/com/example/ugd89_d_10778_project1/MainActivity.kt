package com.example.ugd89_d_10778_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import android.hardware.Camera.CameraInfo.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null

    private var currentCameraId: Int = CAMERA_FACING_BACK
    private val cameraBela: Int = CAMERA_FACING_BACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing our all variables.

        // on below line we are initializing our sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // on below line we are initializing our proximity sensor variable
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        try {
            mCamera = Camera.open(currentCameraId)
        }catch (e: IOException){
            Log.d("Error", "Failed to get Camera" + e.message)
        }

        if (mCamera != null){
            mCameraView = CameraView(this@MainActivity, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }
        @SuppressLint("MissingInflatedId", "LocalSuppress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{view: View? -> System.exit(0)}

        // on below line we are checking if the proximity sensor is null
        if (proximitySensor == null) {
            // on below line we are displaying a toast if no sensor is available
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // on below line we are registering
            // our sensor with sensor manager
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {

            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    // here we are setting our status to our textview..
                    // if sensor event return 0 then object is closed
                    // to sensor else object is away from sensor.
                    //sensorStatusTV.text = "<<< Near >>>"
                    if (currentCameraId == cameraBela) {
                        mCamera?.stopPreview();
                    }
                    //NB: if you don't release the current camera before switching, you app will crash
                    try {
                        mCamera = Camera.open(currentCameraId)
                    }catch (e: IOException){
                        Log.d("Error", "Failed to get Camera" + e.message)
                    }

                    //swap the id of the camera to be used
                    if(currentCameraId == CAMERA_FACING_BACK){
                        currentCameraId = CAMERA_FACING_FRONT;
                    }
                    else {
                        currentCameraId = CAMERA_FACING_BACK;
                    }
                    mCamera = Camera.open(currentCameraId);

//                    setCameraDisplayOrientation(, currentCameraId, camera);
//                    try {
//
//                        camera.setPreviewDisplay(previewHolder);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    camera.startPreview();

                    if (mCamera != null){
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }
                    @SuppressLint("MissingInflatedId", "LocalSuppress")
                    val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener{view: View? -> System.exit(0)}
                }



            } else {
                    // on below line we are setting text for text view
                    // as object is away from sensor.
                    //sensorStatusTV.text = "<<<< Away >>>>"

                }
                }
            }
        }
//}
