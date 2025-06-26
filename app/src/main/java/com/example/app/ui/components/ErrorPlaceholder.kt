package com.example.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.R

@Composable
fun ErrorPlaceholder(
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.ic_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            if (placeholder != null) {
                placeholder()
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.cd_error_artwork),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPlaceholderPreview() {
    ErrorPlaceholder()
}