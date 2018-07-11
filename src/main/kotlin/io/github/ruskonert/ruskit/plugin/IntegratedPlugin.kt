package io.github.ruskonert.ruskit.plugin

import io.github.ruskonert.ruskit.Activator
import io.github.ruskonert.ruskit.Handle
import io.github.ruskonert.ruskit.MessageHandler
import io.github.ruskonert.ruskit.command.RuskitCommand
import io.github.ruskonert.ruskit.component.Prefix
import io.github.ruskonert.ruskit.engine.SustainableHandler
import io.github.ruskonert.ruskit.exception.RuskitPluginException
import io.github.ruskonert.ruskit.util.ReflectionUtility
import io.github.ruskonert.ruskit.util.StringUtility

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap

abstract class IntegratedPlugin : JavaPlugin(), Handle, RuskitServerPlugin {
    init {
        if (CorePlugin != null)
            throw RuskitPluginException("IntegratedPlugin was already initialized")
    }

    companion object {
        var CorePlugin: IntegratedPlugin? = null; private set
    }

    private var registerHandlers: ConcurrentHashMap<SustainableHandler, Boolean> = ConcurrentHashMap()
    override fun getRegisterHandlers(): List<SustainableHandler> = this.registerHandlers.keys.toList()

    fun getRunningRegisterHandlers(): List<SustainableHandler> {
        if (this.registerHandlers.isEmpty()) return ArrayList()
        val list = ArrayList<SustainableHandler>()
        val registered = registerHandlers.keys.toList()
        for ((index, enabled) in registerHandlers.values.withIndex())
            if (enabled)
                list.add(registered[index])
        return list
    }

    private var serverPrefix: Prefix? = null
    override fun getServerPrefix(): Prefix? = this.serverPrefix

    private var messageHandler: MessageHandler? = null
    override fun getMessageHandler(): MessageHandler = this.messageHandler!!

    private var handledInstance: Any? = null
    override fun getHandleInstance(): Any? = this.handledInstance


    @Synchronized
    private fun hookPluginInstance(handle: Any) {
        val field: Field? = ReflectionUtility.GetField(handle::class.java, "instance")
        if (field == null) {
            this.messageHandler!!.defaultMessage("No found at plugin hooker")
        }
        ReflectionUtility.SetField(field!!, handle::class.java, this)
        this.messageHandler!!.defaultMessage("Hooked plugin from Integrated Plugin Loader")
    }

    override fun onInit(handleInstance: Any?): Any? {
        when (handleInstance) {
            is Handle -> {
                val packageName = handleInstance::class.java.canonicalName
                if (this.javaClass.canonicalName == packageName) {
                    CorePlugin = handleInstance as IntegratedPlugin
                    this.serverPrefix = Prefix("&b[" + this.description.name + "]")
                    this.messageHandler = MessageHandler(Bukkit.getConsoleSender(), this.getServerPrefix()!!)
                    this.handledInstance = this
                    this.hookPluginInstance(handleInstance)
                }
            }
            else -> {
                this.handledInstance = handleInstance
            }
        }
        return true
    }

    open fun unload(handleInstance: Any?): Any? {
        // There's no planning to unload schedule
        return true
    }

    fun registerHandler(vararg handles: Class<*>) {
        for (handle in handles) {
            if (handle.isAssignableFrom(SustainableHandler::class.java)) {

            } else if (handle.isAssignableFrom(RuskitCommand::class.java)) {

            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun registerSustainableHandlers(vararg objects: Any) {
        var i = 0
        for (o in objects) {
            if (o is Activator<*>) {
                val ca = o as Activator<IntegratedPlugin?>
                ca.setEnabled(this)
            } else {
                val clazz = o as Class<*>
                if (!Activator::class.java.isAssignableFrom(clazz)) {
                    throw IllegalArgumentException(clazz.name + " isn't Activator class. Ignoring it")
                } else {
                    val instance = ReflectionUtility.getInstance<Activator<IntegratedPlugin?>>(clazz)
                    instance.setEnabled(this)
                    if (RuskitCommand::class.java.isAssignableFrom(clazz)) i++
                }
            }
        }
        if (i != 0) this.messageHandler!!.defaultMessage(StringUtility.WithIndex("&e{0} Command classes has been registered", this.description.name))
        this.messageHandler!!.defaultMessage("&aRegistered class activator " + objects.size + " core(s)")
    }

    @Suppress("UNCHECKED_CAST")
    fun unregisterSustainableHandlers(vararg objects: Any) {
        for (o in objects) {
            if (o is Activator<*>) {
                val ca = o as Activator<IntegratedPlugin?>
                ca.setEnabled(false)
            } else {
                val clazz = o as Class<*>
                if (!Activator::class.java.isAssignableFrom(clazz)) {
                    throw IllegalArgumentException(clazz.name + " isn't ClassActivation class. Ignoring it")
                } else {
                    val instance = ReflectionUtility.getInstance<Activator<IntegratedPlugin?>>(clazz)
                    instance.setEnabled(false)
                }
            }
        }
    }

    @Synchronized
    override fun reload() {
        this.unload(null)
        this.onInit(null)
    }

    fun reload(to : CommandSender)
    {
        this.getMessageHandler().defaultMessage("&bExecuted the command that reloading the plugin", to)
        this.reload()
        this.getMessageHandler().defaultMessage("&eDone!", to)
    }


    fun reloadFinally() {

    }

    final override fun onEnable() {
        if (this.onInit(null) == null) {

        } else {
            this.getMessageHandler().defaultMessage("Plugin loaded successfully")
        }
    }

    final override fun onDisable() {
        if (this.unload(null) == null) {

        } else {
            this.getMessageHandler().defaultMessage("Plugin unloaded successfully")
        }
    }
}