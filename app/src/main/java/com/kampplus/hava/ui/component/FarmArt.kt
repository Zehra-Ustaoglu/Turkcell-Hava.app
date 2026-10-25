package com.kampplus.hava.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kampplus.hava.model.WeatherCondition

/** Original vector pixel art; no bitmap assets or network downloads. */
@Composable
fun FarmLandscape(modifier: Modifier = Modifier, weatherCode: Int = 0) {
    val night = isSystemInDarkTheme()
    val rain = weatherCode in 51..82 || weatherCode >= 95
    val snow = weatherCode in 71..77 || weatherCode in 85..86
    val description = when {
        snow -> "Karlı çiftlik manzarası"
        rain -> "Yağmurlu çiftlik manzarası"
        night -> "Yıldızlı bir gecede çiftlik"
        else -> "Güneşli bir günde çiftlik"
    }
    Canvas(modifier.fillMaxWidth().height(116.dp).semantics { contentDescription = description }) {
        scale(size.width / 240f, size.height / 88f, pivot = Offset.Zero) {
            pixel(
                if (night) {
                    0xFF25354F
                } else if (rain || snow) {
                    0xFF9CAEB3
                } else {
                    0xFF9CD4DC
                },
                0,
                0,
                240,
                88
            )
            if (night) {
                listOf(15 to 13, 49 to 7, 102 to 19, 132 to 8, 178 to 17, 225 to 6).forEach { (x, y) ->
                    pixel(0xFFFFEDAE, x, y, 2, 2)
                }
                pixel(0xFFFFEDAE, 197, 10, 12, 12)
                pixel(0xFF25354F, 202, 8, 10, 11)
            } else {
                pixel(0xFFE9AC43, 192, 8, 22, 22)
                pixel(0xFFFFDF7D, 195, 11, 16, 16)
                pixel(0xFFFFEAAA, 199, 4, 8, 3)
            }
            val cloud = if (night) 0xFF586781 else 0xFFEAF3DF
            listOf(22 to 15, 115 to 9).forEach { (x, y) ->
                pixel(cloud, x, y, 30, 6)
                pixel(cloud, x + 7, y - 5, 15, 6)
            }
            val hill = if (night) 0xFF3D5D59 else 0xFF79A995
            pixel(hill, 0, 39, 240, 35)
            pixel(hill, 16, 30, 45, 12)
            pixel(hill, 28, 24, 20, 8)
            pixel(hill, 118, 32, 67, 12)
            pixel(hill, 136, 25, 29, 8)
            val grass = if (snow) {
                0xFFE2EBDE
            } else if (night) {
                0xFF405B38
            } else {
                0xFF95B654
            }
            pixel(grass, 0, 53, 240, 35)
            pixel(if (night) 0xFF88704C else 0xFFE0BC78, 93, 64, 25, 24)
            pixel(if (night) 0xFF88704C else 0xFFE0BC78, 106, 77, 60, 11)
            // Cabin, stepped roof, chimney and warmly lit windows.
            pixel(0xFF71422E, 69, 43, 51, 26)
            pixel(0xFFBC8150, 72, 45, 45, 22)
            for (row in 0..4) pixel(0xFF71422E, 72, 47 + row * 4, 45, 1)
            pixel(0xFF533B32, 64, 39, 61, 5)
            pixel(0xFFAE5A3D, 69, 34, 51, 5)
            pixel(0xFFC57343, 74, 29, 41, 5)
            pixel(0xFFC57343, 79, 24, 31, 5)
            pixel(0xFF684737, 108, 20, 6, 11)
            pixel(0xFF563A2B, 91, 51, 10, 18)
            pixel(0xFFFFD578, 77, 49, 9, 9)
            pixel(0xFFFFD578, 105, 49, 8, 9)
            pixel(0xFF71422E, 81, 49, 1, 9)
            pixel(0xFF71422E, 77, 53, 9, 1)
            // Evergreen silhouettes.
            listOf(8 to 38, 36 to 43, 201 to 36, 220 to 43).forEach { (x, y) ->
                pixel(0xFF684737, x + 7, y + 12, 4, 17)
                val leaves = if (night) 0xFF294D3A else 0xFF3E7543
                pixel(leaves, x + 6, y, 6, 6)
                pixel(leaves, x + 3, y + 5, 12, 7)
                pixel(leaves, x, y + 12, 18, 8)
            }
            // Tilled vegetable beds and fence.
            pixel(0xFF845837, 143, 59, 43, 17)
            for (row in 0..2) {
                for (col in 0..5) {
                    val x = 146 + col * 7
                    val y = 61 + row * 5
                    pixel(0xFFB48148, x, y + 2, 4, 2)
                    pixel(0xFF436D35, x + 1, y, 2, 3)
                    pixel(if (snow) 0xFFE7ECDC else 0xFFCEE080, x, y, 4, 1)
                }
            }
            for (x in 0..64 step 10) pixel(0xFFD5AD70, x, 69, 3, 13)
            pixel(0xFFD5AD70, 0, 72, 66, 2)
            pixel(0xFFAA7E4A, 0, 77, 66, 2)
            if (rain || snow) {
                for (x in 4..236 step 19) {
                    for (y in 6..80 step 23) {
                        pixel(if (snow) 0xFFF9F6E9 else 0xFF6394AC, x, y, 1, if (snow) 2 else 5)
                    }
                }
            } else {
                listOf(26 to 84, 130 to 71, 193 to 81, 230 to 71).forEach { (x, y) ->
                    pixel(0xFFF6DF9B, x, y, 2, 2)
                    pixel(0xFF72913D, x, y + 2, 1, 2)
                }
            }
        }
    }
}

