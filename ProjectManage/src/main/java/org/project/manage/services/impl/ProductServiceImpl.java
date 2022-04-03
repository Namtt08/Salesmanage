package org.project.manage.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.dao.ProductDao;
import org.project.manage.dto.ProductCategoryDto;
import org.project.manage.dto.ProductDocmentDto;
import org.project.manage.dto.ProductDto;
import org.project.manage.entities.Product;
import org.project.manage.entities.ProductCategory;
import org.project.manage.entities.ProductDocument;
import org.project.manage.repository.ProductCategoryRepository;
import org.project.manage.repository.ProductDocumentRepository;
import org.project.manage.repository.ProductRepository;
import org.project.manage.request.ProductListRequest;
import org.project.manage.response.ListProductRespose;
import org.project.manage.response.ProductDetailResponse;
import org.project.manage.services.ProductService;
import org.project.manage.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductDocumentRepository productDocumentRepository;

	@Override
	public List<ProductCategoryDto> getAllProductCategory() {
		try {
			List<ProductCategoryDto> listProductCategory = new ArrayList<>();
			List<ProductCategory> listProduct = productCategoryRepository.findAll();
			listProductCategory = listProduct.stream().map(productCategory -> new ProductCategoryDto(productCategory))
					.collect(Collectors.toList());
			return listProductCategory;
		} catch (Exception e) {
			log.error("#getAllProductCategory#ERROR#:" + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public ListProductRespose getListProduct(ProductListRequest request) {
		List<ProductDto> listProduct = productDao.getListProduct(request);
		int total = productDao.countListProduct(request);
		return new ListProductRespose(total, listProduct);
	}

	@Override
	public ProductDetailResponse getProductDetail(Long id) {
		ProductDetailResponse response = new ProductDetailResponse();
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
		Product product = this.productRepository.findById(id).orElse(null);
		if (product != null) {
			List<ProductDocmentDto> listDocument = new ArrayList<ProductDocmentDto>();
			response.setId(product.getId());
			response.setProductType(product.getProductType());
			response.setProductBrands(product.getProductBrands());
			if (product.getInsuranceDate() != null) {
				response.setInsuranceDate(formatter.format(product.getInsuranceDate()));
			}
			response.setCode(product.getCode());
			response.setSerial(product.getSerial());
			response.setLotNumber(product.getLotNumber());
			response.setChassisNumber(product.getChassisNumber());
			response.setLicensePlate(product.getLicensePlate());
			response.setSaleStatus(product.getSaleStatus());
			response.setProductName(product.getProductName());
			if (product.getProductCategoryId() != null) {
				ProductCategory productCategory = productCategoryRepository.findById(product.getProductCategoryId())
						.orElse(null);
				if (productCategory != null) {
					response.setProductCategoryName(productCategory.getName());
				}
			}
			response.setTotalProduct(product.getTotalProduct());
			response.setPrice(product.getPrice());
			if (product.getCreatedDate() != null) {
				response.setCreatedDate(formatter.format(product.getCreatedDate()));
			}
			List<ProductDocument> productDocumentList = productDocumentRepository.findByProductId(product.getId());
			if (productDocumentList != null && !productDocumentList.isEmpty()) {
				listDocument = productDocumentList.stream()
						.map(productDocument -> new ProductDocmentDto(productDocument)).collect(Collectors.toList());
				response.setProductDocuments(listDocument);
			}

		}
		return response;
	}

}
