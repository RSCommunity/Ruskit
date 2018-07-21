package io.github.ruskonert.ruskit.command

import io.github.ruskonert.ruskit.command.misc.CommandOrder
import org.bukkit.command.CommandSender

interface Document {
    fun output(listener   : CommandSender,
               targetPage : Int,
               sizeOfLine : Int,
               rawType    : Boolean,
               order      : CommandOrder = CommandOrder.ALPHABET) : Any?
}
