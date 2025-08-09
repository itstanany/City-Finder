package com.itstanany.cityfinder.data.trie

import com.itstanany.cityfinder.domain.model.City


/**
 * A Trie (prefix tree) data structure specifically designed for storing and searching [City] objects.
 *
 * This Trie implementation allows for efficient insertion of cities and searching for cities
 * based on a given prefix. The search is case-insensitive.
 *
 * Each node in the Trie represents a character in a city's name. A [City] object is
 * associated with the node that marks the end of its name. This means that multiple cities
 * with the same name can be stored.
 *
 * The primary operations supported are:
 * - `insert(city: City)`: Adds a city to the Trie.
 * - `search(prefix: String)`: Finds all cities whose names start with the given prefix.
 */
class CityTrie {

  private val root = TrieNode()


  /**
   * Inserts a city into the Trie.
   *
   * The city's name is used as the key for insertion. The name is converted to lowercase
   * before being processed to ensure case-insensitive storage and retrieval.
   * Each character of the city's name navigates or creates a path in the Trie.
   * When the end of the city's name is reached, the corresponding [TrieNode] is marked
   * as the end of a word, and the [City] object is added to the list of cities at that node.
   *
   * @param city The [City] object to be inserted into the Trie.
   */
  fun insert(city: City) {
    var node = root
    val key = city.name.lowercase()
    for (char in key) {
      node = node.children.getOrPut(char) { TrieNode() }
    }
    node.isEndOfWord = true
    node.cities.add(city)
  }


  /**
   * Searches for cities that start with the given prefix.
   *
   * The search is case-insensitive. It traverses the Trie based on the lowercase version of the prefix.
   * If the prefix is not found in the Trie, an empty list is returned.
   * Otherwise, it collects all cities stored in the subtree rooted at the node corresponding to the end of the prefix.
   *
   * @param prefix The prefix string to search for.
   * @return A list of [City] objects whose names start with the given prefix.
   *         Returns an empty list if no cities match the prefix.
   */
  fun search(prefix: String): List<City> {
    var node = root
    val key = prefix.lowercase()
    for (char in key) {
      node = node.children[char] ?: return emptyList()
    }
    val result = mutableListOf<City>()
    collectAllCities(node, result)
    return result
  }

  /**
   * Recursively collects all cities stored in the subtree rooted at the given node.
   *
   * This function performs a depth-first traversal of the Trie starting from the specified [node].
   * If a node marks the end of a word (i.e., `node.isEndOfWord` is true),
   * all cities associated with that word (stored in `node.cities`) are added to the [result] list.
   * The traversal continues through all children of the current node.
   *
   * @param node The [TrieNode] from which to start collecting cities.
   * @param result A mutable list to which the collected [City] objects will be added.
   */
  private fun collectAllCities(node: TrieNode, result: MutableList<City>) {
    if (node.isEndOfWord) {
      result.addAll(node.cities)
    }
    for (child in node.children.values) {
      collectAllCities(child, result)
    }
  }
}
