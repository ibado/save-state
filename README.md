# save-state
Android library to save Activities's state automatically using an annotation


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
