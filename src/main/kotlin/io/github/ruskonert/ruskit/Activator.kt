package io.github.ruskonert.ruskit

interface Activator<P>
{
    fun setEnabled(handleInstance : P)

    fun setEnabled(active : Boolean)

    fun isEnabled(): Boolean
}
