package com.example.registropressoes.extensions

import android.content.Context
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

fun Context.toast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

fun Context.getColorByTheme(color: Int): Int {
    return ResourcesCompat.getColor(resources, color, theme)
}