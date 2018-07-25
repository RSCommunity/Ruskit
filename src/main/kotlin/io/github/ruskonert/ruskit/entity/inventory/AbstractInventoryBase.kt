package io.github.ruskonert.ruskit.entity.inventory

import org.bukkit.inventory.Inventory

interface AbstractInventoryBase
{
    fun getInventoryBase() : Inventory?

    fun getSlotComponents() : Map<Int, InventoryComponent?>
}