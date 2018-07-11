package io.github.ruskonert.ruskit.event

import io.github.ruskonert.ruskit.command.RuskitCommand
import org.bukkit.command.CommandSender
import org.bukkit.event.HandlerList

open class RuskitCommandEvent(var sender : CommandSender, var vcommand : RuskitCommand<*>, val argv : List<String>, var handleInstance : Any?) : AbstractEvent()
{
    companion object { private val handler = HandlerList()
        @JvmStatic
        fun getHandlerList() : HandlerList = handler
    }
    override fun getHandlers(): HandlerList = handler
}
