package ch.lucaro.rle

import io.javalin.Javalin
import io.javalin.http.Handler
import io.javalin.http.staticfiles.Location
import org.eclipse.jetty.server.session.DefaultSessionCache
import org.eclipse.jetty.server.session.FileSessionDataStore
import org.eclipse.jetty.server.session.SessionHandler
import java.io.File

object Main {


    @JvmStatic
    fun main(args: Array<String>) {

        val config = if (args.isNotEmpty()) {
            Config.read(File(args[0]))
        } else {
            null
        } ?: Config()


        val javalin = Javalin.create{cfg ->
            cfg.addStaticFiles(config.videoDir, Location.EXTERNAL)
            cfg.sessionHandler { fileSessionHandler() }
        }.start(config.port)

        javalin.get("/", mainpage)
        javalin.post("/", mainpage)

    }

    private val mainpage = Handler { ctx ->
        val id = ctx.req.session.id

        if (ctx.req.method == "POST"){
            ctx.formParamMap().forEach { (t, u) ->
                println("$t -> $u")
            }
        }

        val model = prepareResultList(id)

        model["title"] = "Results for ${model["queryId"] ?: "???"}"

        model["options"] = listOf(
            mapOf("A" to 4, "B" to "very good"),
            mapOf("A" to 3, "B" to "good"),
            mapOf("A" to 2, "B" to "ok"),
            mapOf("A" to 1, "B" to "bad")
        )


        ctx.render("main.vm", model)
    }

    private fun prepareResultList(sessionId: String): MutableMap<String, Any> { //TODO
        val map = mutableMapOf<String, Any>()

        map["queryId"] = "testVideo.mp4"
        map["ids"] = listOf("testVideo.mp4", "second", "third")

        return map

    }

    fun fileSessionHandler() = SessionHandler().apply {
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                val baseDir = File(".")
                this.storeDir = File(baseDir, "session-store").apply { mkdir() }
            }
        }
        httpOnly = true
    }

}
