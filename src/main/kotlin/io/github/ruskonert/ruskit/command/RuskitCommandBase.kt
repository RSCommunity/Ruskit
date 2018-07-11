package io.github.ruskonert.ruskit.command

import io.github.ruskonert.ruskit.command.misc.Parameter
import io.github.ruskonert.ruskit.command.plugin.document.CmdDetailCommand
import io.github.ruskonert.ruskit.command.plugin.document.DocumentCommand
import io.github.ruskonert.ruskit.util.ReflectionUtility
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginIdentifiableCommand
import org.bukkit.plugin.Plugin
import java.lang.reflect.Method
import java.util.*

open class RuskitCommandBase(name: String, command: RuskitCommand<*>) :
        Command(name, command.getCommandDescription().getFormat(), command.getPermissionMessage()!!.rawMessage(), command.getAlias()), PluginIdentifiableCommand
{
    var ruskitCommand: RuskitCommand<*>
        protected set

    init
    {
        this.ruskitCommand = command

        if(command.getChildCommands().isNotEmpty())
        {
            this.additionalDocument(DocumentCommand())
            if(!command.hasParameter() && !command.isRoot())
            {
                val defaultParameter = ArrayList<Parameter>()
                defaultParameter.add(Parameter("args", true))
                val parameterField = command::class.java.superclass.getDeclaredField("params")
                ReflectionUtility.SetField(parameterField, command, defaultParameter)
            }
            this.defaultFilter()
        }
        else
        {
            this.additionalDocument(CmdDetailCommand())
        }
    }

    open fun defaultFilter()
    {
        for(c in this.ruskitCommand.getChildCommands())
        {
            // Define default filters.
            c.getCommandDescription().addFilter("plugin_name", ruskitCommand.getPlugin()!!.name)
            c.getCommandDescription().addFilter("server_name", Bukkit.getServerName())
            c.getCommandDescription().addFilter("server_time", Date().toString())
        }
    }

    open fun additionalDocument(document : Document)
    {
        for(c in this.ruskitCommand.getChildCommands())
        {
            val performMethod: Method = ReflectionUtility.MethodFromClass(c::class.java, "perform", onTargetOnly=true)!!

            if(!ReflectionUtility.IsImplemented(performMethod))
            {
                c.addChildCommands(document as RuskitCommand<*>)
            }
        }
    }

    override fun getPlugin(): Plugin
    {
        return this.ruskitCommand.getPlugin() as Plugin
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean
    {
        return this.ruskitCommand.execute(sender, ArrayList(args.asList())) != null
    }
}