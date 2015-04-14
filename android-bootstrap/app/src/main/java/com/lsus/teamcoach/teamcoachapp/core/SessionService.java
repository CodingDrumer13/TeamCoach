package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by TeamCoach on 3/31/2015.
 */
public interface SessionService {
    @GET(Constants.Http.URL_SESSIONS_FRAG)
    SessionWrapper getSessions();


    @GET(Constants.Http.URL_SESSIONS_FRAG)
    SessionWrapper getSessions(@Query("where") String constraint);

    /**
     * Adds a drill to parse.
     *
     * @param session
     * @return
     */
    @POST(Constants.Http.URL_SESSIONS_FRAG)
    Session addSession(@Body Session session);

    /**
     * update a session in the database
     *
     * @param session The session
     * @return A update response..
     */
    @PUT(Constants.Http.URL_SESSIONS_FRAG+"/{id}")
    Session update(@Path("id") String id,  @Body Session session);


    /**
     * Remove a session from parse.com
     *
     * @param id
     * @param
     * @return
     */
    @DELETE(Constants.Http.URL_SESSIONS_FRAG+"/{id}")
    Session remove(@Path("id") String id);
}
