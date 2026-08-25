package com.trending.now.app.feature.comments.data.remote

data class PostCommentRequest(
    val source: String,
    val headline: String,
    val topic: String,
    val postId: String,
    val comment: String,
)
