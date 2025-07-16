package com.deerhunter.switcher.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun InternetConnectionAvailability(isInternetConnected: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(if (isInternetConnected) Color.Transparent else Color.Red),
        contentAlignment = Alignment.Center
    ) {
        if (!isInternetConnected) {
            Text(
                text = "No internet connection",
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
