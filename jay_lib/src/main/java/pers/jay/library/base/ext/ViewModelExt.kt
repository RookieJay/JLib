package pers.jay.library.base.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

fun <T : ViewModel> ViewModelStoreOwner.getViewModel(modelClass: Class<T>): T {
    return ViewModelProvider(this)[modelClass]
}

fun <T : ViewModel> ViewModelStoreOwner.getViewModel(key: String, modelClass: Class<T>): T {
    return ViewModelProvider(this)[key, modelClass]
}

