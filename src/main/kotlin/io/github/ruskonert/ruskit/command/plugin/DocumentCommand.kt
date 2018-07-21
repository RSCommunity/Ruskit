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
package io.github.ruskonert.ruskit.command.plugin

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
    @Suppress("FunctionName")
    companion object
    {
        private var documentSize0: Int = 6
        fun SetDocumentSize(size: Int) { documentSize0 = size }
        fun DocumentSize(): Int = documentSize0
    }

    init
    {
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
            0 -> this.output(sender, 0, DocumentSize(), false, CommandOrder.ALPHABET)
            else -> this.output(sender, argv!![0].toInt(), DocumentSize(), false, CommandOrder.ALPHABET)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun output(listener: CommandSender, targetPage: Int, sizeOfLine: Int, rawType: Boolean, order: CommandOrder): Any?
    {
        val messageHandler = this.getPlugin()!!.getMessageHandler()
        if(targetPage < 0)
        {
            messageHandler.defaultMessage("&4Invalid page number.", listener)
            return null
        }

        val commandList = ArrayList<RuskitCommand<*>>()
        val outputList = ArrayList<ComponentString>()

        commandList.addAll(this.getChildCommands())
        commandList.addAll(this.getExternalCommands())

        if(commandList.size - 1 == 0)
        {
            messageHandler.defaultMessage("&cSorry, No provided document on this command because there's no command.", listener)
            return null
        }

        CommandUtility.sortCommand(order, commandList)

        var startRange = 0
        val endRange: Int

        val commandSlashHeader = fun() : FormatDescription {
            val formatDescription = FormatDescription("")
            formatDescription.append("&6/")
            return formatDescription
        }

        when(listener)
        {
            is Player ->
            {
                if(targetPage == 0 || targetPage == 1)  { endRange = if(commandList.size <= DocumentSize()) commandList.size - 1 else sizeOfLine - 1 }
                else
                {
                    startRange = (targetPage * sizeOfLine) - 1
                    endRange = startRange + sizeOfLine - 1
                }

                for(value in commandList.subList(startRange, endRange))
                {
                    // Make a part description:
                    // &6/[currentCommand] [subCommand] : [Description]
                    val framework = ComponentBuilder("").append(CommandUtility.toBaseComponent(commandSlashHeader()) as ComponentString)
                    val currentCommand = this.getCurrentCommand(listener)
                    for((key, pairValue) in currentCommand.selectorList)
                        currentCommand.setDescriptionSelector(key, pairValue)

                    framework.append(CommandUtility.toBaseComponent(currentCommand) as ComponentString)
                    framework.append(" : ")
                    framework.append(CommandUtility.toBaseComponent(this.getCommandDescription()) as ComponentString)
                    outputList.add(framework.create())
                }
            }
            is ConsoleCommandSender -> {
                endRange = commandList.size - 1
                for(value in commandList.subList(startRange, endRange))
                {
                    var framework : String = commandSlashHeader().rawMessage()

                    // append a current command
                    framework += value.getRawCurrentCommand(listener)
                    framework += " : "
                    framework += value.getCommandDescription().rawMessage()
                    outputList.add(CommandUtility.toBaseComponent(FormatDescription(framework)) as ComponentString)
                }
            }
        }
        return Page(outputList).execute(listener, ArrayList())
    }
}