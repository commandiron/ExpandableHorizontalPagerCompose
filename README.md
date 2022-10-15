# ExpandableHorizontalPagerCompose  [![](https://jitpack.io/v/commandiron/ExpandableHorizontalPagerCompose.svg)](https://jitpack.io/#commandiron/ExpandableHorizontalPagerCompose)

Add Expandable Content (Image etc.) Horizontal Pager in Android Jetpack Compose.

## How it looks

<img src="art/expandable_horizontal_pager_2.gif" width="250" height="530">

## Usage

```kotlin
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
```


## Setup
1. Open the file `settings.gradle` (it looks like that)
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add jitpack here 👇🏽
        maven { url 'https://jitpack.io' }
       ...
    }
} 
...
```
2. Sync the project
3. Add dependencies
```groovy
dependencies {
        implementation "com.google.accompanist:accompanist-pager:0.26.5-rc"
        implementation 'com.github.commandiron:ExpandableHorizontalPagerCompose:1.0.11'
}
```
