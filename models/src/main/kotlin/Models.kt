package net.codebot.models

import kotlinx.serialization.Serializable

@Serializable
data class sample(
    val id: Int,
    val title: String? = null,
    val content: String? = null
)
