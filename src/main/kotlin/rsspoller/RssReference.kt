package rsspoller

import org.jsoup.nodes.Document
import org.jsoup.Connection
import org.jsoup.select.Elements
import rsspoller.sort.SortStrategy
import kotlin.collections.HashMap

open class RssReference(protected var connection: Connection,
                   private val cssQuery: String, val parent: RssReference?) {

    constructor(connection: Connection): this(connection, QUERY_EMPTY, null)
    constructor(connection: Connection, cssQuery: String): this(connection, cssQuery, null)

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
        evaluate(forceEval, this)
        return cachedElementsNonNull()
    }

    @Synchronized
    protected open fun evaluate(forceEval: Boolean, initiator: RssReference) {
        if (!isEvaluated() || forceEval) {
                parent!!.evaluate(forceEval, initiator)
            if (this == initiator || hasSortStrategy())
                queryDocument(parent.asDocument())
        }
    }

    protected open fun asDocument(): Document {
        return Document("").also {document->
            document.html(cachedElementsNonNull().html())
        }
    }

    protected fun queryDocument(document: Document) {
        val elems = if (isQuerySpecified())
                document.select(cssQuery)
            else
                document.allElements
        sortStrategy?.sort(elems)
        elementsCache.put(cssQuery, elems)
    }

    fun cachedElements() =
        elementsCache[cssQuery]

    private fun cachedElementsNonNull() =
        cachedElements()!!

    fun isEvaluated(): Boolean =
        elementsCache[cssQuery] != null

    fun isQuerySpecified(): Boolean =
        !cssQuery.equals(QUERY_EMPTY)

    fun hasSortStrategy(): Boolean =
        sortStrategy != null

    companion object {
        val QUERY_EMPTY = ""
    }
}