package org.project.manage.util;

public interface MessageResult {

	String SUCCESS = "Thành công";
	
	String ERROR = "Thất bại";

	String ERROR_COMMON = "Lỗi hệ thống";

	String AS_INVALID_HEADER = "Request không tồn tại";

	String DATA_NOT_FOUND_CORE = "Dữ liệu không tồn tại.";

	String ERROR_TOKEN = "Lỗi xác nhận chứng thực.";

	String VALID_EMAIL = "Định dạng email không đúng";

	String VALID_PROMO_CODE = "Mã khuyến mãi không chính xác";

	String VALID_REFERENCE_NUMBER = "Mã người giới thiệu không chính xác";

	String VALID_DATE = "Định dạng ngày giờ không chính xác.";

	String VALID_NOTNULL = "Gía trị không được trống.";

	String PAY_NOW = "Thanh toán lỗi.";

	String AS_OUT_OF_RESEND_TIMES = "Hết số lần gửi lại OTP";

	String GRD001_BLOCK = " GRD001_Bạn đã bị khóa tài khoản vui lòng liên hệ tổng đài viên";

	String GRD002_ROLE = "GRD002_Role is not found";
}
