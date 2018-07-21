package io.github.ruskonert.ruskit.engine.plugin

import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.RuskitCommandBase
import io.github.ruskonert.ruskit.command.plugin.DocumentCommand
import io.github.ruskonert.ruskit.engine.RuskitThread
import io.github.ruskonert.ruskit.event.RuskitCommandEvent
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.util.ReflectionUtility

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.EventHandler

import java.util.*

class CommandRegistration : RuskitThread()
{
    override fun onInit(handleInstance: Any?): Any?
    {
        CommandRegistration.registerCommand()
        return true
    }

    @EventHandler
    fun onPerform(event : RuskitCommandEvent)
    {
        if(event.vcommand is DocumentCommand)
        {
            if(event.handleInstance == null)
                event.handleInstance = event.vcommand
        }
    }

    companion object
    {
        private val instance : CommandRegistration = CommandRegistration()
        @JvmStatic fun getInstance() : CommandRegistration = instance

        private fun registerCommand()
        {
            val commandMap = simpleCommandMap
            val knownCommands = getSimpleCommandMapRegistered(commandMap)
            val nameTargets = HashMap<String, RuskitCommand<*>>()
            for (abstractCommand in RuskitCommand.EntireCommand())
            {
                //commandAlias = commandAlias.trim { it <= ' ' }.toLowerCase()
                nameTargets[abstractCommand.getCommand()] = abstractCommand
            }

            for ((name, target) in nameTargets)
            {
                target.setEnabled(IntegratedPlugin.CorePlugin)
                val current = knownCommands[name]
                val commandTarget = getRuskitCommand(current)

                if (target === commandTarget) continue

                if (current != null)
                {
                    knownCommands.remove(name)
                    current.unregister(commandMap)
                }

                val command = RuskitCommandBase(name, target)

                val plugin = command.basedRuskitCommand.getPlugin()
                val pluginName = if (plugin != null) plugin.name else "RuskitIntegratedPlugin"
                commandMap.register(pluginName, command)
            }
        }

        private fun getRuskitCommand(command: Command?): RuskitCommand<*>?
        {
            if (command == null) return null
            if (command !is RuskitCommandBase) return null
            val cbc = command as RuskitCommandBase?
            return cbc!!.basedRuskitCommand
        }

        private var commandMapField = ReflectionUtility.GetField(Bukkit.getServer().javaClass, "commandMap")
        private var simpleCommandField = ReflectionUtility.GetField(SimpleCommandMap::class.java, "knownCommands")

        private val simpleCommandMap: SimpleCommandMap
            get() {
                val server = Bukkit.getServer()
                return ReflectionUtility.GetField(commandMapField!!, server)
            }

        private fun getSimpleCommandMapRegistered(simpleCommandMap: SimpleCommandMap): HashMap<String, Command>
        {
            return ReflectionUtility.GetField(simpleCommandField!!, simpleCommandMap)
        }
    }
}