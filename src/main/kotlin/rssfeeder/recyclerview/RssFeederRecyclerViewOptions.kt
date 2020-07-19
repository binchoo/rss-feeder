package rssfeeder.recyclerview

import rssfeeder.RssReference
import java.lang.NullPointerException

class RssFeederRecyclerViewOptions private constructor(val reference: RssReference) {
    var pollInterval: Int = 5000

    class Builder {
        var rssReference: RssReference? = null
        var pollInterval = 5000

        fun reference(reference: RssReference): Builder {
            rssReference = reference
            return this
        }

        fun pollInterval(ms: Int): Builder {
            pollInterval = ms
            return this
        }

        fun build(): RssFeederRecyclerViewOptions {
            if (rssReference == null) throw NullPointerException()
            return RssFeederRecyclerViewOptions(rssReference!!).also {
                it.pollInterval = pollInterval
            }
        }
    }
}