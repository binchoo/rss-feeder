package rssfeeder

import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import rssfeeder.sort.SortStrategy

abstract class RssReference(protected val document: Document) {
    protected var cssQuery: String = QUERY_FOR_ALL
    protected var elementsCache: HashMap<String, Elements> = HashMap()
    var sortStrategy: SortStrategy<*>? = null
        protected set

    abstract fun childOf(cssQuery: String): RssReference

    fun elems(): Elements {
        val elems = elementsCache[cssQuery] ?: cacheNewElems(cssQuery)
        sortStrategy?.sort(elems)
        return elems
    }

    private fun cacheNewElems(cssQuery: String): Elements {
        val elems =
            if (isConstrained())
                document.select(cssQuery)
            else
                document.allElements

        elementsCache.put(cssQuery, elems)
        return elems
    }

    fun isConstrained(): Boolean =
        !cssQuery.equals(QUERY_FOR_ALL)

    fun isEvaluated(): Boolean =
        elementsCache[cssQuery] != null

    fun sortBy(sortStrategy: SortStrategy<*>): RssReference {
        this.sortStrategy = sortStrategy
        return this
    }

    fun cancelSort(): RssReference {
        this.sortStrategy = null
        return this
    }

    companion object {
        val QUERY_FOR_ALL = ""
    }
}