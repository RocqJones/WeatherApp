package com.dvt.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.dvt.weatherapp.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ReusableUtils {

    // Check if device has internet
    @Suppress("DEPRECATION")
    fun isDeviceConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun convertKelvinToCelsius(k: Double): Double {
        val c = 273.15
        val res = k - c
        // ans to 1 decimal place
        return String.format("%.1f", res).toDouble()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeConverter(dateTimeStr: String): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        val zonedDateTime = ZonedDateTime.parse("$dateTimeStr Z", pattern)
        // println("Date Time $zonedDateTime")
        // println("Date Time ${formatter.format(zonedDateTime)}")
        return formatter.format(zonedDateTime)
    }

    fun normalDialog(title: String, message: String, context: Context) {
        try {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_alert)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val dialogCloseBtn = dialog.findViewById<ImageView>(R.id.dialogCloseBtn)
            val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
            val dialogMessage = dialog.findViewById<TextView>(R.id.dialogMessage)
            val okBtn = dialog.findViewById<AppCompatButton>(R.id.dialogOkBtn)

            dialogTitle.text = title
            dialogMessage.text = message

            dialogCloseBtn.visibility = View.GONE
            okBtn.setOnClickListener { dialog.dismiss() }

            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation_1
            dialog.show()
            dialog.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkGPSifEnabled(context: Context) {
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_decision)
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
                val dialogMessage = dialog.findViewById<TextView>(R.id.dialogMessage)
                val dialogCancelBtn = dialog.findViewById<AppCompatButton>(R.id.dialogCancelBtn)
                val dialogConfirmBtn = dialog.findViewById<AppCompatButton>(R.id.dialogConfirmBtn)

                dialogTitle.text = context.getString(R.string.gps_off)
                dialogMessage.text = context.getString(R.string.gps_message)

                dialogConfirmBtn.setOnClickListener {
                    dialog.dismiss()
                    context.startActivity(
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