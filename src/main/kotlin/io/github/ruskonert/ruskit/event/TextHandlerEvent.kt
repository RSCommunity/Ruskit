package io.github.ruskonert.ruskit.event
import io.github.ruskonert.ruskit.command.TextHandler
import org.bukkit.event.HandlerList

class TextHandlerEvent(var textHandler : TextHandler<*>) : AbstractEvent()
{
    companion object { private val handler = HandlerList()
        @JvmStatic
        fun getHandlerList() : HandlerList = handler
    }
    override fun getHandlers(): HandlerList = handler
}