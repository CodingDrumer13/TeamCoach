package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by TeamCoach on 3/17/2015.
 */
public interface DrillService {
    @GET(Constants.Http.URL_DRILL_FRAG)
    //DrillWrapper getDrills();

    DrillWrapper getDrills(@Query("where") String constraint);
}
