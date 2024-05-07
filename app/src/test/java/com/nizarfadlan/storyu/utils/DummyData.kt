package com.nizarfadlan.storyu.utils

import com.nizarfadlan.storyu.domain.model.Story

object DummyData {
    fun emailDummy() = "nizar@email.com"
    fun passwordDummy() = "nizar123"
    fun tokenDummy() =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWJydnIzT1N1RDEtdHNpWmYiLCJpYXQiOjE3MTQ5MDEzNDd9.KOVcCXjMRaUmX1M-qWlIdG24ekEVU7xs1R2g8wOhl-c"

    fun listStoryDummy(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..10) {
            val story = Story(
                id = "story-Y5ydZSquCaoHE-$i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1714901063849_8271964dd36d30c23aa3.jpg",
                createdAt = "2024-05-05T09:24:23.853Z",
                name = "User Name",
                description = "Story Description $i",
                latitude = (-29.212).toFloat(),
                longitude = (-16.002).toFloat(),
            )
            items.add(story)
        }
        return items
    }

    fun emptyListStoryDummy(): List<Story> = emptyList()

}