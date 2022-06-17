package org.project.manage.enums;

import java.util.stream.Stream;

public enum PaymentStatusEnum {
	PAID(1, "Đã thanh toán"), UNPAID(0, "Chưa thanh toán");

	private int value;
	private String name;

	PaymentStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;

	}

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public static PaymentStatusEnum getByValue(int value) {
		return Stream.of(PaymentStatusEnum.values()).filter(ele -> value==ele.value)
				.findFirst().orElse(null);
	}
}
