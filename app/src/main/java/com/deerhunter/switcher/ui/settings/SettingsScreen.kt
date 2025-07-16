package com.deerhunter.switcher.ui.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.deerhunter.switcher.ui.extentions.observeWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    ObserveEvents(viewModel)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val whiteTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
    ) {
        // Back button in top left corner
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = {
                    viewModel.handleIntent(SettingsIntent.UpdateUsername(it))
                },
                label = { Text("Логін") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = whiteTextFieldColors
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = {
                    viewModel.handleIntent(SettingsIntent.UpdatePassword(it))
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = whiteTextFieldColors
            )

            OutlinedTextField(
                value = uiState.ipAddress,
                onValueChange = {
                    viewModel.handleIntent(SettingsIntent.UpdateIpAddress(it))
                },
                label = { Text("IP адреса") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = whiteTextFieldColors
            )

            Button(
                onClick = {
                    viewModel.handleIntent(SettingsIntent.UpdateSettings)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зберегти зміни")
            }
        }
    }
}

@Composable
private fun ObserveEvents(
    viewModel: SettingsViewModel,
) {
    val context = LocalContext.current
    viewModel.actions.observeWithLifecycle { event ->
        when (event) {
            SettingsAction.ShowSuccessToast -> Toast.makeText(context, "Зміни збережено", Toast.LENGTH_SHORT).show()
        }
    }
}
