package rssfeeder

import org.jsoup.nodes.Document

class RssFeedReference(document: Document, constraintCssQuery: String, parent:RssReference? = null)
    : RssReference(document, parent) {

    init {
        cssQuery = constraintCssQuery
    }

    override fun childOf(cssQuery: String): RssReference {
        val childCssQuery = narrowQuery(cssQuery)
        return RssFeedReference(document, childCssQuery, this)
    }

    private fun narrowQuery(cssQuery: String): String {
        return if (isConstrained()) "${this.cssQuery} > $cssQuery"
        else cssQuery
    }
}