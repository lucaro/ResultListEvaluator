package ch.lucaro.rle

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class Config(val port: Int = 8080, val videoDir: String = "video") {

    companion object{
        fun read(file: File): Config? {
            val json = Json(JsonConfiguration.Stable)
            return try {
                json.parse(Config.serializer(), file.readText())
            } catch (e: Exception) {
                null
            }
        }
    }

}