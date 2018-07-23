package io.github.ruskonert.ruskit.config

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import io.github.ruskonert.ruskit.engine.RuskitThread
import io.github.ruskonert.ruskit.event.SynchronizeReaderEvent
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.util.Algorithm

import java.io.File
import java.security.NoSuchAlgorithmException

abstract class SynchronizeReader(target: File) : RuskitThread()
{
    companion object
    {
        private val hREADER : Multimap<IntegratedPlugin, SynchronizeReader> = ArrayListMultimap.create()
        @Synchronized fun RegisterHandledReader() : Multimap<IntegratedPlugin, SynchronizeReader> = hREADER
    }

    private var file: File? = target
    private val folder : File get() = this.file!!.parentFile
    private var lastHash : String = ""

    override fun onInit(handleInstance: Any?): Any?
    {
        try
        {
            if(this.lastHash != "") {
                val hash = Algorithm.getSHA256file(file!!.path)!!
                if(lastHash != hash)
                {
                    val readerEvent = SynchronizeReaderEvent(this)
                    readerEvent.insertCustomData("lastHash", this.lastHash)
                    this.lastHash = hash
                    if(! readerEvent.isCancelled)
                        readerEvent.run()
                }
            }
            else
                this.lastHash = Algorithm.getSHA256file(file!!.path)!!
        }
        catch (e : NullPointerException) { }
        catch (e : NoSuchAlgorithmException) { }
        return true
    }

    abstract fun verify(data : Any) : Boolean

    override fun isEnabled(): Boolean
    {
        val multiMap = hREADER
        if(multiMap.containsKey(this.activePlugin)) {
            for(values in multiMap.get(this.activePlugin)) {
                if(values == this) {
                    return true
                }
            }
            return false
        }
        else return false
    }

    override fun setEnabled(active: Boolean)
    {
        super.setEnabled(active)
        if(active)
        {
            hREADER.put(this.activePlugin, this)
        }
        else
        {
            hREADER.remove(this.activePlugin, this)
        }
    }

    override fun setEnabled(handleInstance: IntegratedPlugin?)
    {
        super.setEnabled(handleInstance)
        this.setEnabled(super.activePlugin != null)
    }
}