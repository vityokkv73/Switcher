package com.deerhunter.switcher.ui.switcher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deerhunter.switcher.model.SwitchInfo
import com.deerhunter.switcher.model.SwitchState

@Composable
internal fun SwitchInfoCard(
    switchInfo: SwitchInfo,
    onCloseClick: (SwitchInfo) -> Unit,
    onOpenClick: (SwitchInfo) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { onCloseClick(switchInfo) },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Close",
                tint = Color.Red,
                modifier = Modifier.size(60.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = when (switchInfo.switchState) {
                        SwitchState.OPEN -> Color.Green
                        SwitchState.CLOSE -> Color.Red
                        SwitchState.UNKNOWN -> Color.Gray
                    },
                )
        ) {
            Text(
                text = switchInfo.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        IconButton(
            onClick = { onOpenClick(switchInfo) },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LockOpen,
                contentDescription = "Open",
                tint = Color.Green,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}
