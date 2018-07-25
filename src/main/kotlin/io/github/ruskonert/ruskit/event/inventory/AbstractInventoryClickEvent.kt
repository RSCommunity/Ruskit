package io.github.ruskonert.ruskit.event.inventory

import io.github.ruskonert.ruskit.entity.AbstractInventory
import io.github.ruskonert.ruskit.entity.inventory.InventoryComponent
import org.bukkit.entity.Player

class AbstractInventoryClickEvent(inventory : AbstractInventory, var executor : Player, var slot : Int, var clicked : InventoryComponent?) : AbstractInventoryEvent(inventory)
