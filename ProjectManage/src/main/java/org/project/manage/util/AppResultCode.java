package org.project.manage.util;

public interface AppResultCode {

	int SUCCESS =0;
	int ERROR =-99;
	int AS_SUCCESS = 200;
	int AS_ERROR = 500;
	int AS_DB_ERROR = 501;
	int AS_DEVICE_BLOCK = 502;
	int AS_NOT_FOUND_RECORD = 506;
	int AS_CONNECTION_TIMEOUT = 509;
	int ACCOUNT_DELETE = 507;

}
