package io.github.ruskonert.ruskit.engine.plugin

import io.github.ruskonert.ruskit.config.SynchronizeReader
import io.github.ruskonert.ruskit.engine.RuskitThread
import io.github.ruskonert.ruskit.event.config.SynchronizeReaderEvent
import org.bukkit.event.EventHandler

class SynchronizeReaderEngine : RuskitThread()
{
    companion object {
        private val instance : SynchronizeReaderEngine = SynchronizeReaderEngine()
        @JvmStatic fun getInstance() : SynchronizeReaderEngine = instance
    }

    override fun onInit(handleInstance: Any?): Any?
    {
        for(key in SynchronizeReader.RegisterHandledReader().keys())
            for(value in SynchronizeReader.RegisterHandledReader()[key])
                value.onInit(null)
        return true
    }

    @EventHandler
    fun onChange(e : SynchronizeReaderEvent)
    {
        val lastHash: String = e.getCustomData()["lastHash"] as String
        //e.target.verify(lastHash)
    }
}
