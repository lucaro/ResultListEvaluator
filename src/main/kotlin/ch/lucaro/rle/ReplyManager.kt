package ch.lucaro.rle

import java.io.File
import java.lang.Exception

class ReplyManager (private val qm: QueryManager, private val baseDir: File) {

    fun getNextListToEvaluate(userId: String): List<Pair<String, String>> {

        val userDir = File(baseDir, userId).apply { mkdirs() }
        qm.queryIdList.forEach { qid ->
            val replyFile = File(userDir, "$qid.csv")
            val evaluated = try{
                replyFile.readLines().map { it.split(",")[0] }.toSet()
            }catch (e: Exception){
                emptySet<String>()
            }
            val remaining = qm.getPairsForQuery(qid).filter { it.second !in evaluated }
            if (remaining.isNotEmpty()){
                return remaining
            }
        }
        return emptyList()

    }

    fun storeEvaluatedResults(userId: String, queryId: String, evaluation: Map<String, String>) {
        val userDir = File(baseDir, userId).apply { mkdirs() }
        val replyFile = File(userDir, "$queryId.csv")
        replyFile.appendText(
            evaluation.map { "${it.key},${it.value}\n" }.joinToString (separator = "")
        )

    }

}