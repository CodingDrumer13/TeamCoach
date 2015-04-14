package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Don on 3/22/2015.
 */
public interface TeamService {
    /**
     * Retrieves the team from parse.com
     *
     * @param id The team object id
     * @return the current user.
     */
    @GET(Constants.Http.URL_TEAM_FRAG + "/{id}")
    Team getTeam(@Path("id") String id);

    /**
     * Creates a team in  parse.com
     *
     */
    @POST(Constants.Http.URL_TEAM_FRAG)
    Team addTeam(@Body Team team);

    @GET(Constants.Http.URL_TEAM_FRAG)

    TeamWrapper getTeams(@Query("where") String constraint);

    /**
     * update a user in the database
     *
     * @param team The updated team
     * @return A update response.
     */
    @PUT(Constants.Http.URL_TEAM_FRAG+"/{id}")
    Team update(@Path("id") String id, @Body Team team);
}
