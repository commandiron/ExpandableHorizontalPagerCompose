# ExpandableHorizontalPagerCompose

# WheelPickerCompose [![](https://jitpack.io/v/commandiron/WheelPickerCompose.svg)](https://jitpack.io/#commandiron/WheelPickerCompose)

Add Wheel Date - Time Picker in Android Jetpack Compose.

## Usage
|Picker|Usage|
|------|-----|
|<img src="https://user-images.githubusercontent.com/50905347/189122534-72e2140f-e5cf-414c-897d-36e6876555a1.gif" width="256" height="256">|```WheelDateTimePicker { snappedDateTime -> }```|
|<img src="https://user-images.githubusercontent.com/50905347/189132165-6d2611a2-4f41-467d-900a-34d87dbbc68c.gif" width="256" height="256">|```WheelDatePicker { snappedDate -> }```|
|<img src="https://user-images.githubusercontent.com/50905347/189145244-887aac1c-17aa-4f65-9049-252898e28a30.gif" width="256" height="256">|```WheelTimePicker { snappedTime -> }```|
|<img src="https://user-images.githubusercontent.com/50905347/189645296-cc9733fa-52bd-46e2-897a-9a256275209b.gif" width="256" height="256">|```WheelTextPicker(texts = (1..6).map { "Text $it" })```|
|<img src="https://user-images.githubusercontent.com/50905347/189134369-8c01dba5-4331-474d-8010-d3926c8fe669.gif" width="256" height="256">|```WheelPicker(count = 6) { index, snappedIndex ->```<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;```Card(Modifier.size(128.dp).padding(8.dp)) {}```<br/>```}```|


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
        implementation 'com.github.commandiron:WheelPickerCompose:1.0.10'
}
```

## Features

<table>
<tr>
<td>
            
```kotlin  
WheelDateTimePicker(
    startDateTime = LocalDateTime.of(
        2025, 10, 30, 5, 0
    ),
    minYear = 1950,
    maxYear = 2050,
    disablePastDateTime = true,
    size = DpSize(200.dp, 100.dp),
    textStyle = MaterialTheme.typography.titleSmall,
    textColor = Color(0xFFffc300),
    selectorEnabled = true,
    selectorShape = RoundedCornerShape(0.dp),
    selectorColor = Color(0xFFf1faee).copy(alpha = 0.2f),
    selectorBorder = BorderStroke(2.dp, Color(0xFFf1faee))
) { snappedDateTime -> }
```
</td>
<td>  
    
<img src="https://user-images.githubusercontent.com/50905347/190863946-3b095696-a4be-4d29-ad01-9489bb2ddb27.gif" width="256" height="256">
    
</td>
</tr>
</table>
