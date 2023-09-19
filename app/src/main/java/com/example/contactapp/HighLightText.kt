package com.example.contactapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Yellow),
        ) {
            append(text.substring(0, startIndex)) // Text before the match
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Yellow),
            ) {
                append(text.substring(startIndex, startIndex + query.length)) // Matched text
            }
            append(text.substring(startIndex + query.length)) // Text after the match
        }
    }
}



