package io.github.ruskonert.ruskit.entity

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.gson.JsonObject

import io.github.ruskonert.ruskit.component.JsonCompatibleSerializer
import io.github.ruskonert.ruskit.config.SynchronizeReader
import java.io.File

abstract class SerializableEntity(entityName : String) : SynchronizeReader(File(entityName))
{
    private var jsonObject : JsonObject? = null
    private val serializableComponent : Multimap<Class<out SerializableEntity>, JsonCompatibleSerializer<*>> = ArrayListMultimap.create()

    abstract fun serialize() : String
    abstract fun deserialize() : SerializableEntity?
}
