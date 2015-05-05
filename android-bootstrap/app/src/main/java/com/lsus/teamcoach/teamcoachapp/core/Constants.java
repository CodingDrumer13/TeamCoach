

package com.lsus.teamcoach.teamcoachapp.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    public static final class Auth {
        private Auth() {}

        /**
         * Account sessionType id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "com.lsus.teamcoach.teamcoachapp";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "teamcoach";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "com.lsus.teamcoach.teamcoachapp.sync";

        /**
         * Auth token sessionType
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}


        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";

        /**
         * Request A Password Reset
         */
        public static final String URL_PASSWORD_RESET_FRAG = "/1/requestPasswordReset";
        public static final String URL_PASSWORD_RESET = URL_BASE + URL_PASSWORD_RESET_FRAG;

        /**
         * Authentication URL
         */
        public static final String URL_AUTH_FRAG = "/1/login";
        public static final String URL_AUTH = URL_BASE + URL_AUTH_FRAG;

        /**
         * List Users URL
         */
        public static final String URL_USERS_FRAG =  "/1/users";
        public static final String URL_USERS_FRAG_CHILD =  "/1/classes/_User";

        public static final String URL_USERS = URL_BASE + URL_USERS_FRAG;


        /**
         * List Session URL
         */
        public static final String URL_SESSIONS_FRAG = "/1/classes/Session";
        public static final String URL_SESSIONS = URL_BASE + URL_SESSIONS_FRAG;


        /**
         * List News URL
         */
        public static final String URL_NEWS_FRAG = "/1/classes/News";
        public static final String URL_NEWS = URL_BASE + URL_NEWS_FRAG;

        /**
         * List Drill URL
         */
        public static final String URL_DRILL_FRAG = "/1/classes/Drill";
        public static final String URL_DRILL = URL_BASE + URL_DRILL_FRAG;

        /**
         * List Drill Picture URL
         */
        public static final String URL_DRILL_PICTURE_FRAG = "/1/classes/DrillPicture";
        public static final String URL_DRILL_PICTURE = URL_BASE + URL_DRILL_PICTURE_FRAG;

        /**
         * List Team URL
         */
        public static final String URL_TEAM_FRAG = "/1/classes/Team";
        public static final String URL_TEAM = URL_BASE + URL_TEAM_FRAG;

        /**
         * List Event URL
         */
        public static final String URL_EVENT_FRAG = "/1/classes/Event";
        public static final String URL_EVENT = URL_BASE + URL_EVENT_FRAG;

        /**
         * List Checkin's URL
         */
        public static final String URL_CHECKINS_FRAG = "/1/classes/Locations";
        public static final String URL_CHECKINS = URL_BASE + URL_CHECKINS_FRAG;

        /**
         * PARAMS for auth
         */
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";


        public static final String PARSE_APP_ID = "SEPDLRAQaBMsThfDV2LoVxT1WTBy5LuzVDM1qRue";
        public static final String PARSE_REST_API_KEY = "T3D6YI8TYJZubA14QflQA62Vavx3bdtNxiTwlqMW";
        public static final String PARSE_CLIENT_KEY_ID = "1zv4ybcWRgJhObC2VTaWn8qs00C3UXGBbFLIzSvO";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";


        /* App .Net Cred */
        public static final String Client_ID = "RdFLyN4kbknGndfsTJa3xRdwRZWNpUG4";

        public static final String Authorize_URL = "https://account.app.net/oauth/authenticate";

        public static final String Access_Token_URL = "https://account.app.net/oauth/access_token";


    }


    public static final class Extra {
        private Extra() {}

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";

        public static final String DRILL_AGE = "drill_age";

        public static final String DRILL_TYPE = "drill_type";

        public static final String DRILL = "drill";

        public static final String DRILL_INFO_PARENT = "drill_info_parent";

        public static final String DRILL_PICTURE_URL = "drill_picture_url";

        public static final String SESSION_AGE = "session_age";

        public static final String SESSION_TYPE = "session_type";

        public static final String SESSION = "session";

        public static final String SESSION_ID = "sesison_id";

        public static final String TEAM = "team";

        public static final String BOTTOM_AGE = "botAge";

        public static final String TOP_AGE = "topAge";

    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "com.lsus.teamcoach.teamcoachapp.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

}


