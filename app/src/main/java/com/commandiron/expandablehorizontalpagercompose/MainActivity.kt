package com.commandiron.expandablehorizontalpagercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.commandiron.expandable_horizontal_pager.ExpandableHorizontalPager
import com.commandiron.expandablehorizontalpagercompose.Film.Companion.films
import com.commandiron.expandablehorizontalpagercompose.ui.theme.ExpandableHorizontalPagerComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ExpandableHorizontalPagerComposeTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                DisposableEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    onDispose {}
                }
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ExpandableHorizontalPager(
                        count = films.size,
                        initialHorizontalPadding = 64.dp,
                        initialWidth = 240.dp,
                        targetWidth = maxWidth,
                        mainContent = { page, isExpanded ->
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(films[page].imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight
                            )
                        },
                        overMainContentCollapsed = { page ->
                            OverMainContent(
                                title = "Details",
                                imageVector = Icons.Default.KeyboardArrowDown
                            )
                        },
                        overMainContentExpanded = { page ->
                            OverMainContent(
                                title = "Close",
                                imageVector = Icons.Default.KeyboardArrowUp,
                                iconOnTop = true
                            )
                        },
                        hiddenContent = { page ->
                            HiddenContent(
                                title = films[page].title,
                                overview = films[page].overview
                            )
                        },
                        onTransform = { isExpanded -> }
                    )
                }
            }
        }
    }
}

@Composable
fun OverMainContent(
    title: String,
    imageVector: ImageVector,
    iconOnTop: Boolean = false
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
        if(iconOnTop) {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.White
        )
        if(!iconOnTop) {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun HiddenContent(title: String, overview: String) {
    Column(Modifier.padding(16.dp)) {
        Text(text = title)
        Spacer(Modifier.height(8.dp))
        Text(text = overview)
    }
}

data class Film(
    val imageUrl: String,
    val title: String,
    val overview: String
) {
    companion object {
        val films = listOf(
            Film(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                title = "Lord Of The Rings",
                overview = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            ),
            Film(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                title = "Lord Of The Rings",
                overview = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            ),
            Film(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                title = "Lord Of The Rings",
                overview = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            ),
            Film(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                title = "Lord Of The Rings",
                overview = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            ),
            Film(
                imageUrl = "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                title = "Lord Of The Rings",
                overview = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
            ),
        )
    }
}