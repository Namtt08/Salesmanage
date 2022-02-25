package org.project.manage.util;

import java.time.ZoneId;

public interface AppConstants {

    String DEFAULT_PAGE_NUMBER = "1";
    String DEFAULT_OFFSET_NUMBER = "0";
    String DEFAULT_PAGE_SIZE = "25";

    int MAX_PAGE_SIZE = 100;
    int RESULT_CODE_SUCCESS = 0;
    int RESULT_CODE_NO_EXIST = -1;
    int RESULT_CODE_NOT_FOUND = -2;
    int RESULT_CODE_SYSTEM_ERROR = -99;

    String RESULT_CODE_SOAP_SUCCESS = "00000";

    int TIME_OUT_OTP = 120;

    int MAX_SIZE = 1048576;

    String DATE_FORMAT_ISO = "yyyy-MM-dd";

    String DATE_FORMAT = "dd/MM/yyyy";

    String DATETIME_FORMAT_ISO = "yyyy-MM-dd HH:mm:ss";

    String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    String TIME_FORMAT = "HH:mm:ss";

    String URL_MAP = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";

    int SUCCESS_CODE = 200;

    String SUCCESS = "success";
    String SPACE = " ";

    String ERROR = "error";
    String DENY_APPLICATION = "deny_application";
    String DEVICE_ID = "DEVICE-ID";
    String LANGUAGE_EN = "en";
    String LANGUAGE_VN = "vn";

    String TOKEN = "TOKEN";

    ZoneId VIETNAM_ZONE = ZoneId.of("Vietnam/Hanoi");

    String RE_SEND_OTP = "reSendOTP";
    String TOTAL_IMAGE = "totalImage";
    String VALIDATE_OTP = "validateOTP";
    String TIME_LOCKING_SEND_OTP = "timeLockingSendOTP";
    String TIME_OUT_OTP_EXPIRE = "timeOutOTP";
    String USER_AGENT = "User-Agent";
    String INTER_VIEW = "interview";
    String BINDING= "binding";
    String NUMBER_DAY_CANCEL_FILE = "NomDayCancelFile";

    String SUBJECT = "Thông tin tài khoản iShinhan";
    String SUBJECT_PIN_CODE = "Thông tin mã đăng nhập nhanh iShinhan";

    String LEVEL = "1";

    interface Header {

        String ACCEPT_LANGUAGE = "Accept-Language";
    }
}
