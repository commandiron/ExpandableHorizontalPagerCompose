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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ExpandableHorizontalPager(
    count: Int,
    modifier: Modifier = Modifier,
    reverseLayout: Boolean = false,
    itemSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    key: ((page: Int) -> Any)? = null,
    userScrollEnabled: Boolean = true,
    initialWidth: Dp = 120.dp,
    targetWidth: Dp = 400.dp,
    aspectRatio: Float = 2/3f,
    durationMillis: Int = 400,
    mainContent: @Composable ColumnScope.(page: Int, expanded: Boolean) -> Unit,
    overMainContentExpanded: @Composable ColumnScope.(page: Int) -> Unit,
    overMainContentCollapsed: @Composable ColumnScope.(page: Int) -> Unit,
    hiddenContent: @Composable ColumnScope.(page: Int) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    var isAnimationFinished by remember { mutableStateOf(false) }

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
            isAnimationFinished = !isAnimationFinished
        }
    )

    var cornerSizeState by remember { mutableStateOf(16.dp) }
    val cornerSize by animateDpAsState(
        targetValue = cornerSizeState,
        animationSpec = tween(
            durationMillis = durationMillis
        )
    )

    fun expand(maxHeight: Dp) {
        if (!expanded) {
            contentWidthState = targetWidth
            contentOffSetYState =
                -((maxHeight - (contentWidthState * 1 / aspectRatio)) / 2)

            boxHeightState = maxHeight - targetWidth * 1 / aspectRatio
            boxOffSetYState = (contentWidthState * 1 / aspectRatio) / 2

            cornerSizeState = 0.dp
        } else {
            contentWidthState = initialWidth
            contentOffSetYState = 0.dp

            boxHeightState = 0.dp
            boxOffSetYState = 0.dp

            cornerSizeState = 16.dp
        }
        expanded = !expanded
    }

    HorizontalPager(
        count = count,
        modifier = modifier,
        reverseLayout = reverseLayout,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        verticalAlignment = verticalAlignment,
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
                    .height(boxHeight)
                    .offset(
                        x = 0.dp,
                        y = boxOffSetY
                    ),
                shape = RoundedCornerShape(cornerSize),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(0.95f),
                    contentColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .drawWithContent {
                            if (isAnimationFinished) {
                                drawContent()
                            }
                        }
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState {},
                            onDragStarted = { expand(this@BoxWithConstraints.maxHeight) }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(expanded) {
                        hiddenContent(page)
                    }
                }
            }
            Card(
                modifier = Modifier
                    .width(contentWidth)
                    .height(contentWidth * 1 / aspectRatio)
                    .aspectRatio(aspectRatio)
                    .offset(x = contentOffSetX, y = contentOffSetY)
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState {},
                        onDragStarted = { expand(maxHeight) }
                    )
                    .clickable { expand(maxHeight) },
                shape = RoundedCornerShape(cornerSize)
            ) {
                Box() {
                    Column() {
                        mainContent(page, expanded)
                    }
                    Column() {
                        AnimatedVisibility(
                            visible = !expanded,
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
                            visible = expanded,
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