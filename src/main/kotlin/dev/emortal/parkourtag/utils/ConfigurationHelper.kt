package dev.emortal.parkourtag.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object ConfigurationHelper {

    inline fun <reified T : Any> initConfigFile(path: Path, emptyObj: T): T {
        return if (path.exists()) format.decodeFromString(path.readText()) else run {
            path.writeText(format.encodeToString(emptyObj))
            emptyObj
        }
    }

    val format = Json {
        encodeDefaults = true
        prettyPrint = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

}