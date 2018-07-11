package io.github.ruskonert.ruskit.command.plugin.policy

import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.misc.Parameter

class PolicyStatusCommand : RuskitCommand<PolicyStatusCommand>("status")
{
    init {
        this.setPermission("status")
        this.addParameter(Parameter("pluginname", true))
        this.setCommandDescription("Show status of handled classes or plugin")
    }
}