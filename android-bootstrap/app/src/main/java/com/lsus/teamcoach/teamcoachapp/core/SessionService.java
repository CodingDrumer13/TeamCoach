package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by TeamCoach on 3/31/2015.
 */
public interface SessionService {
    @GET(Constants.Http.URL_SESSIONS_FRAG)
    SessionWrapper getSessions();


    @GET(Constants.Http.URL_SESSIONS_FRAG)
    SessionWrapper getSessions(@Query("where") String constraint);
}
