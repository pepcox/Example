package com.example.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun rememberDebounced(
    interval: Duration = 500.milliseconds,
    onClick: () -> Unit,
): () -> Unit {
    class Holder(var time: Long?)

    val handle = rememberUpdatedState(newValue = onClick)
    val lastClickTime = remember { Holder(null) }

    return remember {
        {
            val time = lastClickTime.time

            val shouldHandleClick =
                time?.let { (System.currentTimeMillis() - it) > interval.inWholeMilliseconds }
                    ?: true

            if (shouldHandleClick) {
                lastClickTime.time = System.currentTimeMillis()
                handle.value.invoke()
            }
        }
    }
}