/*
Copyright (c) 2018 ruskonert
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
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
package io.github.ruskonert.ruskit

import io.github.ruskonert.ruskit.command.RuskitPluginCommand
import io.github.ruskonert.ruskit.engine.InventoryHandler
import io.github.ruskonert.ruskit.engine.plugin.CommandRegistration
import io.github.ruskonert.ruskit.engine.plugin.SynchronizeReaderEngine
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.plugin.RuskitServerPlugin
import io.github.ruskonert.ruskit.sendbox.RuskitSendboxHandler

class Ruskit : IntegratedPlugin()
{
    companion object {
        private var instance : RuskitServerPlugin? = null
        fun getInstance() : RuskitServerPlugin? = instance
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
                RuskitSendboxHandler::class.java,

                // SynchronizeReader Engine
                SynchronizeReaderEngine::class.java,

                InventoryHandler::class.java
        )
        RuskitSendboxHandler.getInstance().call("PlaySoundA", "/test.wav")
        this.getMessageHandler().defaultMessage("JNI Test -> WinAPI function called: PlaySoundA(test.wav)")
        return true
    }
}