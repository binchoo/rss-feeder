package rssfeeder.sort

import org.jsoup.nodes.Element
import org.jsoup.select.Elements

abstract class SortStrategy<CriterionType: Comparable<CriterionType>>(val isAscending: Boolean) {
    protected abstract val sortCriterion: SortCriterion<CriterionType>

    fun sort(elements: Elements) {
        if (isAscending) {
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

    class AttrValue(private val attrKey: String, ascending: Boolean = true): SortStrategy<String>(ascending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.attr(attrKey)
            }
        }
    }

    class ClassName(isAscending: Boolean = true): SortStrategy<String>(isAscending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.className()
            }
        }
    }

    class TagName(isAscending: Boolean = true): SortStrategy<String>(isAscending) {
        override val sortCriterion = object: SortCriterion<String> {
            override fun criterionValueOf(element: Element): String {
                return element.tagName()
            }
        }
    }

    class ChildrenCount(isAscending: Boolean = true): SortStrategy<Int>(isAscending) {
        override val sortCriterion = object: SortCriterion<Int> {
            override fun criterionValueOf(element: Element): Int {
                return element.children().size
            }
        }
    }
}
