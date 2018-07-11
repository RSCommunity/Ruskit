package io.github.ruskonert.ruskit.command.plugin.policy

import io.github.ruskonert.ruskit.command.RuskitCommand
import org.bukkit.command.CommandSender

class PolicyPermissionCommand : RuskitCommand<PolicyStatusCommand>("permission", "perm")
{
    init {
        this.setPermission("permission")
        this.setCommandDescription("Manage the permission of handled classes")
    }
}
