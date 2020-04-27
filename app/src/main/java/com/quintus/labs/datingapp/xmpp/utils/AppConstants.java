package com.quintus.labs.datingapp.xmpp.utils;

import android.content.Context;
import android.os.Environment;

import com.quintus.labs.datingapp.xmpp.XMPPHelper;

import org.jxmpp.jid.FullJid;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

public class AppConstants {
    public static String[] userColorIndicator = new String[]{"#20a2bb", "#7ed321", "#00a7c8", "#3d85ea", "#75b5ff", "#9013fe", "#2859ae", "#F22229", "#Ee597e", "#F5a623"};

    public static class XMPP {
        public static final String SERVICE_NAME = "localhost";
        //        public static final String SERVICE_NAME = "slave";
        public static final String HOST_NAME_LIVE = "chat.myu.co";
        public static final String HOST_NAME_BETA = "beta-chat.myu.co";
        public static final String HOST_NAME_ALPHA = "149.28.149.131";
//        public static final String HOST_NAME_ALPHA = "192.168.0.61";
    }
    public static String formatNumber(Context context, int number) {

//         ISO-3 Code for Arabic is ara
        if (Locale.getDefault().getISO3Language().equals("ara")) {
            return NumberFormat.getInstance(Locale.getDefault()).format(number);
        }
        return number + "";
    }
    public static String formatNumber(Context context, long number) {
        return formatNumber(context, (int) number);
    }

    public static String getJID(String xmppUsername) {
        return xmppUsername + "@" + XMPPHelper.SERVICE_NAME;
    }

