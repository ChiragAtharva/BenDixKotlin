package com.bendix.base

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.bendix.BR
import com.bendix.utility.BindableDelegates

open class ParentBaseObservable : BaseObservable() {

    @get:Bindable
    var loadingProgress: Boolean by BindableDelegates(false, BR.loadingProgress)
}