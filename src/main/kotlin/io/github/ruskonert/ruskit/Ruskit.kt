package io.github.ruskonert.ruskit

import io.github.ruskonert.ruskit.command.RuskitPluginCommand
import io.github.ruskonert.ruskit.engine.plugin.CommandRegistration
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.plugin.RuskitServerPlugin
import io.github.ruskonert.ruskit.sendbox.RuskitSendboxHandler
import java.io.ByteArrayOutputStream

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
        this.registerSustainableHandlers(
                // Register dynamic commands core
                CommandRegistration::class.java,

                // Register Ruskit main commands
                RuskitPluginCommand::class.java,

                // Test external libs loader
                RuskitSendboxHandler::class.java
        )
        RuskitSendboxHandler.getInstance().call("PlaySoundA", fileToByteArray("/test.wav"))
        this.getMessageHandler().defaultMessage("JNI Test -> WinAPI function called: PlaySoundA(test.wav)")
        return true
    }

    private fun fileToByteArray(filename : String) : ByteArray
    {
        val `is` = javaClass.getResourceAsStream(filename)
        val buffer = ByteArrayOutputStream()

        var nRead: Int
        val data = ByteArray(16384)
        nRead = `is`.read(data, 0, data.size)
        while (nRead != -1)
        {
            buffer.write(data, 0, nRead)
            nRead = `is`.read(data, 0, data.size)
        }
        buffer.flush()
        return buffer.toByteArray()
    }
}