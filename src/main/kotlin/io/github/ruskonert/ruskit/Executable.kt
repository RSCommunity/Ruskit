package io.github.ruskonert.ruskit

interface Executable<in S>
{
    fun execute(target: S, argv: ArrayList<String>): Any?
}