package com.bado.ignacio.statesaver

import android.os.Bundle

interface Saver<T> {
    fun save(target: T, bundle: Bundle)
    fun restore(target: T, bundle: Bundle)
}