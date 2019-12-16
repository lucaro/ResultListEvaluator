package ch.lucaro.rle

import java.io.File

class QueryManager(baseDirectory: File) {

    private val queries = baseDirectory.walk().maxDepth(1).filter{ it.isFile && it.name.endsWith(".txt") }.map {
        it.name.substringBefore(".txt") to it.readLines()
    }.toMap()


    val queryIdList
        get() = queries.keys.toList().sorted()

    fun getPairsForQuery(queryId: String): List<Pair<String, String>> {
        return queries[queryId]?.map { queryId to it } ?: listOf()
    }

}