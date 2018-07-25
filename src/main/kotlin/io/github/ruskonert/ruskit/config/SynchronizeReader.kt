package io.github.ruskonert.ruskit.config

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import io.github.ruskonert.ruskit.engine.RuskitThread
import io.github.ruskonert.ruskit.event.config.SynchronizeReaderEvent
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.util.Algorithm

import java.io.File
import java.security.NoSuchAlgorithmException

abstract class SynchronizeReader<E>(target: File) : RuskitThread()
{
    @Volatile
    private var gsonObject : Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()

    companion object
    {
        private val hREADER : Multimap<IntegratedPlugin, SynchronizeReader<*>> = ArrayListMultimap.create()
        @Synchronized fun RegisterHandledReader() : Multimap<IntegratedPlugin, SynchronizeReader<*>> = hREADER
    }

    open fun getEntity(element : Any) : E?
    {
        return null
    }

    private var file: File? = target
    private val folder : File get() = this.file!!.parentFile
    private var lastHash : String = ""

    override fun onInit(handleInstance: Any?): Any?
    {
        try
        {
            if(this.lastHash != "")
            {
                if(file != null && file!!.exists()) {
                    val hash = Algorithm.getSHA256file(file!!.path)!!
                    if (lastHash != hash) {
                        val readerEvent = SynchronizeReaderEvent(this)
                        readerEvent.insertCustomData("lastHash", this.lastHash)
                        this.lastHash = hash
                        if (!readerEvent.isCancelled) {
                            readerEvent.run()
                        }
                    }
                }
                else
                {

                }
            }
            else
                this.lastHash = Algorithm.getSHA256file(file!!.path)!!
        }
        catch (e : NullPointerException) { }
        catch (e : NoSuchAlgorithmException) { }
        return true
    }

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
    }
}