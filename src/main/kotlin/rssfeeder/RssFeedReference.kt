package rssfeeder

import org.jsoup.nodes.Document

class RssFeedReference(document: Document, constraintCssQuery: String)
    : RssReference(document) {

    init {
        cssQuery = constraintCssQuery
    }

    override fun childOf(cssQuery: String): RssReference {
        val childCssQuery = narrowQuery(cssQuery)
        return RssFeedReference(document, childCssQuery)
    }

    private fun narrowQuery(cssQuery: String): String {
        return if (isConstrained()) "${this.cssQuery} > $cssQuery"
        else cssQuery
    }
}