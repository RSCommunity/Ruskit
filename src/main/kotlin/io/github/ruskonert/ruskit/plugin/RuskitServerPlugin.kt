package io.github.ruskonert.ruskit.plugin

import io.github.ruskonert.ruskit.MessageHandler
import io.github.ruskonert.ruskit.component.Prefix
import io.github.ruskonert.ruskit.engine.SustainableHandler

interface RuskitServerPlugin
{
    fun getMessageHandler(): MessageHandler

    fun getHandleInstance(): Any?

    fun getServerPrefix(): Prefix?

    fun getRegisterHandlers(): List<SustainableHandler>

    fun reload()
}
