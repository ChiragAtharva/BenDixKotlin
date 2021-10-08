package com.bendix.base

import androidx.lifecycle.ViewModel
import com.bendix.SingleLiveEvent

open class BaseViewModel: ViewModel() {

    val messageEvent = SingleLiveEvent<String>()

}