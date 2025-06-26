package com.example.app.viewModel

import com.example.app.data.models.Item
import com.example.app.data.models.Media
import com.example.app.data.models.Result
import com.example.app.domain.repository.DataFeedRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var result: Result

    private fun setUpSuccess() {
        viewModel = MainViewModel(object : DataFeedRepository {
            override suspend fun getFeedData(
                tags: String,
                onSuccess: (Result) -> Unit,
                onError: (Throwable) -> Unit
            ) {
                val mockResult = result
                onSuccess(mockResult)
            }
        })
    }

    private fun setUpError() {
        viewModel = MainViewModel(object : DataFeedRepository {
            override suspend fun getFeedData(
                tags: String,
                onSuccess: (Result) -> Unit,
                onError: (Throwable) -> Unit
            ) {
                onError(NullPointerException())
            }
        })
    }

    @Before
    fun setUp() {
        result = Result(
            title = "Some title", items = listOf(
                Item(
                    title = "Item 1",
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
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetFeedsSuccess() = runTest {
        setUpSuccess()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            viewModel.getFeeds()
            assertEquals(false, viewModel.state.value.isLoading)
            assertEquals(result.items.size, viewModel.state.value.items?.size)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetFeedsError() = runTest {
        setUpError()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            viewModel.getFeeds()
            assertNotNull(viewModel.state.value.errorData)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetFeedsErrorAndClearError() = runTest {
        setUpError()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            viewModel.getFeeds()
            assertNotNull(viewModel.state.value.errorData)
            viewModel.clearError()
            assertNull(viewModel.state.value.errorData)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testSetQuery() = runTest {
        setUpSuccess()
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val query = "new query"
            viewModel.setQuery(query)
            assertEquals(query, viewModel.state.value.query)
        } finally {
            Dispatchers.resetMain()
        }
    }
}