package com.example.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.R
import com.example.app.com.example.app.data.models.ViewState
import com.example.app.data.models.ErrorData
import com.example.app.domain.repository.DataFeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataFeedRepository
) : ViewModel(), MainViewModelContract {

    override val state = MutableStateFlow(ViewState())

    init {
        getFeeds()
    }

    override fun getFeeds() {
        val formatedTags = state.value.query
            .trim()
            .replace(", ", ",")
            .replace(" ", ",")
        println("Formatted Tags: $formatedTags")
        viewModelScope.launch {
            state.update { it.copy(errorData = null, isLoading = true) }
            repository.getFeedData(
                tags = formatedTags,
                onSuccess = { feedResult ->
                    state.update {
                        it.copy(
                            items = feedResult.items.toImmutableList(),
                            isLoading = false
                        )
                    }
                },
                onError = { error ->
                    state.update { it.copy(errorData = error.parseError(), isLoading = false) }
                }
            )
        }
    }

    override fun refreshFeeds() {
        getFeeds()
    }

    override fun clearError() {
        state.update { it.copy(errorData = null) }
    }

    override fun setQuery(query: String) {
        state.update { it.copy(query = query) }
    }
}

/**
 * MainViewModelContract defines the contract for the MainViewModel. Implementing of the contract helps
 * to create screens previews in IDE and also allows for easier testing and mocking.
 */
interface MainViewModelContract {
    fun getFeeds() {}
    fun refreshFeeds() {}
    fun clearError() {}
    fun setQuery(query: String) {}

    val state: MutableStateFlow<ViewState>
}

private fun Throwable.parseError(): ErrorData {
    return when (this) {
        is HttpException, is UnknownHostException -> ErrorData(
            title = R.string.error_network_title,
            message = R.string.error_network_message,
        )

        else -> ErrorData(
            title = R.string.error_general_title,
            message = R.string.error_general_message,
        )
    }
}

