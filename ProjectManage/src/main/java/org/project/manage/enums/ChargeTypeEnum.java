package org.project.manage.enums;

import java.util.stream.Stream;

public enum ChargeTypeEnum {
	RECHARGE(1, "Nạp tiền"), PAY(2, "Thanh toán"), REFUND(3, "Hoàn tiền");

	private int value;
	private String name;

	ChargeTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;

	}

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public static ChargeTypeEnum getByValue(int value) {
		return Stream.of(ChargeTypeEnum.values()).filter(ele -> value==ele.value)
				.findFirst().orElse(null);
	}
}
