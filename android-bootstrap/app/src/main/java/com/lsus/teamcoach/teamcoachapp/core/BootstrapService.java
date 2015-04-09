
package com.lsus.teamcoach.teamcoachapp.core;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private DrillService getDrillService() { return getRestAdapter().create(DrillService.class); }

    private NewsService getNewsService() {
        return getRestAdapter().create(NewsService.class);
    }

    private TeamService getTeamService() { return getRestAdapter().create(TeamService.class); }

    private SessionService getSessionService() { return getRestAdapter().create(SessionService.class); }

    private CheckInService getCheckInService() {
        return getRestAdapter().create(CheckInService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */
    public List<News> getNews() {
        return getNewsService().getNews().getResults();
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    /**
     * Get all bootstrap Drills that exists on Parse.com
     */
    //public List<Drill> getDrills() { return getDrillService().getDrills().getResults(); }

    /**
     * Get bootstrap Drills of a specific type that exists on Parse.com
     */
    public List<Drill> getDrills(String age, String type) {
        String constraint = "{\"drillAge\":\"" + age + "\",\"drillType\":\"" + type + "\"}";
        return getDrillService().getDrills(constraint).getResults();
    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     */
    public List<CheckIn> getCheckIns() {
       return getCheckInService().getCheckIns().getResults();
    }


    /**
     * Adds a Drill to the Parse.com database
     * @param drill
     * @return
     */
    public Drill addDrill(Drill drill){ return getDrillService().addDrill(drill);}

    /**
     * Updates a drill on Parse.com
     */
    public Object update(Drill drill) {
        return getDrillService().update(drill.objectId, drill);
    }


    public void remove(Drill drill) { getDrillService().remove(drill.objectId); }


    /**
     * Authenticates the passed user with Parse.com*/

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }

    /**
     * Registers the user with Parse.com
     */
    public User register(User user) {
        return getUserService().register(user);
    }

    /**
     * Updates a user on Parse.com
     */
    public Object update(User user) {
        return getUserService().update(user.objectId, user.getSessionToken() , user);
    }

    /**
     * Get the current Users from Parse.com
     */
    public User currentUser(String token){
        return  getUserService().currentUser(token);
    }

    public User currentUserWithChildren(String objectID) {return getUserService().currentUserWithChildren(objectID); }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public Team getTeam(String id){ return getTeamService().getTeam(id); };

    public Team setTeam(Team team) { return  getTeamService().addTeam(team); };

    /**
     * Get all Sessions from Parse.com
     */

    //TODO double check this is right!!
    public List<Session> getSession(String user, String age){
        String constraint = "{\"creator\":\"" + user + "\",\"drillAge\":\"" + age + "\"}";
        return getSessionService().getSessions(constraint).getResults(); }
}