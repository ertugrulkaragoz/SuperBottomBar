package me.ertugrul.lib

import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt

data class BottomBarItem(
    val text: CharSequence,
    val icon: Drawable,
    var rect: RectF = RectF()
)