package ru.ulyanaab.lifemates.ui.common.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.ulyanaab.lifemates.R

val SfProFontFamily = FontFamily(
    Font(R.font.sf_pro_regular),
    Font(R.font.sf_pro_bold, FontWeight.Bold),
    Font(R.font.sf_pro_medium, FontWeight.Medium)
)


// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 48.sp,
        letterSpacing = (-0.01).sp,
        lineHeight = 22.sp
    ),
    h2 = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.02.sp,
        lineHeight = 41.sp
    ),
    h3 = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = 0.02.sp,
        lineHeight = 37.sp
    ),
    body1 = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 22.sp
    ),
    body2 = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 22.sp
    ),
    caption = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 22.sp
    ),
    button = TextStyle(
        fontFamily = SfProFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 22.sp
    ),
)
