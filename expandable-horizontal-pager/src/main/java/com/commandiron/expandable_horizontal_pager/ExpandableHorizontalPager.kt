package com.commandiron.expandable_horizontal_pager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExpandableHorizontalPager(
    count: Int,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    itemSpacing: Dp = 0.dp,
    initialHorizontalPadding: Dp = 64.dp,
    targetHorizontalPadding: Dp = 0.dp,
    outerItemScaleEnabled: Boolean = true,
    outerItemScale: Float = 0.85f,
    outerItemAlphaEnabled: Boolean = true,
    outerItemAlpha: Float = 0.5f,
    key: ((page: Int) -> Any)? = null,
    userScrollEnabled: Boolean = true,
    initialWidth: Dp = 200.dp,
    targetWidth: Dp = 300.dp,
    aspectRatio: Float = 2/3f,
    durationMillis: Int = 400,
    mainContent: @Composable ColumnScope.(page: Int) -> Unit,
    overMainContentExpanded: @Composable ColumnScope.(page: Int) -> Unit,
    overMainContentCollapsed: @Composable ColumnScope.(page: Int) -> Unit,
    hiddenContentBoxHeight: Dp = Dp.Unspecified,
    hiddenContent: @Composable ColumnScope.(page: Int) -> Unit
) {
    var state by rememberSaveable { mutableStateOf(ExpandablePagerState.INITIAL) }

    fun animationFinish(expandablePagerState: ExpandablePagerState) {
        when (expandablePagerState) {
            ExpandablePagerState.INITIAL_TO_TARGET -> {
                state = ExpandablePagerState.TARGET
            }
            ExpandablePagerState.TARGET_TO_INITIAL -> {
                state = ExpandablePagerState.INITIAL
            }
            else -> {}
        }
    }

    val contentOffSetXState by remember { mutableStateOf(0.dp) }
    val contentOffSetX by animateDpAsState(
        targetValue = contentOffSetXState,
        animationSpec = tween(
            durationMillis = durationMillis
        )
    )

    var contentOffSetYState by remember { mutableStateOf(0.dp) }
    val contentOffSetY by animateDpAsState(
        targetValue = contentOffSetYState,
        animationSpec = tween(
            durationMillis = durationMillis
        )
    )

    var contentWidthState by remember { mutableStateOf(initialWidth) }
    val contentWidth by animateDpAsState(
        targetValue = contentWidthState,
        animationSpec = tween(
            durationMillis = durationMillis
        ),
        finishedListener = {
            animationFinish(state)
        }
    )

    var boxHeightState by remember { mutableStateOf(0.dp) }
    val boxHeight by animateDpAsState(
        targetValue = boxHeightState,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = 0
        )
    )

    var boxOffSetYState by remember { mutableStateOf(0.dp) }
    val boxOffSetY by animateDpAsState(
        targetValue = boxOffSetYState,
        animationSpec = tween(
            durationMillis = durationMillis
        ),
        finishedListener = {
            animationFinish(state)
        }
    )

    var cornerSizeState by remember { mutableStateOf(16.dp) }
    val cornerSize by animateDpAsState(
        targetValue = cornerSizeState,
        animationSpec = tween(
            durationMillis = durationMillis
        ),
    )

    var horizontalPaddingState by remember { mutableStateOf(initialHorizontalPadding) }
    val horizontalPadding by animateDpAsState(
        targetValue = horizontalPaddingState,
        animationSpec = tween(
            durationMillis = durationMillis
        )
    )

    fun expand(maxHeight: Dp) {
        if(state == ExpandablePagerState.INITIAL) {

            state = ExpandablePagerState.INITIAL_TO_TARGET

            horizontalPaddingState = targetHorizontalPadding

            contentWidthState = targetWidth
            contentOffSetYState = -((maxHeight - (contentWidthState * 1 / aspectRatio)) / 2)

            boxHeightState = if(hiddenContentBoxHeight == Dp.Unspecified) {
                maxHeight - targetWidth * 1 / aspectRatio
            } else {
                hiddenContentBoxHeight
            }

            boxOffSetYState = if(hiddenContentBoxHeight == Dp.Unspecified) {
                (maxHeight - (maxHeight - targetWidth * 1 / aspectRatio)) / 2
            } else {
                - maxHeight / 2 + hiddenContentBoxHeight / 2 + targetWidth * 1 / aspectRatio
            }

            cornerSizeState = 0.dp
        } else if (state == ExpandablePagerState.TARGET) {

            state = ExpandablePagerState.TARGET_TO_INITIAL

            horizontalPaddingState = initialHorizontalPadding

            contentWidthState = initialWidth
            contentOffSetYState = 0.dp

            boxHeightState = 0.dp
            boxOffSetYState = 0.dp

            cornerSizeState = 16.dp
        }


    }

    HorizontalPager(
        count = count,
        modifier = modifier,
        reverseLayout = reverseLayout,
        itemSpacing = itemSpacing,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        key = key,
        userScrollEnabled = userScrollEnabled
    ) { page ->
        BoxWithConstraints(
            Modifier
                .fillMaxSize(),
            Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(contentWidth)
                    .height(
                        if (currentPage == page) {
                            boxHeight
                        } else {
                            if(state == ExpandablePagerState.TARGET) {
                                boxHeight
                            } else {
                                0.dp
                            }
                        }
                    )
                    .offset(
                        x = 0.dp,
                        y = boxOffSetY
                    )
                    .graphicsLayer {
                        if (state != ExpandablePagerState.TARGET) {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            if (outerItemScaleEnabled) {
                                lerp(
                                    start = outerItemScale,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                            }
                            if (outerItemAlphaEnabled) {
                                alpha = lerp(
                                    start = outerItemAlpha,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                        }
                    },
                shape = RoundedCornerShape(cornerSize),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(0.95f),
                    contentColor = Color.White
                )
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState {},
                            onDragStarted = { expand(this@BoxWithConstraints.maxHeight) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column() {
                        if(state == ExpandablePagerState.TARGET) {
                            hiddenContent(page)
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .width(contentWidth)
                    .height(contentWidth * 1 / aspectRatio)
                    .aspectRatio(aspectRatio)
                    .offset(
                        x = contentOffSetX,
                        y = contentOffSetY
                    )
                    .graphicsLayer {
                        if (state != ExpandablePagerState.TARGET) {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                            if (outerItemScaleEnabled) {
                                lerp(
                                    start = outerItemScale,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                            }
                            if (outerItemAlphaEnabled) {
                                alpha = lerp(
                                    start = outerItemAlpha,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                        }
                    }
                    .draggable(
                        enabled = currentPage == page,
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState {},
                        onDragStarted = {
                            expand(maxHeight)
                        }
                    )
                    .clickable(
                        enabled = currentPage == page,
                    ) {
                        expand(maxHeight)
                    },
                shape = RoundedCornerShape(cornerSize)
            ) {
                Box() {
                    Column() {
                        mainContent(page)
                    }
                    Column() {
                        AnimatedVisibility(
                            visible = state == ExpandablePagerState.INITIAL ||
                                    state == ExpandablePagerState.TARGET_TO_INITIAL,
                            enter = fadeIn(tween(durationMillis)),
                            exit = fadeOut(tween(durationMillis))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.65f)
                                            )
                                        )
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                overMainContentExpanded(page)
                            }
                        }
                    }
                    Column() {
                        AnimatedVisibility(
                            visible = state == ExpandablePagerState.TARGET ||
                                    state == ExpandablePagerState.INITIAL_TO_TARGET,
                            enter = fadeIn(tween(durationMillis)),
                            exit = fadeOut(tween(durationMillis))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.65f)
                                            )
                                        )
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                overMainContentCollapsed(page)
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class ExpandablePagerState {
    INITIAL, INITIAL_TO_TARGET, TARGET_TO_INITIAL, TARGET
}