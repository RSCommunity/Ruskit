package io.github.ruskonert.ruskit.command.plugin

import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.misc.Parameter

class PluginCommand : RuskitCommand<PluginCommand>("plugin", "pl")
{
    init {
        this.setCommandDescription("Show all plugins that depend on RuskitIntegratedPlugin")
        this.setPermission("plugin")
        this.setDefaultUser(false)
        this.addParameter(Parameter("plugin_name", true), Parameter("options"))
    }
}
