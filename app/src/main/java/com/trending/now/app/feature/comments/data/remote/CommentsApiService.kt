package com.trending.now.app.feature.comments.data.remote

import com.trending.now.app.core.constants.TrendingNowApiPaths
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentsApiService {
    @GET(TrendingNowApiPaths.COMMENTS_BY_POST)
    suspend fun getComments(
        @Path("postId") postId: String,
    ): Response<ResponseBody>

    @POST(TrendingNowApiPaths.COMMENTS)
    suspend fun postComment(
        @Body body: PostCommentRequest,
    ): Response<ResponseBody>

    @HTTP(method = "DELETE", path = TrendingNowApiPaths.COMMENTS, hasBody = true)
    suspend fun deleteComment(
        @Body body: DeleteCommentRequest,
    ): Response<ResponseBody>
}
