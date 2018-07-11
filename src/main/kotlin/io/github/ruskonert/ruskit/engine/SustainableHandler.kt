package io.github.ruskonert.ruskit.engine

import io.github.ruskonert.ruskit.Handle

abstract class SustainableHandler : Handle, Runnable
{
    private val customData : HashMap<Any, Any?> = HashMap()
    final override fun run()
    {
        this.onInit(customData)
    }
}