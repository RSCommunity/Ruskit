package io.github.ruskonert.ruskit.util

import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.entity.ComponentString
import io.github.ruskonert.ruskit.command.misc.CommandOrder
import io.github.ruskonert.ruskit.component.FormatDescription
import io.github.ruskonert.ruskit.component.Helper
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import java.util.*

object CommandUtility
{
    @Suppress("MemberVisibilityCanBePrivate")
    fun stringWithEscape(s : List<String>) : String
    {
        var string = ""
        for((i, str) in s.withIndex())
        {
            string += str
            if(s.size != i + 1)
            {
                string += "\n"
            }
        }
        return StringUtility.color(string)
    }

    /**
     * Sorts the child command or the specified command list.
     * @param order The sort type
     * @param otherList The target list to sort
     */
    fun sortCommand(order: CommandOrder, otherList: List<RuskitCommand<*>>)
    {
        // It is a variable used to store the value of the order first.
        var const = 1

        // This constant is the Comparable anonymous function needed to sort objects.
        // The alignment is based on the alphabetical order of the main command.
        val func = fun(o1: RuskitCommand<*>?, o2: RuskitCommand<*>?): Int
        {
            val s = arrayOf(o1!!.getCommand(), o2!!.getCommand())
            Arrays.sort(s)
            return when
            {
                o1.getCommand() == s[0] -> -const
                o1.getCommand() == s[1] -> const
                o1.getCommand() == o2.getCommand() -> 0
                else -> 0
            }
        }

        when (order)
        {
        // Sort alphabetically.
            CommandOrder.ALPHABET         -> { const *= -1; Collections.sort(otherList, func) }

        // Sort alphabetically by back-order.
            CommandOrder.ALPHABET_REVERSE -> Collections.sort(otherList, func)

        // Sort randomly. This is always different for each function call.
            CommandOrder.RANDOMIZE        -> Collections.shuffle(otherList, Random(System.nanoTime()))
        }
    }

    fun toBaseComponent(fd : FormatDescription, rawType: Boolean = false,  applyFill : Boolean = true) : Any?
    {
        var componentBuilder : ComponentBuilder? = null
        var selection = fd.getFormat()
        if(applyFill)
            selection = StringUtility.WithIndexString(selection, fd.getFilter())

        val indexArray = StringUtility.indexArgumentPosition(selection)
        val formatSplit = selection.split("\\{[0-9]}".toRegex()).toTypedArray()

        // If the split format is empty (= Not found current index)
        if(formatSplit.isEmpty())
            return if(rawType) componentBuilder else this.convertToColor(componentBuilder!!.create())
        else
        {
            val dummy : HoverEvent? = null
            for((index, value) in formatSplit.withIndex())
            {
                if(componentBuilder == null)
                    componentBuilder = ComponentBuilder(value)
                else
                    componentBuilder.append(value)

                componentBuilder.event(dummy)

                var hover : Pair<String, List<String>>? = null

                if(index + 1 != formatSplit.size)
                    hover = fd.selectorList[indexArray[index]]

                if(hover != null)
                {
                    // append value.
                    componentBuilder.append(hover.first)
                    // append with description.
                    componentBuilder.event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(stringWithEscape(hover.second)).create()))
                }

                if (fd.getClickEventList().containsKey(index))
                    componentBuilder.event(fd.getClickEventList()[index]!!.onLoad().second)

                if (fd.getHoverEventList().containsKey(index))
                    componentBuilder.event(fd.getHoverEventList()[index]!!.onLoad().second)
            }
        }
        return if(rawType) componentBuilder else this.convertToColor(componentBuilder!!.create())
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> convertToColor(out : T) : T?
    {
        when(out)
        {
            is Array<*> -> {
                return this.convertToColorArray(out as ComponentString) as T?
            }
            is ComponentBuilder -> {}
            is String -> return StringUtility.color(out) as T?
        }
        return null
    }

    fun convertToColorArray(out : Array<BaseComponent>) : ComponentString
    {
        val out0 = ArrayList<BaseComponent>()

        for(v in out) {

            val subArray = ArrayList<BaseComponent>()
            val target = v.toPlainText()

            if(Helper.containsString(target)) {
                var componentBuilder : ComponentBuilder? = null
                val hoverEvent = v.hoverEvent
                val clickEvent = v.clickEvent

                val find = target.split(("&[0-9a-z]").toRegex())
                var selected = 0

                for(sv in find) {

                    val t = target.indexOf(sv)
                    var colorcode: String?

                    colorcode = if(t == 0) {
                        target.substring(selected, selected + 2)
                    }
                    else {
                        target.substring(t - 2, t)
                    }

                    if(componentBuilder == null) componentBuilder = ComponentBuilder(sv)
                    else componentBuilder.append(sv)

                    val cc = Helper.getColorExpression(colorcode).colorType
                    this.applyColorCode(componentBuilder, cc)

                    componentBuilder.event(hoverEvent)
                    componentBuilder.event(clickEvent)
                    selected += sv.length + 2
                }
                subArray.addAll(componentBuilder!!.create())
            }
            else
            {
                subArray.add(v)
            }
            out0.addAll(subArray)
        }
        return out0.toTypedArray()
    }

    fun applyColorCode(componentBuilder : ComponentBuilder, cc : ChatColor)
    {
        when (cc)
        {
            ChatColor.BOLD -> componentBuilder.bold(true)

            ChatColor.ITALIC -> componentBuilder.italic(true)

            ChatColor.RESET -> componentBuilder.reset()

            ChatColor.MAGIC -> componentBuilder.obfuscated(true)

            ChatColor.UNDERLINE -> componentBuilder.underlined(true)

            ChatColor.STRIKETHROUGH -> componentBuilder.strikethrough(true)

            else -> {
                componentBuilder.color(cc)

                componentBuilder.bold(false)

                componentBuilder.italic(false)

                componentBuilder.obfuscated(false)

                componentBuilder.underlined(false)

                componentBuilder.strikethrough(false)
            }
        }
    }
}