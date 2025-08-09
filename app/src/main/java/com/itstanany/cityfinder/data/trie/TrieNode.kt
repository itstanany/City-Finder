package com.itstanany.cityfinder.data.trie

import com.itstanany.cityfinder.domain.model.City

/**
 * Represents a node in a Trie (prefix tree) data structure.
 *
 * Each node can have multiple children, each representing a character in a word.
 * It also stores a list of cities that end at this node (if `isEndOfWord` is true)
 * or pass through this node.
 *
 * @property children A map of characters to their corresponding child TrieNodes.
 * @property cities A list of [City] objects associated with this node. If `isEndOfWord` is true,
 *                  these are the cities that end with the prefix represented by the path to this node.
 *                  Otherwise, these are cities that have this prefix.
 * @property isEndOfWord A boolean flag indicating whether this node represents the end of a complete word (city name).
 */
class TrieNode {
  val children: MutableMap<Char, TrieNode> = mutableMapOf()
  val cities: MutableList<City> = mutableListOf()
  var isEndOfWord: Boolean = false
}
