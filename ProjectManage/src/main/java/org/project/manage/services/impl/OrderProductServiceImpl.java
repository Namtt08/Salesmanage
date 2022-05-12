package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.project.manage.dto.CartDto;
import org.project.manage.entities.CartTemp;
import org.project.manage.entities.Product;
import org.project.manage.entities.User;
import org.project.manage.exception.AppException;
import org.project.manage.repository.CartTempRepository;
import org.project.manage.repository.ProductDocumentRepository;
import org.project.manage.repository.ProductRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.services.OrderProductService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProductServiceImpl implements OrderProductService {

	@Autowired
	CartTempRepository cartTempRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductDocumentRepository productDocumentRepository;

	@Override
	public CartResponse addCart(CartAddRequest request, User user) {
		try {

			CartResponse response = new CartResponse();
			List<CartDto> listProduct = new ArrayList<CartDto>();
			CartTemp cart = cartTempRepository
					.findByUserIdAndProductIdOrderByIdDesc(user.getId(), request.getProductId()).orElse(null);
			Product product = productRepository.findById(request.getProductId()).orElse(null);
			if (product == null) {
				throw new AppException(MessageResult.GRD006_PRODUCT);
			}
			User partner = userRepository.findById(product.getUserId()).orElse(null);
			
			if (product.getTotalProduct() == 0) {
				throw new AppException(MessageResult.GRD007_PRODUCT);
			}

			if (request.getTotalProduct() == null) {
				request.setTotalProduct(1L);
			}
			if (cart != null) {
				cart.setUserId(user.getId());
				cart.setProductId(request.getProductId());
				cart.setPartnerId(request.getPartnerId());
				
				cart.setTotalProduct(cart.getTotalProduct()==null ? 0L:cart.getTotalProduct() + request.getTotalProduct());
				cartTempRepository.save(cart);
			} else {
				cart = new CartTemp();
				cart.setUserId(user.getId());
				cart.setProductId(request.getProductId());
				cart.setPartnerId(product.getUserId());
				cart.setTotalProduct(request.getTotalProduct());
				cartTempRepository.save(cart);
			}
			List<CartTemp> listCart = cartTempRepository.findByUserId(user.getId());

			response.setUserId(user.getId());
			Long total = 0L;
			for (CartTemp cartTemp : listCart) {
				CartDto productCart = new CartDto();
				productCart.setId(cartTemp.getId());
				productCart.setProductId(cartTemp.getProductId());
				productCart.setProductName(product.getProductName());
				productCart.setPrice(product.getPrice());
				productCart.setPartnerId(cartTemp.getPartnerId());
				if (partner != null) {
					productCart.setPartnerName(partner.getFullName());
				}
				productCart.setTotalProduct(cartTemp.getTotalProduct());
				listProduct.add(productCart);
				total = total + (cartTemp.getTotalProduct() == null ? 0L : cartTemp.getTotalProduct());

			}
			response.setTotal(total);
			response.setListCart(listProduct);
			return response;
		} catch (Exception e) {
			log.error("#addCart#ERROR#:" + e.getMessage());
			throw new AppException(MessageResult.ERROR_COMMON);
		}
	}

	@Override
	public ProductCartResponse changeProductCart(CartAddRequest request, User user) {
		ProductCartResponse response = new ProductCartResponse();
		try {
			CartTemp cart = cartTempRepository.findById(request.getId()).orElse(null);
			if (cart == null) {
				throw new AppException(MessageResult.ERROR_COMMON);
			}
			if (request.getTotalProduct() == null) {
				request.setTotalProduct(1L);
			}
			Product product = productRepository.findById(cart.getProductId()).orElse(null);
			if (AppConstants.decrease.equals(request.getType())) {
				cart.setTotalProduct(cart.getTotalProduct() - request.getTotalProduct());
			} else if (AppConstants.addition.equals(request.getType())) {
				cart.setTotalProduct(cart.getTotalProduct() + request.getTotalProduct());
			} else {
				cart.setTotalProduct(request.getTotalProduct());
			}
			if (cart.getTotalProduct() > product.getTotalProduct()) {
				cart.setTotalProduct(product.getTotalProduct());
				response.setMessageProduct(MessageResult.GRD008_PRODUCT + cart.getTotalProduct());
			}
			cartTempRepository.save(cart);
			response.setId(cart.getId());
			response.setTotalProduct(cart.getTotalProduct());
		} catch (Exception e) {
			log.error("#changeProductCart#ERROR#");
			throw new AppException(MessageResult.ERROR_COMMON);
		}
		return response;
	}

	@Override
	public ProductCartResponse deleteProductCart(CartAddRequest request, User user) {
		ProductCartResponse response = new ProductCartResponse();
		try {
			cartTempRepository.deleteById(request.getId());
		} catch (Exception e) {
			log.error("#deleteProductCart#ERROR#");
			throw new AppException(MessageResult.ERROR_COMMON);
		}
		return response;
	}

	@Override
	public CartResponse getCart(User user) {
		try {
			CartResponse response = new CartResponse();
			List<CartDto> listProduct = new ArrayList<CartDto>();
			List<CartTemp> listCart = cartTempRepository.findByUserId(user.getId());
			response.setUserId(user.getId());
			Long total = 0L;
			for (CartTemp cartTemp : listCart) {
				CartDto productCart = new CartDto();
				productCart.setId(cartTemp.getId());
				productCart.setProductId(cartTemp.getProductId());
				if (cartTemp.getProductId() == null) {
					continue;
				}
				Product product = productRepository.findById(productCart.getProductId()).orElse(null);
				if (product == null) {
					continue;
				}
				String bannerProduct= productDocumentRepository.getdocPathProductByIdAndPosition(product.getId(), 1L);
				productCart.setProductName(product.getProductName());
				productCart.setPrice(product.getPrice());
				productCart.setBannerPath(bannerProduct);
				productCart.setPartnerId(cartTemp.getPartnerId());
				if (cartTemp.getPartnerId() != null) {
					User partner = userRepository.findById(cartTemp.getPartnerId()).orElse(null);
					if (partner != null) {
						productCart.setPartnerName(partner.getFullName());
					}
				}
				productCart.setTotalProduct(cartTemp.getTotalProduct());
				listProduct.add(productCart);
				total = total + (cartTemp.getTotalProduct() == null ? 0L : cartTemp.getTotalProduct());

			}
			response.setTotal(total);
			response.setListCart(listProduct);
			return response;
		} catch (Exception e) {
			log.error("#getCart#ERROR#");
			throw new AppException(MessageResult.ERROR_COMMON);
		}

	}

}
