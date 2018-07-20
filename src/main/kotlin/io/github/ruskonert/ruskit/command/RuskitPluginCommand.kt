package io.github.ruskonert.ruskit.command

import io.github.ruskonert.ruskit.command.plugin.*
import io.github.ruskonert.ruskit.command.plugin.document.DocumentCommand
import io.github.ruskonert.ruskit.command.plugin.policy.PolicyCommand

class RuskitPluginCommand : RuskitCommand<RuskitPluginCommand>("ruskit", "rusk", "rus")
{
    companion object
    {
        private val instance = RuskitPluginCommand()
        @JvmStatic fun getInstance() : RuskitPluginCommand = instance
    }

    private val documentCommand : DocumentCommand = DocumentCommand()
    private val reloadCommand : ReloadCommand = ReloadCommand()
    private val updateCommand : UpdateCommand = UpdateCommand()
    private val policyCommand : PolicyCommand = PolicyCommand()
    private val pluginCommand : PluginCommand = PluginCommand()
    
    init
    {
        this.addChildCommands(documentCommand, reloadCommand, updateCommand, policyCommand, pluginCommand)
        this.setPermission("ruskit")
    }
}
