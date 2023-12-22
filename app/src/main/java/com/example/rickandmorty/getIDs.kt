package com.example.rickandmorty

fun getIDs(urls: List<String>): List<Int> {
    val characterIds = mutableListOf<Int>()
    for (url in urls) {
        val lastSlashIndex = url.lastIndexOf('/')
        val idString = url.substring(lastSlashIndex + 1)
        val id = idString.toInt()
        characterIds.add(id)
    }
    return characterIds.toList()
}

fun getID(url: String): Int {
    val lastSlashIndex = url.lastIndexOf('/')
    val idString = url.substring(lastSlashIndex + 1)
    if (idString.isNotEmpty()) {
        return idString.toInt()
    }
    return -1
}
