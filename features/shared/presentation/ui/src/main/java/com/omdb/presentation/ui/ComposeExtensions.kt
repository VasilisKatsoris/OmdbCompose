package com.omdb.presentation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

inline fun Modifier.conditional(
    condition: Boolean,
    block: Modifier.() -> Modifier
) = if (condition) {
        this.block()
    } else {
        this
    }


fun Modifier.animateInFromRight(
    startDelay: Int = 0,
    duration: Int = 300,
    distanceX: Dp = 200.dp,
    isVisible : Boolean = true
): Modifier = composed {

    val isInPreview = LocalInspectionMode.current
    if (isInPreview) {
        return@composed this
    }

    var startAnimation by remember { mutableStateOf(false) }

    if(!isVisible){
        return@composed this.alpha(0f)
    }

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val offsetX by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else distanceX,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = startDelay
        ),
        label = "slideInX"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = startDelay
        ),
        label = "fadeInAlpha"
    )

    this.offset {
        IntOffset(offsetX.roundToPx(), 0)
    }.alpha(alpha)
}

fun Modifier.fadeInOnLoad(
    durationMillis: Int = 300,
): Modifier = this.then(
    Modifier.composed {

        val isInPreview = LocalInspectionMode.current
        if (isInPreview) {
            return@composed this
        }

        var isVisible by remember { mutableStateOf(false) }

        val alpha by animateFloatAsState(
            targetValue = if (isVisible) 1f else 0f,
            animationSpec = tween(durationMillis = durationMillis),
            label = "fadeInOnLoad"
        )

        LaunchedEffect(Unit) {
            isVisible = true
        }

        // Apply animated alpha
        Modifier.graphicsLayer(alpha = alpha)
    }
)