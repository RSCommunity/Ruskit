/*
Copyright (c) 2018 ruskonert
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom t.7e Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.ruskonert.ruskit.command.plugin.document

import io.github.ruskonert.ruskit.command.Document
import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.entity.ComponentString
import io.github.ruskonert.ruskit.command.entity.Page
import io.github.ruskonert.ruskit.command.misc.CommandOrder
import io.github.ruskonert.ruskit.command.misc.Parameter
import io.github.ruskonert.ruskit.component.FormatDescription
import io.github.ruskonert.ruskit.util.CommandUtility
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

open class DocumentCommand : RuskitCommand<DocumentCommand>("help", "page", "?"), Document // It sames this.addAlias(arrayOf("page", "?"))
{
    companion object
    {
        private var documentSize0: Int = 6
        fun setDocumentSize(size: Int)
        {
            documentSize0 = size
        }
        fun getDocumentSize(): Int = documentSize0
    }

    init
    {
        // {plugin_name} is already defined specific value.
        // You can use to replace a value as the server environment.
        this.setCommandDescription("Show all {plugin_name}'s command description")
        this.setPermission("help")
        this.addParameter(Parameter("page", false))
        this.setDefaultOP(true)
        this.setDefaultUser(true)
    }

    override fun perform(sender: CommandSender, argc: Int, argv: List<String>?, handleInstance: Any?): Any?
    {
        return when(argc)
        {
            0 -> this.output(sender, 0, getDocumentSize(), false, handleInstance as RuskitCommand<*>?)
            else -> this.output(sender, argv!![0].toInt(), getDocumentSize(), false, handleInstance as RuskitCommand<*>?)
        }
    }

    @Suppress("UNCHECKED_CAST")
    open fun output(to: CommandSender, page: Int = 0, lineAmount: Int = getDocumentSize(), rawType: Boolean = false, target : RuskitCommand<*>? = null,
                    order : CommandOrder = CommandOrder.ALPHABET) : Any? {
        var t : RuskitCommand<*> = this
        if(target != null)
            t = target

        if(!(to is Player || to is ConsoleCommandSender)) return null
        val outputList = ArrayList<ComponentString>()
        val list = ArrayList<RuskitCommand<*>>()
        list.addAll(t.getChildCommands())
        list.addAll(t.getExternalCommands())

        if(list.size - 1 == 0)
        {
            val messageHandler = target!!.getPlugin()!!.getMessageHandler()
            messageHandler.defaultMessage("&cSorry, No provided document on this command because there's no command.", to)
            return null
        }

        CommandUtility.sortCommand(order, list)

        list.add(0, this)

        var startRange = 0
        var endRange: Int

        val getSlashString = fun() : FormatDescription {
            val f = FormatDescription("")
            f.append("&e/")
            return f
        }

        when(to)
        {
            is Player -> {
                if(page == 0 || page == 1)  { endRange = if(list.size <= getDocumentSize()) list.size - 1 else lineAmount - 1 }
                else
                {
                    startRange = (page * lineAmount) - 1
                    endRange = startRange + lineAmount - 1
                }

                // Make a part description:
                // &6/[currentCommand] [subCommand] : [Description]
                var slash = getSlashString()
                var framework = ComponentBuilder("").append(CommandUtility.toBaseComponent(slash) as ComponentString)

                var currentCommand = t.getCurrentCommand(to) as FormatDescription
                for((k,v) in currentCommand.selectorList)
                    currentCommand.setDescriptionSelector(k, v)

                // append a tree command
                framework.append(CommandUtility.toBaseComponent(currentCommand) as ComponentString)
                framework.append(" : ")

                // append a command description.
                framework.append(CommandUtility.toBaseComponent(t.getCommandDescription()) as ComponentString)
                outputList.add(framework.create())

                // Makes parts of selected other command.
                for(value in list.subList(startRange, endRange))
                {
                    slash = getSlashString()
                    framework = ComponentBuilder("").append(CommandUtility.toBaseComponent(slash) as ComponentString)

                    currentCommand = value.getCurrentCommand(to) as FormatDescription
                    for((k,v) in currentCommand.selectorList)
                        currentCommand.setDescriptionSelector(k, v)

                    // append a tree command
                    framework.append(CommandUtility.toBaseComponent(currentCommand) as ComponentString)
                    framework.append(" : ")

                    // append a command description.
                    framework.append(CommandUtility.toBaseComponent(value.getCommandDescription()) as ComponentString)
                    outputList.add(framework.create())
                }
            }
            is ConsoleCommandSender ->
            {
                endRange = list.size - 1
                if(startRange == 0) endRange = list.size - 1
                for(value in list.subList(startRange, endRange))
                {
                    // It creates part : /pc1 pc2 .... currentCommand
                    var other : String = getSlashString().rawMessage()

                    // append a current command
                    other += value.getCurrentCommand(to, rawType = true) as String
                    other += " : "
                    other += value.getCommandDescription().rawMessage()

                    outputList.add(CommandUtility.toBaseComponent(FormatDescription(other)) as ComponentString)
                }
            }
            else ->
            {
                return null
            }
        }
        return Page(outputList).execute(to, ArrayList())
    }

    private fun <K, R> output0(value : K, func : (K) -> R) : Any?
    {
        val expressionFunc = fun() : R { return func(value) }
        return synchronized(this, expressionFunc)
    }
}