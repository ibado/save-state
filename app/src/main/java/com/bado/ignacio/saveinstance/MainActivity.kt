package com.bado.ignacio.saveinstance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bado.ignacio.safefield.Safe
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @Safe
    lateinit var field: String

    @JvmField
    @Safe
    var version: Double? = 1.0

    init {
        field = "fresh-state"
        version = 1.0
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text.text = "$field $version"

        fab.setOnClickListener { view ->
            field = "state-changed"
            version = 2.0
            text.text = "$field $version"
        }
    }
}
