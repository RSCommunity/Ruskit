@file:JvmName("MessageHandler")
@file:Suppress("unused", "UNUSED_PARAMETER")

package io.github.ruskonert.ruskit

import io.github.ruskonert.ruskit.component.Prefix
import io.github.ruskonert.ruskit.util.RuskitLogger
import io.github.ruskonert.ruskit.util.StringUtility
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

open class MessageHandler
{
    private var prefix : Prefix? = null

    private var hasPrefix : Boolean = false

    private var customFilter : HashMap<String, Any> = HashMap()

    private var formatFilter : HashMap<String, String> = HashMap()

    fun addFilter(sentence : String, replaced : String)
    {
        this.formatFilter[sentence] = replaced
    }

    fun getFilterValue(sentence: String? = null) : String?
    {
        return this.formatFilter[sentence]
    }

    fun getEntireFilter() : Map<String, String>
    {
        return this.formatFilter
    }

    private var sender : CommandSender? = null

    constructor(prefix : String)
    {
        this.prefix = Prefix(prefix)
    }

    constructor(prefix : Prefix)
    {
        this.prefix = prefix
    }

    constructor(target : CommandSender)
    {
        this.sender = target
    }

    constructor(target : CommandSender, prefix : Prefix)
    {
        this.sender = target
        this.prefix = prefix
    }

    fun defaultMessage(str : String) {
        this.sendMessage(str, this.prefix != null, RuskitLogger.DefaultLogger, true, this.formatFilter, RuskitLogger.Level.INFO)
    }

    fun defaultMessage(str : String, level : RuskitLogger.Level) {
        this.sendMessage(str, this.prefix != null, RuskitLogger.DefaultLogger, true, this.formatFilter, level)
    }

    fun sendMessage(str : String)
    {

    }

    fun sendMessage(str : String, hasPrefix : Boolean = this.hasPrefix)
    {

    }

    fun sendMessage(str : String, hasPrefix : Boolean = this.hasPrefix, logging : RuskitLogger? = null)
    {

    }

    fun sendMessage(str : String, hasPrefix : Boolean = this.hasPrefix, logging : RuskitLogger? = null, colorable: Boolean = true) {

    }

    fun sendMessage(str : String, hasPrefix : Boolean = this.hasPrefix, logging : RuskitLogger? = null,
                    colorable: Boolean = true, formatFilter : Map<String, String> = this.formatFilter)
    {

    }

    open fun sendMessage(str : String, hasPrefix : Boolean = this.hasPrefix, logging : RuskitLogger? = null,
                    colorable: Boolean = true, formatFilter : Map<String, String> = this.formatFilter, level : RuskitLogger.Level = RuskitLogger.Level.INFO)
    {
        var message = str
        if(hasPrefix)
        {
            message = this.prefix!!.getNameWithAttach() + " " + message
        }

        message = StringUtility.color(message)
        if(! colorable)
        {
            message = ChatColor.stripColor(message)
        }

        sender!!.sendMessage(StringUtility.color(message))
    }

    fun defaultMessage(str : String,
                       target: CommandSender = this.sender!!,
                       level : RuskitLogger.Level = RuskitLogger.Level.INFO) {
        this.sendTargetMessage(str, target, this.prefix != null, RuskitLogger.DefaultLogger, true, this.formatFilter, level)
    }

    open fun sendTargetMessage(str : String,
                               senderTarget : CommandSender,
                               hasPrefix : Boolean = this.hasPrefix,
                               logging : RuskitLogger? = null,
                               colorable: Boolean = true,
                               formatFilter : Map<String, String>? = this.formatFilter,
                               level : RuskitLogger.Level = RuskitLogger.Level.INFO) {

        var prefixMessage = if(hasPrefix) this.prefix!!.getNameWithAttach() + " " else ""
        var message = str

        if(formatFilter != null)
        {
            message = StringUtility.WithIndex(message, formatFilter)
        }

        if(colorable)
            message = StringUtility.color(message)
            prefixMessage = StringUtility.color(prefixMessage)

        senderTarget.sendMessage("$prefixMessage$message")
    }
}