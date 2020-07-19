package com.corndog.rssfeederrecyclerview.rssfeeder

import rsspoller.RssReference

class RssPoller(val rssReference: RssReference, var interval: Long) {
    var isPolling = false
        private set

    private val pollingThread = Thread {
        while (isPolling) {
            rssReference.elems()
            callback?.onEachPolling(rssReference)
            Thread.sleep(interval)
        }
    }
    private var callback: Callback? = null

    fun start() {
        isPolling = true
        pollingThread.start()
    }

    fun stop() {
        isPolling = false
        pollingThread.interrupt()
    }

    fun addCallback(callback: Callback) {
        this.callback = callback
    }

    fun changeInterval(interval: Long) {
        this.interval = interval
    }

    interface Callback {
        fun onEachPolling(updatedRssReference: RssReference)
    }
}