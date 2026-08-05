package com.example.ui.components

import android.text.Html
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun RichTextRenderer(
    htmlContent: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp,
    textColor: Color = Color(0xFF1E293B)
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, fontSize.value)
                setTextColor(textColor.toArgb())
                setLineSpacing(12f, 1.2f)
            }
        },
        update = { textView ->
            textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, fontSize.value)
            textView.setTextColor(textColor.toArgb())
            textView.text = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
        }
    )
}
