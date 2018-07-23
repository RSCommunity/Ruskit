package io.github.ruskonert.ruskit.entity.inventory

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import io.github.ruskonert.ruskit.component.JsonCompatibleSerializer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import java.lang.reflect.Type

open class InventoryComponent : JsonCompatibleSerializer<InventoryComponent>()
{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): InventoryComponent {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun serialize(src: InventoryComponent?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement
    {
        var jsonObject = JsonObject()
    }

    private var material : Material = Material.GRASS

    private var isGlow : Boolean = false

    private var meterialData : Byte = 0

    private var materialName : String = "Default"

    private var amount : Int = 1
    fun setAmount(value : Int) {
        this.amount = value
        this.itemStack.amount = this.amount
    }

    private var damaged : Short = 0
    fun setDamage(value : Short) {
        this.damaged = value
        this.itemStack.durability = damaged
    }

    private val itemStack : ItemStack = ItemStack(material, amount, damaged)

    @Synchronized
    fun toItemStack() : ItemStack = itemStack


    private val description: ArrayList<String> = ArrayList()
    fun setDescription(index : Int, decsription : String) {
        this.description[index] = decsription
    }

    private var hoverFunction : ((Player) -> Boolean)? = null
    fun setHover(function : (Player) -> Boolean) { this.hoverFunction = function }

    private var clickFunction : ((Player) -> Boolean)? = null
    fun setClick(function : (Player) -> Boolean) { this.clickFunction = function }
    /*
    fun toItemStack() : ItemStack
    {
        val itemStack = ItemStack(material, amount, damaged)
        val meta = itemStack.itemMeta
        meta.displayName = materialName
        meta.lore = this.description
        itemStack.itemMeta = meta
        return itemStack
    }
    */
}