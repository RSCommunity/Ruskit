package io.github.ruskonert.ruskit.platform

import io.github.ruskonert.ruskit.Handle

/**
 * GenericInstance is a sub-class that can refer to an instance of a superclass type.
 * It can be referenced from outside without creating a separate instance.
 * @param C The type of inherited classes
 */
abstract class GenericInstance<C> : Handle
{
    protected var genericInstance : C? = null
    fun getSuperclassInstance() : C? = this.genericInstance

    @Suppress("UNCHECKED_CAST")
    override fun onInit(handleInstance: Any?): Any?
    {
        this.genericInstance = handleInstance as C?
        return this.genericInstance
    }
}