import org.jsoup.nodes.Element
import rssfeeder.RssFeeder
import rssfeeder.sort.SortCriterion
import rssfeeder.sort.SortStrategy

fun main() {
    test1()
    println("-*-*-*-*-*-*-*-*-*-*-*")
    test2()
    println("-*-*-*-*-*-*-*-*-*-*-*")
    test3()
}

fun test1() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val ref = feeder.getReference()
    val li_b_a = ref.child("li > b > a")
    li_b_a.elems().forEach {
        println(it.text())
    }

    val li_b_a_sorted = li_b_a.sort(
        SortStrategy(object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.ownText()
            }
        }, false)
    )

    println("-------update false--------")
    li_b_a_sorted.elems(false).forEach {
        println(it.text())
    }

    println("-------update true--------")
    li_b_a_sorted.elems().forEach {
        println(it.text())
    }
}

fun test2() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val ref = feeder.getReference()
    val li_b_a = ref.child("li > b > a")
    li_b_a.elems().forEach {
        println(it.text())
    }

    val li_b_a_sorted = li_b_a.sort(SortStrategy.TextLength())

    println("-------update false--------")
    li_b_a_sorted.elems(false).forEach {
        println(it.text())
    }

    println("-------update true--------")
    li_b_a_sorted.elems().forEach {
        println(it.text())
    }
}

fun test3() {
    val feeder = RssFeeder.getInstance("https://en.wikipedia.org")
    val ref = feeder.getReference()
    var li_b_a_sorted = ref.child("li").sort(SortStrategy.ChildrenCount())
        .child("b").sort(SortStrategy.HtmlLength())
        .child("a").sort(SortStrategy.AttrValue("href"))

    li_b_a_sorted.elems().forEach {
        println(it.text())
    }

    li_b_a_sorted = li_b_a_sorted.sort(SortStrategy.TextLength())

    println("-------update false--------")
    li_b_a_sorted.elems(false).forEach {
        println(it.text())
    }

    println("-------update true--------")
    li_b_a_sorted.elems().forEach {
        println(it.text())
    }
}