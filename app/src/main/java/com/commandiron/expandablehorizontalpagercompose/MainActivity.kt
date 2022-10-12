package com.commandiron.expandablehorizontalpagercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

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
                        initialHorizontalPadding = 80.dp,
                        targetWidth = maxWidth,
                        mainContent = { page ->
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
                        overMainContentExpanded = { page ->
                            OverMainContentExpanded(
                                title = "Details",
                                imageVector = Icons.Default.KeyboardArrowDown
                            )
                        },
                        overMainContentCollapsed = { page ->
                            OverMainContentExpanded(
                                title = "Close",
                                imageVector = Icons.Default.KeyboardArrowUp,
                                iconOnTop = true
                            )
                        },
                        hiddenContent = { page ->
                            Column(Modifier.padding(16.dp)) {
                                HiddenContent(page)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OverMainContentExpanded(
    title: String,
    imageVector: ImageVector,
    iconOnTop: Boolean = false
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

@Composable
fun HiddenContent(page: Int) {
    Text(text = films[page].title)
    Spacer(Modifier.height(8.dp))
    Text(text = films[page].overview)
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