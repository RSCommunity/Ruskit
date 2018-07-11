@file:Suppress("UNCHECKED_CAST")

package io.github.ruskonert.ruskit.event

import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import java.util.*

abstract class AbstractEvent : Event(), Runnable, Cancellable
{
    private var cancel: Boolean = false
    private var customData: Map<Any, Any> = HashMap()

    final override fun run()
    {
        Bukkit.getPluginManager().callEvent(this)
    }

    override fun isCancelled(): Boolean
    {
        return this.cancel
    }

    override fun setCancelled(cancel: Boolean)
    {
        this.cancel = cancel
    }

    fun setCustomData(m: Map<*, *>)
    {
        this.customData = m as Map<Any, Any>
    }
}