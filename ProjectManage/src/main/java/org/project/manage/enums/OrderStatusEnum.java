package org.project.manage.enums;

import java.util.stream.Stream;

public enum OrderStatusEnum {
	FAIL("-1", "Đơn hàng đã hủy"), NEW("0", "Đang xử lý"), WAITING("1", "Đang giao"),
	SUCCESS("2", "Đã hoàn thành");

	private String value;
	private String name;

	OrderStatusEnum(String value, String name) {
		this.value = value;
		this.name = name;

	}

	public String getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public static OrderStatusEnum getByValue(String value) {
		return Stream.of(OrderStatusEnum.values()).filter(ele -> value.equalsIgnoreCase(String.valueOf(ele.value)))
				.findFirst().orElse(null);
	}
}
