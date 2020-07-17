package rssfeeder.sort

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

abstract class SortStrategy<CriterionType: Comparable<CriterionType>>(val ascending: Boolean) {
    abstract val sortCriterion: SortCriterion<CriterionType>

    fun sort(elements: Elements) {
        if (ascending) {
            elements.sortBy {element->
                criterionValueOf(element)
            }
        } else {
            elements.sortByDescending {element->
                criterionValueOf(element)
            }
        }
    }

    fun criterionValueOf(element: Element): CriterionType {
        return sortCriterion.criterionValueOf(element)
    }

    class AttrValue(val attrKey: String, ascending: Boolean = true): SortStrategy<String>(ascending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.attr(attrKey)
            }
        }
    }

    class ClassName(ascending: Boolean = true): SortStrategy<String>(ascending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.className()
            }
        }
    }

    class TagName(ascending: Boolean = true): SortStrategy<String>(ascending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.tagName()
            }
        }
    }

    class ChildrenCount(ascending: Boolean = true): SortStrategy<Int>(ascending) {
        override val sortCriterion = object: SortCriterion<Int> {
            override fun criterionValueOf(element: Element): Int {
                return element.children().size
            }
        }
    }
}
