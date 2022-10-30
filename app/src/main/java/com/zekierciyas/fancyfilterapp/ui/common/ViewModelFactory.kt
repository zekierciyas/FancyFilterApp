package com.zekierciyas.fancyfilterapp.ui.common

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

/**
 * Get a [ViewModel] in an [ComponentActivity].
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModelBuilder(
    noinline viewModelInitializer: () -> VM
): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            return@ViewModelLazy object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")// Casting T as ViewModel
                    return viewModelInitializer.invoke() as T
                }
            }
        }
    )
}

/**
 * Get a [ViewModel] in a [Fragment].
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModelBuilder(
    noinline viewModelInitializer: () -> VM
): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { requireActivity().viewModelStore },
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")// Casting T as ViewModel
                    return viewModelInitializer.invoke() as T
                }
            }
        }
    )
}