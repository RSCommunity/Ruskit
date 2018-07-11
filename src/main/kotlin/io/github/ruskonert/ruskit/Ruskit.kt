package io.github.ruskonert.ruskit

import io.github.ruskonert.ruskit.command.RuskitPluginCommand
import io.github.ruskonert.ruskit.engine.plugin.CommandRegistration
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.plugin.RuskitServerPlugin

class Ruskit : IntegratedPlugin()
{
    companion object {
        private var i : RuskitServerPlugin? = null
        fun getInstance() : RuskitServerPlugin? = i
    }

    var settings : Map<String, Any> = HashMap()

    override fun onInit(handleInstance: Any?): Any?
    {
        super.onInit(this)
        this.registerSustainableHandlers(CommandRegistration::class.java, RuskitPluginCommand::class.java)
        return true
    }
}