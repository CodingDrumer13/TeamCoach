package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by TeamCoach on 3/17/2015.
 */
public interface DrillService {
    @GET(Constants.Http.URL_DRILL_FRAG)
    DrillWrapper getDrills(@Query("where") String constraint);

    @POST(Constants.Http.URL_DRILL_FRAG)
    Drill addDrill(@Body Drill drill);
}
