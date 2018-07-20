package io.github.ruskonert.ruskit.sendbox

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import io.github.ruskonert.ruskit.engine.RuskitThread
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import org.bukkit.Bukkit
import java.lang.reflect.Method
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class Monitor<T>(stringMethod: () -> T) : Runnable
{
    private var func : () -> T = stringMethod

    override fun run()
    {
        val executorService = Executors.newSingleThreadExecutor()
        val callable = Callable<T>(func)
        val future = executorService.submit(callable)

        try
        {
            val result = future.get(30, TimeUnit.SECONDS)
            //println("Result: $result")
        }
        catch(e : TimeoutException)
        {
            System.out.println("Timeout")
            //future.cancel(true)
        }
    }
}


abstract class ExternalExecutor
{
    private var handlePlugin : IntegratedPlugin? = null

    fun monitor(subject : IntegratedPlugin? = this.handlePlugin!!)
    {
        if(subject == null)
        {
            throw NullPointerException()
        }
        else
        {
            if(this.handlePlugin == null)
                this.handlePlugin = subject
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this.handlePlugin, ExternalExecutor.taskThreadState, 0L, 0L)
        }
    }

    private var runnable : Runnable = Runnable {

    }


    companion object
    {
        private var LIBFUNC0 : Multimap<String, Method> = ArrayListMultimap.create()
        private val taskThreadState : Runnable = Runnable {}


    }

    constructor(libname : String)
    {

    }
}
