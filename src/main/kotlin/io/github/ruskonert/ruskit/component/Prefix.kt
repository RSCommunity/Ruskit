package io.github.ruskonert.ruskit.component

class Prefix(var prefix: String)
{
    fun getNameWithAttach(): String
    {
        return prefix
    }

    fun getName() : String
    {
        return prefix
    }

    fun getFormatAttach() : String
    {
        return "[%s]"
    }
}