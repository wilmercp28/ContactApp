package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import java.util.Locale
@Composable
fun highlightText(text: String, query: String): AnnotatedString {
    if (query.isEmpty()) {
        return AnnotatedString(text)
    }
    val lowerText = text.lowercase(Locale.getDefault())
    val lowerQuery = query.lowercase(Locale.getDefault())
    val startIndex = lowerText.indexOf(lowerQuery)
    if (startIndex == -1) {
        return AnnotatedString(text)
    }
    return buildAnnotatedString {
        val endIndex = startIndex + query.length
        val beforeMatch = text.substring(0, startIndex)
        val matchedText = text.substring(startIndex, endIndex)
        val afterMatch = text.substring(endIndex)
        append(beforeMatch)
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
            )
        ) {
            append(matchedText)
        }
        append(afterMatch)
    }
}
