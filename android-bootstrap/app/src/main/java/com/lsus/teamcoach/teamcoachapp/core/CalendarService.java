package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Caroline on 4/27/2015.
 */
public interface CalendarService {
    /**
     * Retrieves the event from parse.com
     *
     * @param id The event object id
     * @return the current user.
     */
    @GET(Constants.Http.URL_EVENT_FRAG + "/{id}")
    CalendarEvent getEvent(@Path("id") String id);

    /**
     * Creates an event in  parse.com
     *
     */
    @POST(Constants.Http.URL_EVENT_FRAG)
    CalendarEvent addEvent(@Body CalendarEvent event);

    @GET(Constants.Http.URL_EVENT_FRAG)

    CalendarWrapper getEvents(@Query("where") String constraint, @Query("order") String eventTime);

    /**
     * update an event in the database
     *
     * @param event The updated event
     * @return A update response.
     */
    @PUT(Constants.Http.URL_EVENT_FRAG +"/{id}")
    CalendarEvent update(@Path("id") String id, @Body CalendarEvent event);

    /**
     * Remove an event from parse.com
     *
     * @param id
     * @param
     * @return
     */
    @DELETE(Constants.Http.URL_EVENT_FRAG +"/{id}")
    CalendarEvent remove(@Path("id") String id);
}
