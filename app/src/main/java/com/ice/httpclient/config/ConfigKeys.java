package com.ice.httpclient.config;

/**
 * Desc:公共变量
 * Created by icewater on 2024/01/02.
 */
public class ConfigKeys {
    /**************公共变量****************/
    static final String ACCESS_TOKEN = "access_token";

    public static final String FIRST_LAUNCH = "first_launch";

    public static final String APP_VERSION = "app_version";

    public static final String HIPPIUS_CONFIG = "hippius_config";

    public static final String TENANT_USER_INFO = "tenant_user_info";

    public static final String USER_INFO = "user_info";

    public static final String OPTIONS = "options";

    public static final String GLOBAL_SET_INFO = "global_set_info";

    public static final String ENCRYPT_PRIMARY_KEY = "encrypt_primary_key";

    public static final String SERVER_TIME = "server_time";

    //仅android_id获取失败时使用
    public static final String SP_UUID = "sp_uuid";

    public static final String SP_LANGUAGE = "sp_lang";

    public static final String KEY_RN_EXTRA = "rn_extra";
    //同意用户协议，和登录账号相关
    public static final String SP_AGREE_PRIVACY = "sp_agree_privacy";

    //禁止截屏
    public static final String SP_FORBID_SCREENSHOT = "sp_forbid_screen_shot";

    //当前icon
    public static final String SP_CURRENT_ICON = "sp_current_icon";
    //从后端返回的设备id（设备表中的主键）
    public static final String SP_DEVICE_ID_FROM_SERVER = "sp_device_id_from_server";

    public static final String SP_LOGIN_ACCOUNT = "sp_login_account";

    public static final String SP_LOGIN_PASSWORD = "sp_login_password";

    public static final String SP_LOGIN_INTER_TEL_CODE = "sp_login_inter_tel_code";

    public static final String SP_DEVICE_STATUS = "sp_device_status";

    public static final String SP_FINGERPRINT_LOCK_ENABLE = "sp_fingerprint_lock_enable";

    public static final String SP_PATTERN_LOCK_ENABLE = "sp_pattern_lock_enable";

    public static final String SP_PATTERN_PASSWORD = "sp_pattern_password";

    public static final String SP_LOCK_INIT = "sp_lock_init";

    public static final String SP_USERID = "sp_userid";

    public static final String SP_PATTERN_WRONG_TIMES = "sp_pattern_wrong_times";

    public static final String SP_LAST_LOGIN_TIME = "sp_last_login_time";//本地缓存上一次登录的时间

    public static final String SCHEME_THIRD_PARTY_AUTH = "scheme_third_party_auth";
    /**************消息****************/
    public static final String SP_FCM_PUSH_TOKEN = "sp_fcm_push_token";

    public static final String SP_HUAWEI_PUSH_TOKEN = "sp_huawei_push_token";

    public static final String SP_XIAOMI_REG_ID = "sp_xiaomi_reg_id";

    public static final String SP_JPUSH_REG_ID = "sp_jpush_reg_id";

    //标志位，当推送token变化的时候需要在HomeActivity 启动时重新注册设备
    public static final String SP_RE_REGISTER_PUSH_TOKEN = "sp_re_register_push_token";

    //标志位，在上次注册设备失败后，在下次启动应用进入HomeActivity时重新注册设备
    public static final String SP_REGISTER_PUSH_FAILED = "sp_re_register_push_failed";

    public static final String SP_LAST_PUSH_REGISTER_NAME = "sp_last_push_register_name";

    public static final String SP_IM_TOKEN = "sp_im_token";

    public static final String SP_MESSAGE_SEARCH_KEYS = "sp_message_search_keys";

    public static final String SP_TENANT_MSG_GROUP_TOP = "sp_tenant_msg_group_top";
    /**************应用****************/
    public static final String SP_APP_SEARCH_KEYS = "sp_app_search_keys";

    public static final String SP_APP_UPDATE_TIP_ENABLE = "sp_app_update_tip_enable";

    public static final String SP_APP_AUTO_UPDATE_WHEN_WIFI = "sp_app_auto_update_when_wifi";

    public static final String SP_APP_AUTO_UPDATE_WHEN_G = "sp_app_auto_update_when_g";

    public static final String SP_APP_MULTI_PAGE = "sp_app_multi_page";

    public static final String NEED_OPEN_APP = "need_open_app";

    public static final String SCHEME_OPEN_APP = "scheme_open_app";

    public static final String SP_ADS_HAS_READ = "sp_ads_has_read";

    public static final String SP_LOCAL_SORT_AREA_MENU = "sp_local_sort_area_menu";
    /**************通讯录****************/
    public static final String SP_NEW_CONTACT_SEARCH_KEYS = "sp_new_contact_search_keys";
    public static final String SP_CONTACT_SEARCH_KEYS = "sp_contact_search_keys";
    public static final String SP_CONTACT_USER_HEAD = "sp_contact_user_head";
    public static final String SP_USER_INFO = "sp_user_info";
    public static final String SP_USER_TENANT_INFO = "sp_user_tenant_info";

}
