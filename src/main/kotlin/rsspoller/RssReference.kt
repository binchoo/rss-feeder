package rsspoller

import org.jsoup.nodes.Document
import org.jsoup.Connection
import org.jsoup.select.Elements
import rsspoller.sort.SortStrategy
import kotlin.collections.HashMap

class RssReference(private var connection: Connection,
                   private val cssQuery: String, val parent: RssReference?) {

    constructor(connection: Connection): this(connection, QUERY_DEFAULT, null)
    constructor(connection: Connection, cssQuery: String): this(connection, cssQuery, null)

//    private var evalQueue: LinkedList<RssReference> =
//        if (parent == null) {
//            LinkedList()
//        } else {
//            parent.evalQueue.clone() as LinkedList<RssReference>
//        }.also {evalQueue->
//            evalQueue.add(this)
//        }

    var sortStrategy: SortStrategy<*>? = null
        private set

    private var elementsCache: HashMap<String, Elements> = HashMap()

    fun child(cssQuery: String): RssReference {
        val childCssQuery = concatQuery(cssQuery)
        return RssReference(connection, childCssQuery, this)
    }

    private fun concatQuery(cssQuery: String): String {
        return if (hasSortStrategy() || !isQuerySpecified()) cssQuery
        else "${this.cssQuery} > $cssQuery"
    }

    fun sort(sortStrategy: SortStrategy<*>): RssReference {
        this.sortStrategy = sortStrategy
        return this
    }

    fun noSort(): RssReference {
        this.sortStrategy = null
        return this
    }

    /**
     * @param forceEval whether or not to force the lazy evaluation. Default true.
     * @return Elements newly evaluated if forceEval=true or isEvalutated()=false, else read from cache.
     * @author binchoo
     */
    fun elems(forceEval: Boolean = true): Elements {
        evaluate(forceEval)
        return readCacheNonNull()
    }

    @Synchronized
    fun evaluate(forceEval: Boolean) {
        if (!isEvaluated() || forceEval) {
            parseMyDocument(
                if (parent == null) {
                    lazyConnection()
                } else {
                    parent.evaluate(forceEval)
                    parent.asDocument()
            })
        }
    }

    fun lazyConnection(): Document {
        return connection.get()
    }

    private fun asDocument(): Document {
        return Document("").also {
            it.html(readCacheNonNull().html())
        }
    }

    private fun parseMyDocument(document: Document) {
        val elems =
            if (isQuerySpecified())
                document.select(cssQuery)
            else
                document.allElements
        sortStrategy?.sort(elems)
        elementsCache.put(cssQuery, elems)
    }

    fun readFromCache() =
        elementsCache[cssQuery]

    private fun readCacheNonNull() =
        readFromCache()!!

    fun isQuerySpecified(): Boolean =
        !cssQuery.equals(QUERY_DEFAULT)

    fun isEvaluated(): Boolean =
        elementsCache[cssQuery] != null

    fun hasSortStrategy(): Boolean =
        sortStrategy != null

    companion object {
        private val QUERY_DEFAULT = ""
    }
}