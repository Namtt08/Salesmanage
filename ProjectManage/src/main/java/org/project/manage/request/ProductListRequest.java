package org.project.manage.request;

import lombok.Data;

@Data
public class ProductListRequest {

	private String productType;
	private Integer productCategoryId;
	private String saleStatus;
	private String salesType;
	private String key;
	private Integer page = 1;
	private Integer size = 25;
}