@Composable
fun FarmBanner(modifier: Modifier = Modifier, weatherCode: Int = 0, caption: String = "Her şehrin gökyüzünde başka bir hikâye.") {
    Card(modifier = modifier, border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)) {
        FarmLandscape(weatherCode = weatherCode)
        Text(caption, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
    }
}

@Composable
fun PixelWeatherIcon(code: Int, modifier: Modifier = Modifier) {
    val label = WeatherCondition.fromWeatherCode(code).label
    Canvas(modifier.size(40.dp).semantics { contentDescription = label }) {
        scale(size.width / 32f, size.height / 32f, pivot = Offset.Zero) {
            if (code <= 2) {
                pixel(0xFFBB7B31, 7, 5, 16, 16)
                pixel(0xFFF4BF50, 9, 7, 12, 12)
                pixel(0xFFFFDF83, 11, 9, 8, 8)
                listOf(13 to 0, 13 to 23, 2 to 11, 25 to 11).forEach { (x, y) -> pixel(0xFFDB9B37, x, y, 4, 4) }
            }
            if (code > 0) {
                pixel(0xFF667B86, 4, 15, 26, 10)
                pixel(0xFF667B86, 10, 9, 12, 7)
                pixel(0xFFD3E0DB, 6, 15, 22, 7)
                pixel(0xFFD3E0DB, 12, 11, 8, 6)
            }
            if (code in 51..67 || code in 80..82) {
                listOf(8, 16, 24).forEach { pixel(0xFF5F96B2, it, 27, 2, 4) }
            } else if (code in 71..77 || code in 85..86) {
                listOf(8, 16, 24).forEach { pixel(0xFF88AFB9, it, 27, 3, 3) }
            } else if (code >= 95) {
                pixel(0xFFE7AF42, 16, 23, 5, 5)
                pixel(0xFFE7AF42, 13, 27, 5, 4)
            }
        }
    }
}

@Composable
fun GrowingPlant(modifier: Modifier = Modifier) {
    val growth by rememberInfiniteTransition(label = "seedling").animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "growth"
    )
    val green = MaterialTheme.colorScheme.primary
    Column(modifier) {
        Canvas(Modifier.size(48.dp)) {
            scale(size.width / 24f, size.height / 24f, pivot = Offset.Zero) {
                pixel(0xFF9B693E, 5, 19, 14, 3)
                drawRect(green, Offset(11f, 17f - 10f * growth), Size(2f, 10f * growth + 2f))
                drawRect(green, Offset(5f, 13f - 5f * growth), Size(6f, 3f))
                drawRect(green, Offset(13f, 9f - 4f * growth), Size(6f, 3f))
            }
        }
    }
}

private fun DrawScope.pixel(color: Long, x: Int, y: Int, width: Int, height: Int) {
    drawRect(Color(color), Offset(x.toFloat(), y.toFloat()), Size(width.toFloat(), height.toFloat()))
}