    public static boolean isRequiredTimeOverInBg(Context context) {
        return (AppSharedPreferences.getInstance(context).getTotalBgTimeMillis() / (1000 * 60 * 60 * 24)) > AppSharedPreferences.getInstance(context).getChatHistoryRequiredAfterDays();
    }
    public static int getUserIdFromJID(FullJid address) {

        try {
            return Integer.parseInt(address.toString().substring(0, address.toString().indexOf("@")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static class Chat {

        //        public static final int TYPE_SINGLE_CHAT = 1;
//        public static final int TYPE_GROUP_CHAT = 2;
        public static final int TYPE_SINGLE_CHAT = 0;
        public static final int TYPE_GROUP_CHAT = 1;

        public static final String MESSAGE_TYPE_SENT = "sent";
        public static final String MESSAGE_TYPE_RECEIVED = "received";

        public static final int ROLE_MEMBER = 0;
        public static final int ROLE_NOT_MEMBER = -1;
        public static final int ROLE_ADMIN = 1;
        public static final int ROLE_OWNER = 2;
        public static final int ROLE_DESTROYED = -2;

        public static final String SERVER_ROLE_OWNER = "owner";
        public static final String SERVER_ROLE_MEMBER = "user";
        public static final String SERVER_ROLE_NONE = "none";

        public static final String TYPE_CHAT_IMAGE = "chat_image";
        public static final String TYPE_CHAT_TEXT = "chat_text";
        public static final String TYPE_CHAT_VIDEO = "chat_video";
        public static final String TYPE_CHAT_AUDIO = "chat_audio";
        public static final String TYPE_CHAT_DOC = "chat_doc";
        public static final String TYPE_CHAT_LOCATION = "chat_location";
        public static final String TYPE_CHAT_CONTACT = "chat_contact";

//        public static final String TYPE_GROUP_PARTICIPANT_ADDED = "group_participant_added";
//        public static final String TYPE_GROUP_PARTICIPANT_DELETED = "group_participant_deleted";
//        public static final String TYPE_GROUP_NAME_CHANGE = "group_name_change";
//        public static final String TYPE_GROUP_DELETED = "group_deleted";

        public static final String TYPE_NORMAL = "normal";
        public static final String TYPE_CHAT = "chat";
        public static final String TYPE_CHAT_GROUP = "groupchat";

        public static final String TYPE_GROUP_PARTICIPANT_ADDED = "subscribe";
        public static final String TYPE_GROUP_PARTICIPANT_DELETED = "unsubscribe";
        public static final String TYPE_GROUP_DELETED = "destroy";

        public static final String TYPE_TYPING_START = "typing_start";
        public static final String TYPE_TYPING_STOP = "typing_stop";

//        public static final int TYPE_CHAT_ID = 1;
//        public static final int TYPE_CHAT_GROUP_ID = 2;
//        public static final int TYPE_CHAT_IMAGE_ID = 3;
//        public static final int TYPE_CHAT_VIDEO_ID = 4;
//        public static final int TYPE_CHAT_AUDIO_ID = 5;
//        public static final int TYPE_CHAT_DOC_ID = 6;
//        public static final int TYPE_CHAT_LOCATION_ID = 7;
//        public static final int TYPE_CHAT_CONTACT_ID = 7;

        public static final int TYPE_TYPING_START_ID = 5;
        public static final int TYPE_TYPING_STOP_ID = 6;

        public static final int STATUS_PENDING = 1;
        public static final int STATUS_FAILED = 2;
        public static final int STATUS_SENT = 3;
        public static final int STATUS_DELIVERED = 4;
        public static final int STATUS_SEEN = 5;
    }


    public static class Limits {
        public static final int PAGINATION_LIMIT_COMMENTS = 25;
        public static final int PAGINATION_LIMIT_FOLLOW = 20;
        public static final int PAGINATION_LIMIT_BOARD_FILE = 5;
        public static final int PAGINATION_LIMIT_NEWS = 20;
        public static final int PAGINATION_LIMIT_USER = 20;
        public static final int PAGINATION_CHAT_MESSAGES = 1000;
        public static final int PAGINATION_LIMIT_USER_SEARCH = 20;
        public static final int NEWS_FEED_COLLAGE_LIMIT = 5;
        public static int POST_IMAGE_UPLOAD_LIMIT = 8;
        public static final int POST_IMAGE_UPLOAD_CHAT_LIMIT = 10;
        public static final int POST_VIDEO_UPLOAD_LIMIT = 1;
        public static final int NEWS_FEED_TEXT_MAX_LINES = 5;
        public static final int TAGGING_SUGGESTION_START_LENGTH = 0;
        public static final int CHAT_MESSAGE_LOAD_MORE = 50;
        public static final int VIDEO_SIZE_LIMIT = 50;//Mb
        public static int AUDIO_RECORD_LIMIT_SEC = 300;//sec
        public static int VIDEO_RECORD_DURATION_LIMIT;//sec
        public static int FILE_NAME_LENGHT_LIMIT = 50;//sec
        public static int VIDEO_BITRATE = 2500;//kb
        public static int BOARD_NAME_LIMIT = 35;//kb
        public static final int FB_AD_COUNT = 6;
        public static final int CHAT_FORWARD_SELECTION_LIMIT = 10;
        public static final int CHAT_INACTIVE_USER_DAYS_LIMIT = 30;//days
        public static int CHAT_GROUP_SIZE = 50;

        public static boolean isVideoSizeValid(File file) {
            if ((file.length() / (1024 * 1024)) < VIDEO_SIZE_LIMIT) {
                return true;
            }
            return false;
        }
    }
    public static final class Notification {
        public static int NEW_POST_UPLOAD_ID = 134;
        public static final String TYPE = "type";
        public static final String KEY = "key";
        public static final String MESSAGE = "message";
        public static final String FORDUEDATE = "forDueDate";
        public static final String BODY = "body";
        public static final String OBJECTDATAID = "objectDataId";
        public static final String BOARDNAME = "boardName";
        public static final String NOTIFICATIONTYPE = "notificationCategory";
        public static final String USERTONOTIFY = "userIdToNotify";
        public static final String URL = "url";
        public static final String NEWSLIKE = "NewsLike";
        public static final String NEWSLIKECHAIN = "NewsLikeChain";
        public static final String NEWSLIKEUSERCHAIN = "NewsLikeUserChain";

        public static final String NEWSCOMMENTUSERCHAIN = "NewsCommentedUserChain";
        public static final String NEWSCOMMENTOWNERUSER = "NewsCommentOwnerUser";
        public static final String NEWSCOMMENTMENTION = "NewsCommentMention";
        public static final String NEWSCOMMENTTHREADUSER = "NewsCommentThreadUsers";
        public static final String PRIVATEBOARDACCEPT = "PrivateBoardAcceptRequest";
        public static final String ADMINREQUESTACCEPTED = "AdminRequestAccepted";
        public static final String FOLLOWUSER = "FollowUser";
        public static final String NEWSPOSTMENTION = "NewsPostMention";
        public static final String BOARDCREATE = "BoardCreate";
        public static final String JOINEDBOARD = "JoinBoard";
        public static final String JOINBOARDREQUEST = "JoinBoardRequest";
        public static final String NEWSPOSTBOARD = "NewsPostBoard";
        public static final String FOLLOWUSERCHAIN = "FollowUserChain";
        public static final String NEWSPOST = "NewsPost";
        public static final String NEWSCOMMENT = "NewsComment";
        public static final String CMS = "CMS";
        public static final String CHAT = "Chat";
        public static final String CHAT_GROUP = "GroupChat";
        public static final String ERRORMESSAGE = "errorMessage";
        public static final String UPDATEINFOTEXT = "updateInfoText";
        public static final String PRIMEINFOTEXT = "prime-expired";
        public static final String MAKEBOARDADMIN = "BoardAdminRequest";
        public static final String ADDMEMBERINVITED = "AddBoardMemberAccepted";
        public static final String FOLLOWREQUEST = "FollowUserRequest";
        public static final String ACCEPTFOLLOWREQUEST = "AcceptFollowRequest";
        public static final String ASSIGNMENTRESPONSESUBMITTED = "ASSIGNMENT_RESPONSE_SUBMITTED";
        public static final String ASSIGNMENTRESPONSEREJECTED = "ASSIGNMENT_RESPONSE_REJECTED";
        public static final String ASSIGNMENTRESPONSEGRADED = "ASSIGNMENT_RESPONSE_GRADED";

    }
    public static class ApiParamKey {
        public static final String OBJECT_TYPE_ID = "objectTypeId";
        public static final String OBJECT_TYPE = "objectType";
        public static final String OBJECT_DATA_ID = "objectDataId";
        public static final String MEDIA = "media";
        public static final String MEDIA_TYPE = "mediaType";


        public static final String MEDIA_FOR = "mediaFor";
        public static final String UNIQUE_CHAR = "uniqueChar";
        public static final String MEDIA_NAME = "mediaName";
        public static final String IMAGE_CAPTION = "imageCaption";
        public static final String IMAGE = "Image";
        public static final String UPLOAD_FROM = "uploadFrom";
        public static final String USER_ID = "userId";
        public static final String NEWS_OWNERID = "newsOwnerId";
        public static final String KEY_1 = "key1";
        public static final String KEY_2 = "key2";
        public static final String KEY_3 = "key3";
        public static final String USERNAME = "username";
        public static final String usernameOrEmail = "usernameOrEmail";
        public static final String BARE_PEER = "bare_peer";
        public static final String GROUP = "group";
        public static final String VIRTUAL_NEWS_ID = "virtualNewsId";
        public static final String NEWS_COMMENT_TEXT = "newsCommentText";
        public static final String IS_ARCHIVED = "isArchived";
        public static final String NEWS_TEXT = "newsText";
        public static final String IS_WALL_POST = "isWallPost";
        public static final String BOARD_IDS = "boardIds";
        public static final String TYPE = "type";
        public static final String EVENT = "event";
        public static final String HASH_TEXT = "hashText";
        public static final String CATAGORY = "notificationCategory";
        public static final String DELETE_CATAGORY = "category";
        public static final String ASSIGNMENT_ID = "assignmentId";
        public static final String PAGE = "page";
        public static final String STATUS = "status";
        public static final String ID_FOR_PAGE = "idForPage";
        public static final String TIMESTAMP_FOR_PAGE = "timestampForPage";
        public static final String PAGE_DIRECTION = "pageDirection";
        public static final String FILTERTYPE = "filterType";
        public static final String DATAFROMWIDER = "dataFromWiderWindow";
        public static final String EXPLOREPOPULAR = "papularCategory";
        public static final String EXPLOREEXCLUDE = "excludeUsers";
        public static final String SIZE = "size";
        public static final String ROWS = "rows";
        public static final String QUERY = "query";
        public static final String ID = "id";
        public static final String NEWS_ID = "newsId";
        public static final String LIKE_TOGGLE = "likeToggle";
        public static final String MYU_AUTH_TOKEN = "myu-auth-token";
        public static final String APP_LANGUAGE = "Content-Language";
        public static final String APP_TIMEZONE = "timeZone";
        public static final String APP_CLIENT_KEY = "app-client-key";
        public static final String RESOURCE = "resource";
        public static final String APP_VERSION = "appVersion";
        public static final String FOLLOWER_USER_ID = "followerUserId";
        public static final String FOLLOWED_USER_ID = "followedUserId";
        public static final String FOLLOW_TOGGLE = "followToggle";
        public static final String BOARD_ID = "boardId";
        public static final String SEE_MORE = "seeMore";
        public static final String JOINED_BY = "joinedBy";
        public static final String NAME = "name";
        public static final String SORTORDER = "sortOrder";
        public static final String CREATEDFROM = "createdFrom";
        public static final String ABBRIVATION = "abbreviation";
        public static final String CITYID = "cityId";
        public static final String WEBSITE = "website";
        public static final String PHONE = "phone";
        public static final String PINCODE = "pincode";
        public static final String ADDRESS = "address";
        public static final String COUNTRY = "country";
        public static final String STATE = "state";
        public static final String CITY = "city";
        public static final String ISPRIME = "isprime";
        public static final String POSTCHECKFROMBOARD = "postcheck";
        public static final String ISAUDIOSELECTED = "isaudio";
        public static String TOTAL_ALLOCATED_SPACE = "totalAllocatedSpace";
        public static String CONSUMED_SPACE = "consumedSpace";
        public static String CRASHLYTICS_ERROR_TAG_SERVER_API = "server_api";
        public static String DEBUG = "debug";
        public static final String CODE = "code";
        public static final String ACTIONFOR = "actionFor";
        public static final String BOARD_ATTENDEE_CONFIGID = "boardAttendanceConfigId";
        public static final String MEMBER_TYPE = "memberType";
        public static final String COMMENTDISABLETOGGLE = "commentDisableToggle";
        public static final String UNIVERCITY_LEVEL_ID = "universityLevelId";
        public static final String UNIVERCITY_ID = "universityId";
        public static final String COUNTRY_ID = "countryId";
    }
    public static class ApiParamValue {
        public static final String OBJECT_TYPE_NEWS = "news";
        public static final String OBJECT_TYPE_ASSIGMENT = "AssignmentResponse";
        public static final String AUTHENTICATION_ERROR = "401";
        public static final String SALT = "salt";
        public static final int NEWS_TYPE_PROMOTED = 3;
        public static final String FORCE_UPDATE_ERROR = "426";
        public static final String RESPONSE_ERROR = "5XX";
        public static final String RESPONSE_ERROR_4XX = "4XX";
        //        public static final String V2_USER_INTERNAL_LOGIN = "v2_user_internal_login";
        public static final String V2_USER_INTERNAL_LOGIN = "v2Tov3ForceUpdate";
        public static final String FORCE_UPDATE_MESSAGE = "FORCE_UPDATE";
        public static final String UPDATE_SUGGESTION = "UPDATE_SUGGESTION";
        public static final String MEDIA_TYPE_AUDIO = "Audio";
        public static final String MEDIA_TYPE_IMAGE = "Image";
        public static final String MEDIA_TYPE_VIDEO = "Video";
        public static final String MEDIA_TYPE_DOC_PDF = "PDF";
        public static final String MEDIA_TYPE_DOC_XLSX = "XLSX";
        public static final String MEDIA_TYPE_DOC_XLS = "XLS";
        public static final String MEDIA_TYPE_DOC_DOC = "DOC";
        public static final String MEDIA_TYPE_DOC_DOCX = "DOCX";
        public static final String MEDIA_TYPE_DOC_PPT = "PPT";
        public static final String MEDIA_TYPE_DOC_PPTX = "PPTX";
        public static final String MEDIA_FOR_USER_PROFILE = "UserProfile";
        public static final String MEDIA_FOR_USER_BG = "UserBackGround";
        public static final String MEDIA_FOR_NEWS_POST = "NewsPost";
        public static final String MEDIA_FOR_CHAT = "chat";
        public static final String MEDIA_FOR_USER = "user";
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
        public static final String TYPE_BOARD_POST = "boardPost";
        public static final String TYPE_OWN_POST = "ownPost";
        public static final String TYPE_HASHTAG = "hashTag";
        public static final String TYPE_NEWS_FEED = "newsFeed";
        public static final String TYPE_FOLLOWING_CATAGORY_FEED = "following";
        public static final String TYPE_YOU_CATAGORY_FEED = "you";
        public static final String USER = "user";
        public static final String NEXT = "next";
        public static final String PREVIOUS = "previous";
        public static final String GENDER_MALE = "Male";
        public static final String GENDER_FEMALE = "Female";
        public static final String GENDER_COED = "CO_ED";
        public static final String ACTION_TARGET_WEBVIEW = "1";
        public static final String ACTION_TARGET_EXTERNAL_BROWSER = "2";

        /**
         * for mediaName value
         ***/
        public static String getFileName(File file) {
            return file.getName();
        }
    }

    public static String APP_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "myU" + File.separator;
    public static String APP_FOLDER_AUDIO = APP_FOLDER + "Audio" + File.separator;
    public static String APP_FOLDER_VIDEO = APP_FOLDER + "Video" + File.separator;
    public static String APP_FOLDER_IMAGE = APP_FOLDER + "Images" + File.separator;
    public static String APP_FOLDER_IMAGE_COMPRESSED = APP_FOLDER + "Images" + File.separator + "Compressed" + File.separator;
    public static String APP_FOLDER_DOC = APP_FOLDER + "Doc" + File.separator;
    public static String APP_FOLDER_TEMP = APP_FOLDER + "Temp" + File.separator;
    public static String APP_FOLDER_DOC_ROOT = "myU" + File.separator + "Doc" + File.separator;
    public static String FILE_NAME_TRIMMED_VIDEO = "trimmed.mp4";
    public static String FILE_NAME_COMPRESSED_VIDEO = "trimmed_compressed.mp4";
    public static class Values {

        // public static final int IMAGE_RESOLUTION_COMPRESSION = 1440;
        public static final int IMAGE_RESOLUTION_COMPRESSION = 1920;
        public static final int IMAGE_THUMBNAIL_RESOLUTION_COMPRESSION = 256;
        //  public static final int IMAGE_QUALITY = 85;
        public static final int IMAGE_QUALITY = 95;
    }
    public static boolean checkIsFileValid(File file) {
        if (file.exists() == true && file.length() > 100) {
            return true;
        }
        return false;
    }
    public static class DataKey {
        public static final String DOCS_URL_STRING = "docs_url_string";
        public static final String DOC_MEDIA_TYPE = "doc_media_type";
        public static final String DOCS_URL_STRING_ID = "docs_url_string_id";
        public static final String DOCS_URL_FILE_NAME = "docs_url_file_name";
        public static final String MEDIA_MODEL_OBJECT = "media_model_object";
        public static final String POST_DATA_FILE_OBJECT = "post_data_file_object";
        public static final String POST_DATA_OBJECT = "post_data_object";
        public static final String POST_DATA_CANCEL_BOOLEAN = "post_data_cancel_boolean";
        public static final String POST_DATA_RETRY_BOOLEAN = "post_data_retry_boolean";
        public static final String POST_DATA_PAUSE_BOOLEAN = "post_data_pause_boolean";
        public static final String SELECT_HOME_PAGE_INDEX_INT = "select_home_page_index_int";
        public static final String POST_SERVICE_STOP_BOOLEAN = "post_service_stop_boolean";
        public static final String OPEN_COMMENTS_BOOLEAN = "OPEN_COMMENTS_BOOLEAN";
        public static final String NEWS_MODEL_OBJECT = "news_model_object";
        public static final String PAGER_POSITION_INT = "pager_position_int";
        public static final String PAGER_CHAT_ACTIVITY = "chat_activity_page";
        public static final String USER_DETAIL_MODEL_OBJECT = "user_detail_model_object";
        public static final String CARD_DETAIL_MODEL_OBJECT = "card_detail_model_object";

        public static final String CHAT_MODEL_OBJECT = "chat_model_object";
        public static final String CONVERSATION_ID = "conversation_id";
        public static final String CONVERSATION_TYPE = "conversation_type";
        public static final String NEWS_LIST_TYPE_ENUM = "news_list_type";
        public static final String USER_MODEL_OBJECT = "user_model_object";
        public static final String VIDEO_TRIMMER_OPTIONS_OBJECT = "video_trimmer_options_object";
        public static final String VIDEO_TRIMMER_RESULT_URI_STRING = "video_trimmer_result_uri_string";
        public static final String VIDEO_TRIMMER_RESULT_PATH_STRING = "video_trimmer_result_path_string";
        public static final String BOARD_MODEL_OBJECT = "board_model_object";
        public static final String HASH_TAG_STRING = "hash_tag_string";
        public static final String SHOW_LIKE_LIST_BOOLEAN = "show_like_list_boolean";
        public static final String SHOW_VIEW_LIST_BOOLEAN = "show_view_list_boolean";
        public static final String IS_MY_PROFILE_BOOLEAN = "is_my_profile_boolean";
        public static final String NEWS_TYPE_STRING = "news_type_string";
        public static final String IMAGE_URL_STRING = "image_url_string";
        public static final String XMPP_USERNAME_STRING = "xmpp_username_string";
        public static final String XMPP_PASSWORD_STRING = "xmpp_password_string";
        public static final String Chat_MESSAGE_MODEL_OBJECT = "chat_message_model";
        public static final String IS_NAVIGATION_FROM_SHARE_CLASS = "is_navigation_from_share_class";
        public static final String IS_NAVIGATION_FROM_SHARE = "is_navigation_from_share";
        public static final String CLASS_CODE = "class_code";
        public static final String SELECT_EXPLORE_PAGE_INDEX_INT = "select_explore_page";
        public static final String FROM_REGISTRATION = "from_registration";
        public static final String ATTENDANCEITEM_MODEL_OBJECT = "attendanceitem_model_object";
        public static final String ATTENDANCE_CODE = "attendancecode";
        public static final String ATTENDANCE_PRESENT = "attendancepresent";
        public static final String ATTENDANCE_ABSENT = "attendanceabsent";
        public static final String BoardATTEANDANCE_CONFIGID = "boardAttendanceConfigId";
        public static final String BoardID= "boardID";

        public static final String IS_OPENED_FROM_NOTIFICATION = "is_opened_from_notification";

    }
    public static class ShownIn {
        public static final int CHATTING_SCREEN = 30;
        public static final int GROUP_MESSSAGE_SCREEN = 31;
    }
}
