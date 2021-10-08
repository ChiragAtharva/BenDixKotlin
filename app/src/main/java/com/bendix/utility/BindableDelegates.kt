package com.bendix.utility

import androidx.databinding.BaseObservable
import kotlin.reflect.KProperty

class BindableDelegates<in R : BaseObservable, T : Any>  (private var value: T, private val bindingEntry : Int) {
    operator fun getValue(thisRef: R, property: KProperty<*>): T = value
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.notifyPropertyChanged(bindingEntry)
    }
}