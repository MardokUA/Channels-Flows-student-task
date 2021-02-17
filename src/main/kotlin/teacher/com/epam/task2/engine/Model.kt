package teacher.com.epam.task2.engine

import teacher.com.epam.task2.api.Asset

/**
 * Represents overall result from the [SearchEngine]
 */
data class SearchResult(
    val assets: List<Asset>,
    val type: Asset.Type,
    /** Designed to use in terminal. See screenshot for more info*/
    val groupName: String
)