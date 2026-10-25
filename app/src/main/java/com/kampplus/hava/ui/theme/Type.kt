package com.kampplus.hava.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Yazı tipi ölçeği. Tema üç dosyadan oluşur: Color, Theme, Type.
 *
 * Material 3'ün hazır ölçeğinden başlayıp yalnızca başlıkları biraz kalınlaştırıyoruz.
 * Bileşenler boyutu elle yazmaz; `MaterialTheme.typography.titleMedium` gibi
 * **rol** isimleriyle okur. Böylece tipografi tek yerden değişir.
 */
private val base = Typography()

internal val HavaTypography = base.copy(
    titleLarge = base.titleLarge.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, letterSpacing = 0.5.sp),
    titleMedium = base.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    labelLarge = base.labelLarge.copy(fontWeight = FontWeight.Bold),
    labelSmall = base.labelSmall.copy(fontFamily = FontFamily.Monospace, letterSpacing = 1.sp),
    displaySmall = base.displaySmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
)
