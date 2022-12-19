package com.dvt.weatherapp.views

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.dvt.weatherapp.R
import com.dvt.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkGPSifEnabled()
    }

    override fun onRestart() {
        super.onRestart()
        checkGPSifEnabled()
    }

    private fun checkGPSifEnabled() {
        try {
            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_decision)
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
                val dialogMessage = dialog.findViewById<TextView>(R.id.dialogMessage)
                val dialogCancelBtn = dialog.findViewById<AppCompatButton>(R.id.dialogCancelBtn)
                val dialogConfirmBtn = dialog.findViewById<AppCompatButton>(R.id.dialogConfirmBtn)

                dialogTitle.text = getString(R.string.gps_off)
                dialogMessage.text = getString(R.string.gps_message)

                dialogConfirmBtn.setOnClickListener {
                    dialog.dismiss()
                    this.startActivity(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }

                dialogCancelBtn.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_1
                dialog.show()
                dialog.setCanceledOnTouchOutside(false)
            } else {
                // getCurrentKnownLocation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}