package org.project.manage.dao.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.dao.OrderProductDao;
import org.project.manage.dao.ProductDao;
import org.project.manage.dto.OrderStatusProducDto;
import org.project.manage.dto.ProductDto;
import org.project.manage.request.ProductListRequest;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class OrderProductImpl implements OrderProductDao {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderStatusProducDto> getListOrderProductStatus(Long userId) {
		StringBuilder builder = new StringBuilder();
		this.generateQuery(builder, userId);

		Query query = entityManager.createNativeQuery(builder.toString());
		setSearchFilter(userId, query);

		List<Object[]> results = query.getResultList();

		return convertOrderStatusProduc(results);
	}

	private List<OrderStatusProducDto> convertOrderStatusProduc(List<Object[]> results) {
		try {
			return results.stream()
					.map(result -> new OrderStatusProducDto(((BigInteger) result[0]).longValue(), (String) result[1],
							((BigInteger) result[2]).longValue(), ((BigInteger) result[3]).longValue(),
							((BigInteger) result[4]).longValue(), (String) result[5], (String) result[6],
							((BigInteger) result[7]).longValue(), (String) result[8],
							((BigInteger) result[9]).longValue(), (String) result[10], (String) result[11],
							(String) result[12], (String) result[13], ((BigInteger) result[14]).longValue(),
							((BigInteger) result[15]).longValue(), ((BigInteger) result[16]).longValue(),
							(String) result[17], (String) result[18]))
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("#convertOrderStatusProduc#ERROR#: ", e.toString());
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private void setSearchFilter(Long userId, Query query) {

		if (userId != null) {
			query.setParameter("userId", userId);
		}

	}

	private void generateQuery(StringBuilder builder, Long userId) {
		builder.append("SELECT \r\n" + "tpo.product_id,\r\n" + "p.product_name,\r\n" + "p.price,\r\n"
				+ "tpo.total_product,\r\n" + "tpo.price_after_promotion,\r\n" + "tpo.order_status,\r\n"
				+ "tpo.order_product_code,\r\n" + "op.partner_id,\r\n" + "op.uuid_id,\r\n" + "op.total_amount,\r\n"
				+ "op.created_by,\r\n"
				+ "CONVERT(VARCHAR(10), ISNULL(op.created_date,SYSDATETIME()), 103) created_date,\r\n"

				+ "op.payment_status,\r\n" + "op.payment_method,\r\n" + "op.voucher_id,\r\n" + "op.promotion_id,\r\n"
				+ "op.total_discount,\r\n" + "op.delivery_address,\r\n" + "op.note");

		builder.append(" from transaction_product_order tpo");
		builder.append(" left join order_product op on op.code_orders = tpo.order_product_code");
		builder.append(" left join product p on p.id = tpo.product_id ");
		builder.append(" where 1=1");

		if (userId != null) {
			builder.append(" AND op.user_id  =:userId");
		}
		builder.append(" ORDER BY");
		builder.append(" op.created_date desc");

	}

}
