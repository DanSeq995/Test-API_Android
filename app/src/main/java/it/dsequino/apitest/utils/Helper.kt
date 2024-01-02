package it.dsequino.apitest.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.security.MessageDigest

object Helper {
    fun startActivity(from: AppCompatActivity, destination: Class<*>) {
        val intent = Intent(from, destination)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        from.startActivity(intent)
        ActivityCompat.finishAffinity(from)
    }

    fun startFragment(activity: AppCompatActivity, destination: Fragment, container: Int, bundle: Bundle?, animEnter: Int?, animExit: Int?) {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        destination.arguments = bundle
        if (animEnter != null && animExit != null) {
            fragmentTransaction.setCustomAnimations(animEnter, animExit)
        }

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(container, destination)
        fragmentTransaction.commit()
    }

    fun back(activity: AppCompatActivity) {
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.popBackStack()
    }

    fun encryptToSHA512(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        val bytes = messageDigest.digest(input.toByteArray())
        val hexString = StringBuilder()

        for (byte in bytes) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }
}