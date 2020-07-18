import rssfeeder.RssFeeder
import rssfeeder.sort.SortStrategy

fun main() {
    rssFeederNarrowingQuery()
    rssFeederSortingByAttrHrefValue()
    rssFeederSortingByTextAndTextLength()
    rssFeederCompoundedSortingStrategy()
    rssFeederEvalListValidation()
}

fun rssFeederNarrowingQuery() {

    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val rssRef = feeder.getReference().child("li").child("b").child("a")

    println("<<rssFeederNarrowingQuery>>")

    println(rssRef.isQuerySpecified())
    println(rssRef.isEvaluated())
    println(rssRef.elems().count().toString() + "개")
    println(rssRef.isQuerySpecified())
    println(rssRef.isEvaluated())

    val elems = rssRef.elems()
    elems.forEach {
        println(it.parent().html())
    }
    println("count = ${elems.count()}")
}

fun rssFeederSortingByAttrHrefValue() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val rssRef = feeder.getReference().child("li").child("b").child("a")

    val strategyHrefValueDescending = SortStrategy.AttrValue("href", false)
    val elems = rssRef.sort(strategyHrefValueDescending).elems()

    println("<<rssFeederSortingByAttrHrefValue>>")
    elems.forEach {
        println(it.attr("href"))
    }
}

fun rssFeederSortingByTextAndTextLength() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val rssRef = feeder.getReference().child("li").child("b")
    val strategyText = SortStrategy.Text()
    val strategyTextLength = SortStrategy.TextLength()

    println("<<rssFeederSortingByText>>")
    rssRef.sort(strategyText).elems().forEach {
        println(it.text())
    }

    println("<<rssFeederSortingByTextLength>>")
    rssRef.sort(strategyTextLength).elems().forEach {
        println(it.text())
    }
}

//TODO: Implement compounded sorting strategy feature.
fun rssFeederCompoundedSortingStrategy() {
    val strategyChildrenCount = SortStrategy.ChildrenCount()
    val strategyHrefValueDescending = SortStrategy.AttrValue("href", false)

    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val rssRef = feeder.getReference("li")
        .child("b").sort(strategyChildrenCount)
        .child("a").sort(strategyHrefValueDescending)
    val elems = rssRef.elems()

    println("<<rssFeederCompoundedSortingStrategy>>")
    elems.forEach {
        println(it.html())
    }
    println("count = ${elems.count()}")
}

fun rssFeederEvalListValidation() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    println("ref's evalQueue")
    val ref = feeder.getReference().child("li")
    println("ref1's evalQueue")
    val ref1 = ref.child("b")
    println("ref2's evalQueue")
    val ref2 = ref.child("a")
}
