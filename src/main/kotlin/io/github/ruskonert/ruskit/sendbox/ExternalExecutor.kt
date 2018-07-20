package io.github.ruskonert.ruskit.sendbox

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import io.github.ruskonert.ruskit.Activator
import io.github.ruskonert.ruskit.platform.GenericInstance
import io.github.ruskonert.ruskit.plugin.IntegratedPlugin
import io.github.ruskonert.ruskit.util.ReflectionUtility
import org.bukkit.Bukkit
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

import cz.adamh.utils.NativeUtils
import java.io.FileNotFoundException

abstract class ExternalExecutor protected constructor() : GenericInstance<ExternalExecutor>(), Activator<IntegratedPlugin>
{
    private var handlePlugin : IntegratedPlugin? = IntegratedPlugin.CorePlugin
    fun getHandlePlugin() : IntegratedPlugin? = this.handlePlugin

    override fun isEnabled(): Boolean = ExternalExecutor.EXTERNAL_EXECUTORS.contains(this)

    override fun setEnabled(handleInstance: IntegratedPlugin)
    {
        this.handlePlugin = handleInstance
        this.setEnabled(this.handlePlugin != null)
    }

    override fun setEnabled(active: Boolean)
    {
        if(active)
        {
            if(ExternalExecutor.taskThreadState == null)
                this.initialize()

            if(! isEnabled())
            {
                this.onInit(null)
            }
        }
        else
        {
            if(isEnabled())
            {
                ExternalExecutor.EXTERNAL_EXECUTORS.remove(this)
            }
        }
    }

    fun call(methodName : String, vararg args : Any? = Array(0, fun(_ : Int) : Any? { return null })) : Any? = call(this, methodName, args)

    private val methodList : ArrayList<Method> = ArrayList()

    override fun onInit(handleInstance: Any?): Any?
    {
        if(handleInstance != null)
            super.onInit(handleInstance)
        else
            throw NullPointerException("The variable 'handleInstance' must be non-null type")

        for (method in this.genericInstance!!.javaClass.declaredMethods)
        {
            method.isAccessible = true
            if (method.getAnnotation(SafetyExecutable::class.java) == null) continue
            val reference = method.getAnnotation(SafetyExecutable::class.java)

            // Get external path from method annotation
            val defaultLibraryName = ReflectionUtility.getAnnotationDefaultValue(SafetyExecutable::class.java, "libname") as String
            var libraryName = reference.libname
            if(libraryName.isEmpty()) libraryName = defaultLibraryName

            // Check handleInstance's methods were already registered
            if(! LIBRARY_NATIVE_FUNCTION.containsEntry(libraryName, handleInstance::class.java to method))
            {
                LIBRARY_NATIVE_FUNCTION.put(libraryName, handleInstance::class.java to method)
                this.methodList.add(method)
                taskThreadState!!.run()
            }
        }
        return this
    }

    private val is64BitArch : Boolean = System.getProperty("os.arch").indexOf("64") != -1


    @Synchronized private fun initialize()
    {
        val messageHandler = IntegratedPlugin.CorePlugin!!.getMessageHandler()
        if (taskThreadState == null)
        {
            taskThreadState = Runnable {
                for(path in LIBRARY_NATIVE_FUNCTION.keys())
                {
                    if(! CONTEXTS_LOADED.containsKey(path))
                    {
                        val isLoaded : Boolean = try
                        {
                            if(is64BitArch)
                                NativeUtils.loadLibraryFromJar("/libs/$path.dll")
                            else
                                NativeUtils.loadLibraryFromJar("/libs/$path-x86.dll")
                            messageHandler.defaultMessage("&bSystemLibrary loaded successfully -> &e$path")
                            true
                        }
                        catch(e: FileNotFoundException)
                        {
                            messageHandler.defaultMessage("&cSystemLibrary load failed, Because of no such of jar-> &e$path")
                            false
                        }
                        catch(e : UnsatisfiedLinkError)
                        {
                            messageHandler.defaultMessage("&cSystemLibrary load failed! &fMake sure that the file is in your path -> &e$path")
                            false
                        }
                        CONTEXTS_LOADED[path] = isLoaded
                    }
                    else
                    {
                       // messageHandler.defaultMessage("&bSystemLibrary already loaded -> &e$path")
                    }
                }
            }
            TASK_ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(IntegratedPlugin.CorePlugin, taskThreadState, 0L, 0L)
            if(TASK_ID == -1)
            {
                messageHandler.defaultMessage("&eWarning:&c ExternalLib management system load failed")
            }
            else
            {
                messageHandler.defaultMessage("&aExternal libs management system activated")
            }
        }
        else
        {
            messageHandler.defaultMessage("&cThe External Executor was already initialized!")
        }
    }

    @Suppress("FunctionName")
    companion object
    {
         val EXTERNAL_EXECUTORS : HashSet<ExternalExecutor> = HashSet()
         val LIBRARY_NATIVE_FUNCTION: Multimap<String, Pair<Class<*>, Method>> = ArrayListMultimap.create()
         val CONTEXTS_LOADED: ConcurrentHashMap<String, Boolean> = ConcurrentHashMap()
         var TASK_ID = -1
         var taskThreadState: Runnable? = null

        fun Loaded(systemPath : String) : Boolean
        {
            return if(ExternalExecutor.CONTEXTS_LOADED.containsKey(systemPath)) {
                ExternalExecutor.CONTEXTS_LOADED[systemPath]!!
            } else {
                false
            }
        }

        fun call(target: Any, methodName : String, args : Array<out Any?>) : Any?
        {
            if(target is ExternalExecutor) {
                val messageHandler = target.handlePlugin!!.getMessageHandler()
                for (systemPath in ExternalExecutor.LIBRARY_NATIVE_FUNCTION.keys())
                    for (pair in ExternalExecutor.LIBRARY_NATIVE_FUNCTION[systemPath])
                    {
                        if (pair.first == target::class.java && pair.second.name == methodName)
                        {
                            val method = pair.second
                            if (ExternalExecutor.Loaded(systemPath))
                            {
                                return try
                                {
                                    method.invoke(target, *args)
                                }
                                catch(e : IllegalArgumentException)
                                {
                                    e.printStackTrace()
                                    null
                                }
                            }
                            else
                            {
                                messageHandler.defaultMessage("&cError: That method should be referenced in an native code, but not loaded -> $systemPath | " +
                                        "&cUnfortunately the function can not be executed: ${method.name}#${method.returnType.simpleName}")
                            }
                        }
                    }

                messageHandler.defaultMessage("There is no such method in that class: $methodName")
                return null
            }
            else
            {
                throw ClassCastException()
            }
        }
    }
}
