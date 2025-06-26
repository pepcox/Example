package com.example.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.app.R
import com.example.app.ui.components.ErrorPlaceholder
import com.example.app.ui.components.LoadingPlaceholder
import com.example.app.ui.theme.ApplicationTheme

@Composable
fun DetailScreen(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.cd_image_detail),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            loading = { LoadingPlaceholder() },
            error = { ErrorPlaceholder() },
        )
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_6)
@Composable
private fun DetailScreenPreview() {
    ApplicationTheme {
        DetailScreen(
            imageUrl = "https://example.com/image1.jpg",
            modifier = Modifier.fillMaxSize(),
        )
    }
}