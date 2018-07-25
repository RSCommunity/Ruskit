package io.github.ruskonert.ruskit.event.config

import io.github.ruskonert.ruskit.config.SynchronizeReader
import io.github.ruskonert.ruskit.event.AbstractEvent
import org.bukkit.event.HandlerList

open class SynchronizeReaderEvent(var target: SynchronizeReader<*>) : AbstractEvent()
{
    companion object { private val handler = HandlerList()
        @JvmStatic
        fun getHandlerList() : HandlerList = handler
    }

    override fun getHandlers(): HandlerList = handler
}
