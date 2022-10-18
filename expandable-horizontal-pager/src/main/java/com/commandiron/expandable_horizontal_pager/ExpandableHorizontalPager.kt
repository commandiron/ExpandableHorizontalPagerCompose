package com.commandiron.expandable_horizontal_pager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExpandableHorizontalPager(
    count: Int,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
    reverseLayout: Boolean = false,
    itemSpacing: Dp = 0.dp,
    initialHorizontalPadding: Dp = 0.dp,
    targetHorizontalPadding: Dp = 0.dp,
    outerItemScaleEnabled: Boolean = true,
    outerItemScale: Float = 0.85f,
    outerItemAlphaEnabled: Boolean = true,
    outerItemAlpha: Float = 0.5f,
    key: ((page: Int) -> Any)? = null,
    userScrollEnabled: Boolean = true,
    initialWidth: Dp = 200.dp,
    targetWidth: Dp = 300.dp,
    initialAspectRatio: Float = 2 / 3f,
    targetAspectRatio: Float = 2 / 3f,
    durationMillis: Int = 400,
    mainContent: @Composable ColumnScope.(page: Int, isExpanded: Boolean) -> Unit,
    overMainContentCollapsed: @Composable ColumnScope.(page: Int) -> Unit = {},
    overMainContentExpanded: @Composable ColumnScope.(page: Int) -> Unit = {},
    hiddenContentBoxHeight: Dp = Dp.Unspecified,
    hiddenContentContainerColor: Color = Color.Black,
    hiddenContentContentColor: Color = Color.White,
    hiddenContent: @Composable ColumnScope.(page: Int) -> Unit,
    onTransform: (isExpanded: Boolean) -> Unit = {}
) {
    var transformState by rememberSaveable { mutableStateOf(ExpandablePagerTransformState.INITIAL) }

    fun setStateOnAnimationFinish(expandablePagerTransformState: ExpandablePagerTransformState) {
        when (expandablePagerTransformState) {
            ExpandablePagerTransformState.INITIAL_TO_TARGET -> {
                transformState = ExpandablePagerTransformState.TARGET
            }
            ExpandablePagerTransformState.TARGET_TO_INITIAL -> {
                transformState = ExpandablePagerTransformState.INITIAL
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
            setStateOnAnimationFinish(transformState)
        }
    )

    var aspectRatioState by remember { mutableStateOf(initialAspectRatio) }
    val aspectRatio by animateFloatAsState(
        targetValue = aspectRatioState,
        animationSpec = tween(
            durationMillis = durationMillis
        )
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
            setStateOnAnimationFinish(transformState)
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
        if(transformState == ExpandablePagerTransformState.INITIAL) {

            transformState = ExpandablePagerTransformState.INITIAL_TO_TARGET

            horizontalPaddingState = targetHorizontalPadding

            contentWidthState = targetWidth

            aspectRatioState = targetAspectRatio

            contentOffSetYState = -((maxHeight - (contentWidthState * 1 / targetAspectRatio)) / 2)

            boxHeightState = if(hiddenContentBoxHeight == Dp.Unspecified) {
                maxHeight - targetWidth * 1 / targetAspectRatio
            } else {
                hiddenContentBoxHeight
            }

            boxOffSetYState = if(hiddenContentBoxHeight == Dp.Unspecified) {
                (maxHeight - (maxHeight - targetWidth * 1 / targetAspectRatio)) / 2
            } else {
                - maxHeight / 2 + hiddenContentBoxHeight / 2 + targetWidth * 1 / targetAspectRatio
            }

            cornerSizeState = 0.dp
        } else if (transformState == ExpandablePagerTransformState.TARGET) {

            transformState = ExpandablePagerTransformState.TARGET_TO_INITIAL

            horizontalPaddingState = initialHorizontalPadding

            contentWidthState = initialWidth
            aspectRatioState = initialAspectRatio
            contentOffSetYState = 0.dp

            boxHeightState = 0.dp
            boxOffSetYState = 0.dp

            cornerSizeState = 16.dp
        }
    }

    HorizontalPager(
        count = count,
        modifier = modifier,
        state = state,
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
            val isExpanded = transformState == ExpandablePagerTransformState.TARGET ||
                    transformState == ExpandablePagerTransformState.INITIAL_TO_TARGET

            Card(
                modifier = Modifier
                    .width(contentWidth)
                    .height(
                        if (currentPage == page) {
                            boxHeight
                        } else {
                            if (transformState == ExpandablePagerTransformState.TARGET) boxHeight else 0.dp
                        }
                    )
                    .offset(
                        x = 0.dp,
                        y = boxOffSetY
                    )
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        if (transformState != ExpandablePagerTransformState.TARGET) {
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
                    containerColor =  hiddenContentContainerColor,
                    contentColor =  hiddenContentContentColor
                )
            ) {
                BoxWithConstraints(
                    contentAlignment = Alignment.Center
                ) {
                    Column() {
                        if(transformState == ExpandablePagerTransformState.TARGET) {
                            hiddenContent(page)
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .width(contentWidth)
                    .height(contentWidth * 1 / aspectRatio)
                    .offset(
                        x = contentOffSetX,
                        y = contentOffSetY
                    )
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        if (transformState != ExpandablePagerTransformState.TARGET) {
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
                            onTransform(!isExpanded)
                            expand(maxHeight)
                        }
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = currentPage == page,
                    ) {
                        onTransform(!isExpanded)
                        expand(maxHeight)
                    },
                shape = RoundedCornerShape(cornerSize)
            ) {
                Box() {
                    Column() {
                        mainContent(page, isExpanded)
                    }
                    Column() {
                        AnimatedVisibility(
                            visible = !isExpanded,
                            enter = fadeIn(tween(durationMillis)),
                            exit = fadeOut(tween(durationMillis))
                        ) {
                            overMainContentCollapsed(page)
                        }
                    }
                    Column() {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn(tween(durationMillis)),
                            exit = fadeOut(tween(durationMillis))
                        ) {
                            overMainContentExpanded(page)
                        }
                    }
                }
            }
        }
    }
}

enum class ExpandablePagerTransformState {
    INITIAL, INITIAL_TO_TARGET, TARGET_TO_INITIAL, TARGET
}