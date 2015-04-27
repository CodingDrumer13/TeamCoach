package com.lsus.teamcoach.teamcoachapp.core;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Interface for defining the news service to communicate with Parse.com
 */
public interface NewsService {

    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getNews();

    /**
     * Creates a news in  parse.com
     *
     */
    @POST(Constants.Http.URL_NEWS_FRAG)
    News addNews(@Body News news);

    /**
     *
     * @param constraint
     * @return
     */
    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getTeamNews(@Query("where") String constraint, @Query("order") String createdAt);

    @GET(Constants.Http.URL_NEWS_FRAG)
    NewsWrapper getCoachNews(@Query("where") String constraint, @Query("order") String createdAt);
}
