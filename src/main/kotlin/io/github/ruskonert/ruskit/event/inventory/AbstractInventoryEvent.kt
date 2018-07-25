package io.github.ruskonert.ruskit.event.inventory

import io.github.ruskonert.ruskit.entity.AbstractInventory
import io.github.ruskonert.ruskit.event.AbstractEvent
import org.bukkit.event.HandlerList

abstract class AbstractInventoryEvent(var inventory : AbstractInventory) : AbstractEvent()
{
    companion object { private val handler = HandlerList()
        @JvmStatic
        fun getHandlerList() : HandlerList = handler
    }

    override fun getHandlers(): HandlerList = handler
}

