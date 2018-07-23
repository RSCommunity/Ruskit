package io.github.ruskonert.ruskit.component

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

abstract class JsonCompatibleSerializer<A> : JsonDeserializer<A>, JsonSerializer<A>