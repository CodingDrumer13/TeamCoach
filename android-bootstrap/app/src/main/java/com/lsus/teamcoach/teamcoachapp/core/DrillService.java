package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by TeamCoach on 3/17/2015.
 */
public interface DrillService {

    /**
     * Retrieves drills from parse using a constraint.
     *
     * @param constraint
     * @return
     */
    @GET(Constants.Http.URL_DRILL_FRAG)
    DrillWrapper getDrills(@Query("where") String constraint);

    /**
     * Adds a drill to parse.
     *
     * @param drill
     * @return
     */
    @POST(Constants.Http.URL_DRILL_FRAG)
    Drill addDrill(@Body Drill drill);

    /**
     * update a drill in the database
     *
     * @param drill The drill
     * @return A update response.
     */
    @PUT(Constants.Http.URL_DRILL_FRAG+"/{id}")
    Drill update(@Path("id") String id,  @Body Drill drill);


    /**
     * Remove a drill from parse.com
     *
     * @param id
     * @param
     * @return
     */
    @DELETE(Constants.Http.URL_DRILL_FRAG+"/{id}")
    Drill remove(@Path("id") String id);
}
