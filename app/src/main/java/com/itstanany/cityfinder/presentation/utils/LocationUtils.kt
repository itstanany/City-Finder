package com.itstanany.cityfinder.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.net.toUri
import com.itstanany.cityfinder.R

fun openLocationInMaps(
  context: Context,
  latitude: Double,
  longitude: Double,
  label: String?
) {
  val geoUri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)".toUri()
  val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)

  val packageManager = context.packageManager
  val activities =
    packageManager.queryIntentActivities(mapIntent, PackageManager.MATCH_DEFAULT_ONLY)

  if (activities.isNotEmpty()) {
    context.startActivity(mapIntent)
    return
  }

  // No maps app found, redirect to appropriate store
  openGoogleMapsInStore(context)
}

private fun openGoogleMapsInStore(context: Context) {
  val googleMapsPackage = "com.google.android.apps.maps"

  // Try Google Play Store
  try {
    val playIntent =
      Intent(
        Intent.ACTION_VIEW,
        "market://details?id=$googleMapsPackage".toUri()
      ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    context.startActivity(playIntent)
    return
  } catch (e: ActivityNotFoundException) {
    // Continue to next fallback
  }

  // Try Huawei AppGallery
  try {
    val huaweiUri = "appmarket://details?id=$googleMapsPackage".toUri()
    val huaweiIntent =
      Intent(Intent.ACTION_VIEW, huaweiUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    context.startActivity(huaweiIntent)
    return
  } catch (e: ActivityNotFoundException) {
    // Continue to next fallback
  }

  // Try Samsung Galaxy Store
  try {
    val samsungUri = "samsungapps://ProductDetail/$googleMapsPackage".toUri()
    val samsungIntent =
      Intent(Intent.ACTION_VIEW, samsungUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    context.startActivity(samsungIntent)
    return
  } catch (e: ActivityNotFoundException) {
    // Continue to next fallback
  }

  // Final fallback: open in browser
  try {
    val browserUri = "https://play.google.com/store/apps/details?id=$googleMapsPackage".toUri()
    val browserIntent =
      Intent(Intent.ACTION_VIEW, browserUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    context.startActivity(browserIntent)
  } catch (e: Exception) {
    Toast.makeText(
      context,
      context.getString(R.string.no_app_store_or_browser_available_to_install_maps),
      Toast.LENGTH_LONG
    ).show()
  }
}
