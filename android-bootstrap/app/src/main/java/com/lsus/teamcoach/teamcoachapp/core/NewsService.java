package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


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

}
