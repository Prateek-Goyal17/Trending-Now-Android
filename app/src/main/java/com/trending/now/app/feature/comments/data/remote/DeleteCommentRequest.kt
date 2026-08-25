package com.trending.now.app.feature.comments.data.remote

data class DeleteCommentRequest(
    val postId: String,
    val commentId: String,
)
