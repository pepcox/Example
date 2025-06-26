package com.example.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.app.R
import com.example.app.com.example.app.data.models.ViewState
import com.example.app.data.models.ErrorData
import com.example.app.data.models.Item
import com.example.app.data.models.Media
import com.example.app.rememberDebounced
import com.example.app.ui.components.ErrorPlaceholder
import com.example.app.ui.components.LoadingPlaceholder
import com.example.app.ui.theme.ApplicationTheme
import com.example.app.viewModel.MainViewModel
import com.example.app.viewModel.MainViewModelContract
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onItemTapped: (Item) -> Unit = {},
) {
    HomeScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        onItemTapped = onItemTapped
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    viewModel: MainViewModelContract,
    modifier: Modifier = Modifier,
    onItemTapped: (Item) -> Unit = {},
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val state by viewModel.state.collectAsState()

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.setQuery(it) },
                label = { Text(stringResource(R.string.label_tags)) },
                singleLine = true,
                modifier = Modifier
                    .padding(vertical = 0.dp)
                    .weight(1f)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* handle search */ }),
            )

            Button(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                onClick = rememberDebounced {
                    keyboardController?.hide()
                    viewModel.getFeeds()
                },
                content = { Text(stringResource(R.string.action_search)) }
            )
        }
        Spacer(Modifier.height(16.dp))

        val items = state.items

        if (items.isNullOrEmpty()) {
            if (!state.isLoading) {
                Image(
                    painter = painterResource(id = R.drawable.empty_state),
                    contentDescription = stringResource(R.string.cd_error_artwork),
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "No items found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        } else {
            // Compose grid of items
            PullToRefreshBox(
                isRefreshing = state.isLoading,
                onRefresh = {
                    viewModel.refreshFeeds()
                },
                modifier = modifier
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {
                    items(items) { item ->
                        Card(
                            modifier = modifier,
                            onClick = rememberDebounced { onItemTapped.invoke(item) },
                            content = {
                                Column {
                                    if (item.title.isNotBlank()) {
                                        Text(
                                            item.title,
                                            modifier
                                                .fillMaxWidth()
                                                .padding(4.dp),
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center,
                                        )
                                    }

                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(item.media.imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        loading = { LoadingPlaceholder() },
                                        error = { ErrorPlaceholder() },
                                        contentDescription = stringResource(R.string.cd_item_thumbnail),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    val errorDataValue = state.errorData
    if (errorDataValue != null) {
        AlertDialog(
            icon = {
                Icon(
                    Icons.Default.Build,
                    contentDescription = stringResource(R.string.cd_dialog_icon),
                )
            },
            title = {
                Text(text = stringResource(errorDataValue.title))
            },
            text = {
                Text(text = stringResource(errorDataValue.message))
            },
            onDismissRequest = {
                viewModel.clearError()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.refreshFeeds()
                    }
                ) {
                    Text(stringResource(R.string.action_retry))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.clearError()
                    }
                ) {
                    Text(stringResource(R.string.action_close))
                }
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_6)
@Composable
private fun HomeScreenPreview() {
    val viewModelContract: MainViewModelContract = object : MainViewModelContract {
        override val state: MutableStateFlow<ViewState>
            get() = MutableStateFlow(
                ViewState(
                    items = persistentListOf(
                        Item(
                            title = "Item 2",
                            media = Media(imageUrl = "https://example.com/image1.jpg")
                        ),
                        Item(
                            title = "Item 2",
                            media = Media(imageUrl = "https://example.com/image2.jpg")
                        ),
                        Item(
                            title = "Item 3",
                            media = Media(imageUrl = "https://example.com/image2.jpg")
                        )
                    )
                )
            )
    }

    ApplicationTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModelContract,
            onItemTapped = { /* Handle item tap */ }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_6)
@Composable
private fun HomeScreenEmptyPreview() {
    val viewModelContract: MainViewModelContract = object : MainViewModelContract {
        override val state: MutableStateFlow<ViewState>
            get() = MutableStateFlow(
                ViewState()
            )
    }

    ApplicationTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModelContract,
            onItemTapped = { /* Handle item tap */ }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_6)
@Composable
private fun HomeScreenErrorPreview() {
    val viewModelContract: MainViewModelContract = object : MainViewModelContract {
        override val state: MutableStateFlow<ViewState>
            get() = MutableStateFlow(
                ViewState(
                    errorData = ErrorData(
                        title = R.string.error_network_title,
                        message = R.string.error_network_message
                    )
                )
            )
    }

    ApplicationTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModelContract,
            onItemTapped = { /* Handle item tap */ }
        )
    }
}