package com.example.notesapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.notesapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private val splashScreenScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        splashScreenScope.launch {
            delay(SPLASH_SCREEN_DURATION)

            if (hasNotificationPermission()) {
                startMainActivity()
            } else {
                requestNotificationPermission()
            }
        }


    }

    private fun startMainActivity() {
        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        finish()
    }

    private fun showPermissionDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Permission Required")
            .setMessage("The app needs notification permission to function properly.")
            .setPositiveButton("Go to Settings") { _, _ -> openAppSettings() }
            .setNegativeButton("Exit App") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }


    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, OPEN_SETTINGS_REQUEST_CODE)
    }

    private fun hasNotificationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_SETTINGS_REQUEST_CODE) {
            // After returning from app settings, recheck the notification permission
            if (hasNotificationPermission()) {
                startMainActivity()
            } else {
                // User still didn't grant the permission, handle accordingly
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity()
                } else {
                    showPermissionDeniedDialog()
                }
            }
        }
    }


    private companion object {
        private const val SPLASH_SCREEN_DURATION = 2000L
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
        private const val OPEN_SETTINGS_REQUEST_CODE = 2
    }

    override fun onDestroy() {
        super.onDestroy()

        splashScreenScope.cancel()
    }
}


