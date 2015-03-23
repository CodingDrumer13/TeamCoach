package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

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
    @GET(Constants.Http.URL_USERS_FRAG + "/{id}")
    Team getTeam(@Path("id") String id);

    /**
     * Creates a team in  parse.com
     *
     */
    @POST(Constants.Http.URL_TEAM_FRAG)
    Team setTeam(@Body Team team);
}
