package io.github.ruskonert.ruskit.command.plugin.document

import io.github.ruskonert.ruskit.command.Document
import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.command.entity.ComponentString
import io.github.ruskonert.ruskit.command.entity.Page
import io.github.ruskonert.ruskit.component.FormatDescription
import io.github.ruskonert.ruskit.util.CommandUtility
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class CmdDetailCommand : RuskitCommand<CmdDetailCommand>("detail"), Document
{
    private var currentCommand : String

    init
    {
        this.setPermission("detail")
        this.setDefaultUser(false)
        this.currentCommand = (this.getCurrentCommand(null, true) as String).replace(" ", ".")
        val description = FormatDescription("Show the detail command of {0}")
        description.setDescriptionSelector(0, this.currentCommand, "")
        this.setCommandDescription(description)
    }

    override fun perform(sender: CommandSender, argc: Int, argv: List<String>?, handleInstance: Any?): Any?
    {

        val componentList = ArrayList<ComponentString>()
        val rawDescription = FormatDescription("Review of command : {command}")
        rawDescription.addFilter("command", this.currentCommand)
        // Convert to base component.
        var description = CommandUtility.toBaseComponent(rawDescription) as ComponentString
        componentList.add(description)

        // &aThe framework of command: {current_command} (Included hover message)
        val cmdFramework = FormatDescription(currentCommand)
        cmdFramework.appendFront("&aThe framework of command: &e/&f")

        description = CommandUtility.toBaseComponent(cmdFramework) as ComponentString
        componentList.add(description)
        componentList.add(CommandUtility.toBaseComponent(this.getCommandDescription()) as ComponentString)
        componentList.add(CommandUtility.toBaseComponent(FormatDescription("Parameters : ")) as ComponentString)
        var parameterDescription : FormatDescription
        for(param in this.getParameters())
        {
            parameterDescription = FormatDescription(param.getName() + " | " + param.getDescription()!!.rawMessage())
            componentList.add(CommandUtility.toBaseComponent(parameterDescription) as ComponentString)
        }

        return Page(componentList).execute(sender, ArrayList())
    }
}
