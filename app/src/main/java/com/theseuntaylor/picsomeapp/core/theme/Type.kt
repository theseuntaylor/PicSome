package com.theseuntaylor.picsomeapp.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.theseuntaylor.picsomeapp.R

// Plus Jakarta Sans (SIL OFL 1.1, see assets/licenses) is a variable font; each weight below picks
// a point on its weight axis. Android 7.x can't apply variations and shows the default weight.
@OptIn(ExperimentalTextApi::class)
private fun plusJakartaSans(weight: FontWeight) = Font(
    resId = R.font.plus_jakarta_sans,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

private val BrandFontFamily = FontFamily(
    plusJakartaSans(FontWeight.Normal),
    plusJakartaSans(FontWeight.Medium),
    plusJakartaSans(FontWeight.SemiBold),
    plusJakartaSans(FontWeight.Bold),
)

private val Baseline = Typography()

/**
 * Material 3 type scale: the brand font for display, headline and title roles (the "expressive"
 * text), the system font for body and label text where readability matters most.
 */
val AppTypography = Typography(
    displayLarge = Baseline.displayLarge.copy(fontFamily = BrandFontFamily),
    displayMedium = Baseline.displayMedium.copy(fontFamily = BrandFontFamily),
    displaySmall = Baseline.displaySmall.copy(fontFamily = BrandFontFamily),
    headlineLarge = Baseline.headlineLarge.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
    headlineMedium = Baseline.headlineMedium.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = Baseline.headlineSmall.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
    titleLarge = Baseline.titleLarge.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
    titleMedium = Baseline.titleMedium.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = Baseline.titleSmall.copy(fontFamily = BrandFontFamily, fontWeight = FontWeight.SemiBold),
)
