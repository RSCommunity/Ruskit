package io.github.ruskonert.ruskit.event

import io.github.ruskonert.ruskit.config.SynchronizeReader
import org.bukkit.event.HandlerList

open class SynchronizeReaderEvent(var target: SynchronizeReader) : AbstractEvent()
{
    companion object { private val handler = HandlerList()
        @JvmStatic
        fun getHandlerList() : HandlerList = handler
    }

    override fun getHandlers(): HandlerList = handler


}
