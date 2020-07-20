package rsspoller.sort

import org.jsoup.Connection
import org.jsoup.nodes.Document
import rsspoller.RssReference

class RootReference(connection: Connection, cssQuery: String)
    : RssReference(connection, cssQuery, null) {

    constructor(connection: Connection): this(connection, QUERY_EMPTY)

    private lateinit var document: Document //only the first reference can possess a late-initialized document.

    override fun evaluate(forceEval: Boolean, initiator: RssReference) {
        if (!isEvaluated() || forceEval)
            lazyConnection()
            if (this == initiator || hasSortStrategy())
                queryDocument(document)
    }

    private fun lazyConnection(): Document {
        document = connection.get()
        return document
    }

    override fun asDocument(): Document {
        return document
    }
}