package com.lsus.teamcoach.teamcoachapp.core;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface UserService {

    @GET(Constants.Http.URL_USERS_FRAG)
    UsersWrapper getUsers();

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param email The users email
     * @param password The users password
     * @param useralias The users alias
     * @param role The users role
     * @return A login response.
     */
    @GET(Constants.Http.URL_AUTH_FRAG)
    User authenticate(@Query(Constants.Http.PARAM_USERNAME) String email,
                               @Query(Constants.Http.PARAM_PASSWORD) String password);

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param userUsername The users username
     * @param userPassword The users password
     * @param userAlias The users alias
     * @param userRole The users role
     * @param userEmail The users email
     * @param userFirstName The users first name
     * @param userLastName The users last name
     * @return A login response.
     */
    @POST(Constants.Http.URL_USERS_FRAG)
    User register(@Query(Constants.Http.PARAM_USERNAME) String userUsername,
                      @Query(Constants.Http.PARAM_PASSWORD) String userPassword,
                      @Query(Constants.Http.PARAM_ALIAS) String userAlias,
                      @Query(Constants.Http.PARAM_ROLE) String userRole,
                      @Query(Constants.Http.PARAM_EMAIL) String userEmail,
                      @Query(Constants.Http.PARAM_FIRST_NAME) String userFirstName,
                      @Query(Constants.Http.PARAM_LAST_NAME) String userLastName
                      );
}
