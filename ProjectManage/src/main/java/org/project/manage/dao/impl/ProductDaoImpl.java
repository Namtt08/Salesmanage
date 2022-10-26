package org.project.manage.dao.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.dao.ProductDao;
import org.project.manage.dto.ProductDto;
import org.project.manage.request.ProductListRequest;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ProductDaoImpl implements ProductDao {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDto> getListProduct(ProductListRequest request) {
		StringBuilder builder = new StringBuilder();

		builder.append(" SELECT * FROM ( ");
		this.generateQuery(builder, request, false);
		Query query = entityManager.createNativeQuery(builder.toString());
		setSearchFilter(request, query);
		//query.setFirstResult((request.getPage() - 1) * request.getSize());
		query.setFirstResult(request.getPage()-1);
		query.setMaxResults(request.getSize());

		List<Object[]> results = query.getResultList();

		return convertProduct(results);
	}

	private List<ProductDto> convertProduct(List<Object[]> results) {
		try {

            return results.stream()
                    .map(result -> new ProductDto( 
                    		((BigInteger) result[0]).longValue(),
                            (String) result[1], (String) result[2], 
                            (String) result[3], (String) result[4],
                            ((BigInteger) result[5]).longValue(), (String) result[6],
                            ((BigInteger) result[7]).longValue(), (String) result[8], (String) result[9] , 
                            ((BigInteger) result[16]).longValue(), ((BigInteger) result[17]).longValue(), 
                            (String) result[18] , (String) result[19]  , (String) result[20], (String) result[21]             		
                    		))
                    .collect(Collectors.toList());
        } catch (Exception e) {
        	log.error("#convertProduct#ERROR#: ", e.toString());
        	e.printStackTrace();
            return Collections.emptyList();
        }
	}

	private void setSearchFilter(ProductListRequest request, Query query) {
		if (StringUtils.isNotBlank(request.getProductType())) {
			query.setParameter("productType", request.getProductType());
		}
		if (request.getProductCategoryId() != null) {
			query.setParameter("productCategoryId", request.getProductCategoryId());
		}
		if (StringUtils.isNotBlank(request.getSaleStatus())) {
			query.setParameter("saleStatus",request.getSaleStatus());
		}
		if (StringUtils.isNotBlank(request.getSalesType())) {
			query.setParameter("salesType", request.getSalesType());
		}
		if (StringUtils.isNotBlank(request.getKey())) {
			query.setParameter("key", "%" + request.getKey().toLowerCase() + "%");
		}

	}

	private void generateQuery(StringBuilder builder, ProductListRequest request, boolean isCount) {
		builder.append(
				" select pd.id, pd.product_type, pd.product_brands,pd.product_name, pc.name product_category_name, pd.total_product,pd.sale_status, pd.price, pd.product_name as temp, CONVERT(VARCHAR(10), ISNULL(pd.created_date,SYSDATETIME()), 103) dateCreate, ");
		builder.append(
				" pd.sales_type, pd.status, ISNULL(pd.start_date,CONVERT(datetime,'1990-01-01',102)) start_date, ISNULL(pd.end_date,CONVERT(datetime,'9999-01-30',102)) end_date, pc.id product_category_id,pd.created_date,"
				+ "pd.product_category_id as product_cate_id, pd.user_id as partner_id , pd.insurance, pd.product_desc,  pd.code, u.full_name ");
		builder.append(" from product pd ");
		builder.append(" left join product_category pc on pd.product_category_id = pc.id and pd.product_name is not null");
		builder.append(" left join users u on u.id = pd.user_id ");
		//builder.append(" left join product_document pdc on pd.id = pdc.product_id and pdc.position =1");
		builder.append("  ) a");
		builder.append(" where 1=1");
		builder.append(" and a.status = 1 and a.product_category_name is not null");
		//builder.append(" AND SYSDATETIME() BETWEEN A.start_date AND A.end_date");	
		if (StringUtils.isNotBlank(request.getProductType())) {
			builder.append(" AND A.product_type =:productType");
		}
		if (request.getProductCategoryId() != null) {
			builder.append(" AND A.product_category_id =:productCategoryId");
		}
		if (StringUtils.isNotBlank(request.getSaleStatus())) {
			builder.append(" and a.sale_status =:saleStatus");
		}
		if (StringUtils.isNotBlank(request.getSalesType())) {
			builder.append(" and a.sales_type =:salesType");
		}
		if (StringUtils.isNotBlank(request.getKey())) {
			builder.append(" AND (LOWER(a.product_name) LIKE :key OR LOWER(a.product_brands) LIKE :key ) ");

		}

		if (!isCount) {
			builder.append(" ORDER BY");
			builder.append(" a.created_date desc");
		}

	}

	@Override
	public int countListProduct(ProductListRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT count(1) FROM ( ");
		this.generateQuery(builder, request, true);

		Query query = entityManager.createNativeQuery(builder.toString());
		setSearchFilter(request, query);
		 return query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
	}
	
}
