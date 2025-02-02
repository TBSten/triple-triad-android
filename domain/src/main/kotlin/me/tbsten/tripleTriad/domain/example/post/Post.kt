package me.tbsten.tripleTriad.domain.example.post

data class Post(
    val id: PostId,
    val userId: UserId,
    val title: String,
    val body: String,
)

@JvmInline
value class PostId(val value: Int)

@JvmInline
value class UserId(val value: Int)
