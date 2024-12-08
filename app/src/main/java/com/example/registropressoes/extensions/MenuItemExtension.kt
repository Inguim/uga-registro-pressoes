package com.example.registropressoes.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.view.MenuItem
import androidx.core.text.parseAsHtml
import androidx.core.view.MenuItemCompat

fun MenuItem.setTitleColor(context: Context, color: Int) {
    val colorByTheme = context.getColorByTheme(color)
    val hexColor = Integer.toHexString(colorByTheme).uppercase().substring(2)
    val html = "<font color='#$hexColor'>$title</font>"
    this.title = html.parseAsHtml()
}

fun MenuItem.setIconColor(context: Context, color: Int) {
    val colorByTheme = context.getColorByTheme(color)
    MenuItemCompat.setIconTintList(this, ColorStateList.valueOf(colorByTheme))
}