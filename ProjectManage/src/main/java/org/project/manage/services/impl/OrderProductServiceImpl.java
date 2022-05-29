package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.project.manage.dto.CartDto;
import org.project.manage.dto.PromotionDto;
import org.project.manage.entities.CartTemp;
import org.project.manage.entities.OrderProduct;
import org.project.manage.entities.PaymentHistory;
import org.project.manage.entities.Product;
import org.project.manage.entities.Promotion;
import org.project.manage.entities.TransactionProductOrder;
import org.project.manage.entities.User;
import org.project.manage.entities.UserPromotion;
import org.project.manage.entities.Voucher;
import org.project.manage.exception.AppException;
import org.project.manage.repository.CartTempRepository;
import org.project.manage.repository.OrderProductRepository;
import org.project.manage.repository.PaymentHistoryRepository;
import org.project.manage.repository.ProductDocumentRepository;
import org.project.manage.repository.ProductRepository;
import org.project.manage.repository.PromotionRepository;
import org.project.manage.repository.TransactionProductOrderRepository;
import org.project.manage.repository.UserPromotionRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.repository.VoucherRepository;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.PaymentOrderResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.services.OrderProductService;
import org.project.manage.util.AppConstants;
import org.project.manage.util.DateHelper;
import org.project.manage.util.MessageResult;
import org.project.manage.util.StringHelper;
import org.project.manage.util.SystemConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;
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

	@Autowired
	TransactionProductOrderRepository transactionProductOrderRepository;

	@Autowired
	OrderProductRepository orderProductRepository;

	@Autowired
	private PromotionRepository promotionRepository;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private UserPromotionRepository userPromotionRepository;

	@Autowired
	private PaymentHistoryRepository paymentHistoryRepository;

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

				cart.setTotalProduct(
						cart.getTotalProduct() == null ? 0L : cart.getTotalProduct() + request.getTotalProduct());
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
				if (product.getStatus() != SystemConfigUtil.STATUS_ACTIVE) {
					cartTemp.setTotalProduct(0L);
					cartTempRepository.save(cartTemp);
				}
				if (cartTemp.getTotalProduct() > product.getTotalProduct()) {
					cartTemp.setTotalProduct(product.getTotalProduct());
					response.setMessageProduct(MessageResult.GRD008_PRODUCT + cartTemp.getTotalProduct());
					cartTempRepository.save(cartTemp);
				}
				String bannerProduct = productDocumentRepository.getdocPathProductByIdAndPosition(product.getId(), 1L);
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

	@Override
	public CartResponse getPaymentOrder(CartResponse request, User user) {
		try {
			CartResponse response = new CartResponse();
			List<CartDto> listProduct = new ArrayList<CartDto>();
			List<CartDto> listCart = request.getListCart();
			response.setUserId(user.getId());
			Long total = 0L;
			Long totalDiscount = 0L;
			Long maxAmountDiscount = null;
			if (!Objects.isNull(request.getPromotionDto())) {
				if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.VOURCHER)) {
					Voucher voucher = voucherRepository.findById(request.getPromotionDto().getId())
							.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
					maxAmountDiscount = voucher.getPoint();
				} else if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.PROMOTION)) {
					Promotion promotion = promotionRepository.findById(request.getPromotionDto().getId())
							.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
					maxAmountDiscount = promotion.getMaxAmount();
				}
			}
			for (CartDto cartTemp : listCart) {
				CartDto productCart = new CartDto();
				productCart.setCartId(cartTemp.getCartId());
				// productCart.setId(cartTemp.getId());
				productCart.setProductId(cartTemp.getProductId());
				if (cartTemp.getProductId() == null) {
					continue;
				}
				Product product = productRepository.findById(productCart.getProductId()).orElse(null);
				if (product == null) {
					continue;
				}
				if (product.getStatus() != SystemConfigUtil.STATUS_ACTIVE) {
					throw new AppException(MessageResult.GRD007_PRODUCT);
				}
				if (cartTemp.getTotalProduct() > product.getTotalProduct()) {
					throw new AppException(MessageResult.GRD010_PRODUCT);
				}
				String bannerProduct = productDocumentRepository.getdocPathProductByIdAndPosition(product.getId(), 1L);
				productCart.setProductName(product.getProductName());
				productCart.setPrice(product.getPrice());
				productCart.setBannerPath(bannerProduct);
				productCart.setPartnerId(product.getUserId());
				if (product.getUserId() != null) {
					User partner = userRepository.findById(product.getUserId()).orElse(null);
					if (partner != null) {
						productCart.setPartnerName(partner.getFullName());
					}
				}
				productCart.setTotalProduct(cartTemp.getTotalProduct());
				listProduct.add(productCart);
				total = total + (product.getPrice() * cartTemp.getTotalProduct());
				if (!Objects.isNull(request.getPromotionDto())) {
					if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.VOURCHER)) {
						Voucher voucher = voucherRepository.findById(request.getPromotionDto().getId())
								.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
						totalDiscount = voucher.getPoint();
					} else if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.PROMOTION)) {
						Promotion promotion = promotionRepository.findById(request.getPromotionDto().getId())
								.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
						if (promotion.getProductCategoryId() == product.getProductCategoryId()
								&& StringUtils.equals(promotion.getUserType(), user.getUserType())) {

							if (StringUtils.equals(promotion.getPromotionType(), SystemConfigUtil.percentage)) {
								float value = (float) (promotion.getPromotionValue()) / 100;
								totalDiscount = (long) (totalDiscount
										+ (product.getPrice() * value * cartTemp.getTotalProduct()));

							} else if (StringUtils.equals(promotion.getPromotionType(), SystemConfigUtil.discount)) {
								totalDiscount = (long) (totalDiscount + promotion.getPromotionValue());
							}
							if (!Objects.isNull(maxAmountDiscount) && totalDiscount > maxAmountDiscount) {
								totalDiscount = maxAmountDiscount;
							}
						}
					}
				}
			}
			response.setTotalAmount(total - totalDiscount);
			response.setListCart(listProduct);

			return response;
		} catch (Exception e) {
			log.error("#getPaymentOrder#ERROR#");
			throw e;
		}
	}

	@Override
	@Transactional
	public PaymentOrderResponse paymentOrder(CartResponse request, User user) {
		PaymentOrderResponse response = new PaymentOrderResponse();
		String uuid = UUID.randomUUID().toString();
		String yyyymmddhhmmss = DateHelper.convertDateTimeToString(new Date());
		List<CartDto> listCart = request.getListCart();
		if (!Objects.isNull(request.getPromotionDto())) {
			if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.VOURCHER)) {
				Voucher voucher = voucherRepository.findById(request.getPromotionDto().getId())
						.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
				request.setMaxAmountDiscount(voucher.getPoint());
			} else if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.PROMOTION)) {
				Promotion promotion = promotionRepository.findById(request.getPromotionDto().getId())
						.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
				request.setMaxAmountDiscount(promotion.getMaxAmount());
			}
		}
		Map<Long, List<CartDto>> mapCart = listCart.stream().collect(Collectors.groupingBy(CartDto::getPartnerId));
		mapCart.forEach((k, v) -> {
			OrderProduct orderEnity = new OrderProduct();
			orderEnity.setUserId(user.getId());
			orderEnity.setUuidId(uuid);
			orderEnity.setPartnerId(k);
			orderEnity.setCreatedBy(user.getUsername());
			orderEnity.setCreatedDate(new Date());
			String orderCode = StringHelper.randomString(3) + yyyymmddhhmmss;
			orderEnity.setCodeOrders(orderCode);
			orderEnity.setStatus(String.valueOf(0));
			orderEnity.setPaymentStatus(0);
			Long totalAmount = 0L;
			Long totalDiscount = 0L;
			for (CartDto cartTemp : v) {
				TransactionProductOrder transaction = new TransactionProductOrder();
				Product product = productRepository.findById(cartTemp.getProductId()).orElse(null);
				if (product == null) {
					continue;
				}
				if (product.getStatus() != SystemConfigUtil.STATUS_ACTIVE) {
					throw new AppException(MessageResult.GRD007_PRODUCT);
				}
				if (cartTemp.getTotalProduct() > product.getTotalProduct()) {
					throw new AppException(MessageResult.GRD010_PRODUCT);
				}
				product.setTotalProduct(product.getTotalProduct()-cartTemp.getTotalProduct());
				productRepository.save(product);
				transaction.setProductId(product.getId());
				transaction.setAmount(product.getPrice());
				transaction.setTotalProduct(cartTemp.getTotalProduct());
				transaction.setCreatedDate(new Date());
				transaction.setCreatedBy(String.valueOf(user.getId()));
				transaction.setOrderProductCode(orderCode);
				if (!Objects.isNull(request.getPromotionDto())) {
					if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.VOURCHER)) {
						Voucher voucher = voucherRepository.findById(request.getPromotionDto().getId())
								.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
						totalDiscount = voucher.getPoint();
						voucher.setStatus(false);
						voucherRepository.save(voucher);
						orderEnity.setVoucherId(voucher.getId());
					} else if (StringUtils.equals(request.getPromotionDto().getType(), SystemConfigUtil.PROMOTION)) {
						// userPromotion
						Promotion promotion = promotionRepository.findById(request.getPromotionDto().getId())
								.orElseThrow(() -> new AppException(MessageResult.GRD011_ORDER));
						if (promotion.getProductCategoryId() == product.getProductCategoryId()
								&& StringUtils.equals(promotion.getUserType(), user.getUserType())) {
							Long promotionTotal = promotion.getPromotionTotal();
							if (Objects.isNull(promotion.getPromotionTotal()))
								promotionTotal = 9999L;
							List<UserPromotion> userPromotionList = userPromotionRepository
									.findByUserIdAndPromotionId(user.getId(), promotion.getId());
							if (promotionTotal > userPromotionList.size()) {
								orderEnity.setPromotionId(promotion.getId());
								UserPromotion userPromotion = new UserPromotion();
								userPromotion.setPromotionId(promotion.getId());
								userPromotion.setUserId(user.getId());
								userPromotionRepository.save(userPromotion);
								if (StringUtils.equals(promotion.getPromotionType(), SystemConfigUtil.percentage)) {
									float value = (float) (promotion.getPromotionValue()) / 100;
									totalDiscount = (long) (totalDiscount
											+ (product.getPrice() * value * cartTemp.getTotalProduct()));
								} else if (StringUtils.equals(promotion.getPromotionType(),
										SystemConfigUtil.discount)) {
									totalDiscount = (long) (totalDiscount + promotion.getPromotionValue());
								}
								if (!Objects.isNull(request.getMaxAmountDiscount())){
									if (totalDiscount > request.getMaxAmountDiscount()) {
										totalDiscount = request.getMaxAmountDiscount();
										request.setMaxAmountDiscount(0L);
									} else {
										request.setMaxAmountDiscount(request.getMaxAmountDiscount() - totalDiscount);
									}
								}
								
							}
						}

					}
				}
				totalAmount = totalAmount + (product.getPrice() * cartTemp.getTotalProduct()) - totalDiscount;
				if (StringUtils.equals(SystemConfigUtil.WALLET, request.getPaymentMethod())) {
					if (totalAmount > user.getPoint()) {
						throw new AppException(MessageResult.GRD011_PAYMENT);
					}
					user.setPoint(user.getPoint() - totalAmount);
					userRepository.save(user);
					orderEnity.setPaymentStatus(1);
				}
				if (!Objects.isNull(cartTemp.getCartId())) {
					CartTemp cart = cartTempRepository.findById(cartTemp.getCartId()).orElse(null);
					if (cart != null) {
						cartTempRepository.delete(cart);
					}
				}
				transactionProductOrderRepository.save(transaction);
			}
			orderEnity.setPaymentMethod(request.getPaymentMethod());
			orderEnity.setTotalAmount(totalAmount);
			orderProductRepository.save(orderEnity);
			if (StringUtils.equals(SystemConfigUtil.WALLET, request.getPaymentMethod())) {
				PaymentHistory paymentHistory = new PaymentHistory();
				paymentHistory.setAmount(totalAmount);
				paymentHistory.setCreatedDate(new Date());
				paymentHistory.setUserId(user.getId());
				paymentHistory.setCodeOrders(orderCode);
				paymentHistory.setChargeType(2);
				paymentHistory.setCreatedBy(user.getUsername());
				paymentHistory.setDescription("Thanh toán đơn hàng: " + orderCode);
				paymentHistoryRepository.save(paymentHistory);
			}
		});
		response.setOrderId(uuid);
		return response;
	}

	@Override
	@SneakyThrows
	public List<PromotionDto> promotionOrder(CartResponse request, User user) {
		List<PromotionDto> response = new ArrayList<PromotionDto>();
		if (request != null) {
			List<CartDto> listCart = request.getListCart();
			listCart.stream().forEach(x -> {
				Product product = productRepository.findById(x.getProductId()).orElse(null);
				List<UserPromotion> userPromotion = userPromotionRepository.findByUserIdAndPromotionId(user.getId(),
						x.getProductId());
				List<PromotionDto> listPromotion = promotionRepository
						.findByAndProductCategoryIdAndUserType(product.getProductCategoryId(), user.getUserType())
						.stream()
						.filter(promotion -> (promotion.getPromotionTotal() == null ? 9999L
								: promotion.getPromotionTotal()) > userPromotion.size())
						.map(promote -> new PromotionDto(promote)).collect(Collectors.toList());
				response.addAll(listPromotion);
			});
		}
		List<PromotionDto> listvoucher = voucherRepository.findByUserId(user.getId()).stream()
				.map(voucher -> new PromotionDto(voucher)).collect(Collectors.toList());
		response.addAll(listvoucher);
		return response;
	}
}
