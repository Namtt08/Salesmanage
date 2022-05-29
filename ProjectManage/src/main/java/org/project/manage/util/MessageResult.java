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

	String GRD001_BLOCK = " GRD001: Bạn đã bị khóa tài khoản vui lòng liên hệ tổng đài viên";

	String GRD002_ROLE = "GRD002: Role is not found";
	
	String GRD003_OTP = "GRD003: Bạn đã vượt quá số lần gửi OTP trong ngày";
	
	String GRD004_NOT_FOUND = "GRD004: Thông tin người dùng không tồn tại";
	
	String GRD005_NOT_FOUND = "GRD005: Tệp tin không tồn tại";
	
	String GRD006_PRODUCT = "Sản phẩm đã bị xóa hoặc tạm ngưng bán hàng";
	
	String GRD007_PRODUCT = "Sản phẩm đã hết hàng";
	
	String GRD008_PRODUCT = "Sản phẩm đã đạt số lượng tối đa: ";
	
	String GRD009_PRODUCT = "Sản phẩm đã đạt số lượng tối đa";
	
	String GRD010_PRODUCT = "Sản phẩm đã cập nhật số lượng";
	
	String GRD011_ORDER = "Khuyến mãi đã hết hiệu lực";
	
	String GRD011_PAYMENT = "Số dư tài khoản không đủ vui lòng nạp thêm hoặc chọn kênh giao dịch khác";
	
}
