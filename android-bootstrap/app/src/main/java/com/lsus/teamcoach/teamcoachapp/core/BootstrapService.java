
package com.lsus.teamcoach.teamcoachapp.core;

import com.parse.ParseFile;

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

    private CalendarService getCalendarService() { return getRestAdapter().create(CalendarService.class); }

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
     * Adds a News to Parse.com
     */
    public News addNews(News news) { return getNewsService().addNews(news); }

    /**
     *  Get News by Team
     */
    public List<News> getTeamNews(String teamId){
        String constraint = "{\"teamId\":\"" + teamId + "\"}";
        return getNewsService().getTeamNews(constraint, "createdAt").getResults();
    }

    public List<News> getCoachNews(String coachId) {
        String constraint = "{\"creator\":\"" + coachId + "\"}";
        return getNewsService().getCoachNews(constraint, "createdAt").getResults();
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
     * Get bootstrap Drills of a specific type and age that exists on Parse.com
     */
    public List<Drill> getDrills(String age, String type) {
        String constraint = "{\"drillAge\":\"" + age + "\",\"drillType\":\"" + type + "\"}";
        return getDrillService().getDrills(constraint, "-drillRating").getResults();
    }

    /**
     * Get bootstrap Drills based on age from parse.com
     * @param age
     * @return
     */
    public List<Drill> getDrills(String age) {
        String constraint = "{\"drillAge\":\"" + age+ "\"}";
        return getDrillService().getDrills(constraint, "-drillRating").getResults();
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

    public List<Drill> getGroupDrills(String groupId) {
        String constraint = "{\"groupId\":\"" + groupId + "\"}";
        return getDrillService().getGroup(constraint).getResults();
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
        return getUserService().update(user.objectId, user.getSessionToken(), user);
    }

    /**
     * Get the current Users from Parse.com
     */
    public User currentUser(String token){
        return  getUserService().currentUser(token);
    }

    public User currentUserWithChildren(String objectID) {return getUserService().currentUserWithChildren(objectID); }

    /**
     *  Get the team members
      */
    public List<User> getTeamMembers(String team){
        String constraint = "{\"team\":\"" + team + "\",\"role\":\"Player\"}";
        return getUserService().getTeamMembers(constraint).getResults(); }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public Team getTeam(String id){ return getTeamService().getTeam(id); }

    public List<Team> getTeams(String coach){
        String constraint = "{\"coach\":\"" + coach + "\"}";
        return getTeamService().getTeams(constraint).getResults(); }

    public Team setTeam(Team team) { return  getTeamService().addTeam(team); }


    /**
     * Updates a team on Parse.com
     */
    public Object update(Team team) { return getTeamService().update(team.objectId, team);}
    /**
     * Get all Sessions from Parse.com
     */

    public void remove(Team team) { getTeamService().remove(team.objectId); }


    public Session addSession(Session session){ return getSessionService().addSession(session);}

    public Object update(Session session) {
        return getSessionService().update(session.objectId, session);
    }

    public void remove(Session session) { getSessionService().remove(session.objectId); }

    public List<Session> getPublicSessions(String age, String type){
        boolean isPublic = true;
        //String constraint = "{\"isPublic\":\"" + true + "\",\"ageGroup\":\"" + age + "\",\"sessionType\":\"" + type + "\"}";
        String constraint = "{\"ageGroup\":\"" + age + "\",\"sessionType\":\"" + type + "\"}";
        return getSessionService().getSessions(constraint).getResults();
    }

    //TODO double check this is right!!
    public List<Session> getUserSession(String user, String age){
        String constraint = "{\"creator\":\"" + user + "\",\"ageGroup\":\"" + age + "\"}";
        return getSessionService().getSessions(constraint).getResults();
    }


    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public CalendarEvent getEvent(String id){ return getCalendarService().getEvent(id); }

    public List<CalendarEvent> getEvents(String creator){
        String constraint = "{\"creator\":\"" + creator + "\"}";
        return getCalendarService().getEvents(constraint).getResults(); }

    public CalendarEvent setEvent(CalendarEvent event) { return  getCalendarService().addEvent(event); }


    /**
     * Updates an event on Parse.com
     */
    public Object update(CalendarEvent event) { return getCalendarService().update(event.objectId, event);}


    public void remove(CalendarEvent event) { getCalendarService().remove(event.objectId); }


}