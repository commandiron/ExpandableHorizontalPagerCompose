# ExpandableHorizontalPagerCompose  [![](https://jitpack.io/v/commandiron/ExpandableHorizontalPagerCompose.svg)](https://jitpack.io/#commandiron/ExpandableHorizontalPagerCompose)

Add Expandable Horizontal Pager in Android Jetpack Compose.

## How it looks

<img src="art/expandable_horizontal_pager.gif" width="250" height="530">

## Usage

```kotlin
ExpandableHorizontalPager(
    count = films.size,
    initialHorizontalPadding = 64.dp,
    initialWidth = 240.dp,
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
        OverMainContent(
            title = "Details",
            imageVector = Icons.Default.KeyboardArrowDown
        )
    },
    overMainContentCollapsed = { page ->
        OverMainContent(
            title = "Close",
            imageVector = Icons.Default.KeyboardArrowUp,
            iconOnTop = true
        )
    },
    HiddenContent(
        title = films[page].title,
        overview = films[page].overview
    )
)
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
3. Add dependency
```groovy
dependencies {
        implementation 'com.github.commandiron:ExpandableHorizontalPagerCompose:1.0.3'
}
```
