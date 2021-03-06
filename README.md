# save-state
Android library to save Activities's state automatically using an annotation


# Usage example
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        StateSaver.saveActivitiesState(this)
    }
}


class MainActivity : AppCompatActivity() {

    @Safe
    lateinit var field: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            field = "state-changed"
        }
    }
}


```
# Installation

```gradle
// in your general build.gradle
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}

// in your module/app build.gradle
dependencies {
    //...
    implementation 'com.github.ibado:save-state:0.0.1'
    annotationProcessor 'com.github.ibado.save-state:processor:0.0.1'
    // for kotlin
    kapt 'com.github.ibado.save-state:processor:0.0.1'
}
 
```
