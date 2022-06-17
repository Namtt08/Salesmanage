package org.project.manage.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.project.manage.entities.OrderProductHistory;
import org.project.manage.enums.OrderStatusEnum;
import org.project.manage.util.DateHelper;

import lombok.Data;

@Data
public class OrderProductHistoryDto {
	
	private String status;

	private String codeOrders;
	
	private String paymentMethod;
	
	private Integer paymentStatus;
	
	private String createdBy;

	private String createdDate;
	
	public OrderProductHistoryDto (OrderProductHistory entity) {
		this.status =OrderStatusEnum.getByValue(entity.getStatus()).getName();
		this.codeOrders=entity.getCodeOrders();
		this.paymentMethod=entity.getPaymentMethod();
		this.paymentStatus=entity.getPaymentStatus();
		this.createdDate = DateHelper.convertDateTime(entity.getCreatedDate());
		this.createdBy = entity.getCreatedBy();
	}
	
	public OrderProductHistoryDto () {
	}

}
