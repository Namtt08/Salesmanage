package org.project.manage.services.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.dao.OrderProductDao;
import org.project.manage.dto.CartDto;
import org.project.manage.dto.MailDto;
import org.project.manage.dto.OrderPaymentDto;
import org.project.manage.dto.OrderProductDto;
import org.project.manage.dto.OrderProductHistoryDto;
import org.project.manage.dto.OrderStatusProducDto;
import org.project.manage.dto.PromotionDto;
import org.project.manage.dto.PromotionSearchDto;
import org.project.manage.dto.PushNotificationRequest;
import org.project.manage.entities.CartTemp;
import org.project.manage.entities.MailTemplate;
import org.project.manage.entities.NotificationTemplateEntity;
import org.project.manage.entities.OrderProduct;
import org.project.manage.entities.OrderProductHistory;
import org.project.manage.entities.PaymentHistory;
import org.project.manage.entities.Product;
import org.project.manage.entities.ProductCategory;
import org.project.manage.entities.Promotion;
import org.project.manage.entities.SystemSetting;
import org.project.manage.entities.TransactionProductOrder;
import org.project.manage.entities.User;
import org.project.manage.entities.UserPromotion;
import org.project.manage.entities.Voucher;
import org.project.manage.enums.OrderStatusEnum;
import org.project.manage.enums.PaymentStatusEnum;
import org.project.manage.exception.AppException;
import org.project.manage.repository.CartTempRepository;
import org.project.manage.repository.MailTemplateRepository;
import org.project.manage.repository.NotificationTemplateRepository;
import org.project.manage.repository.OrderProductHistoryRepository;
import org.project.manage.repository.OrderProductRepository;
import org.project.manage.repository.PaymentHistoryRepository;
import org.project.manage.repository.ProductCategoryRepository;
import org.project.manage.repository.ProductDocumentRepository;
import org.project.manage.repository.ProductRepository;
import org.project.manage.repository.PromotionRepository;
import org.project.manage.repository.TransactionProductOrderRepository;
import org.project.manage.repository.UserPromotionRepository;
import org.project.manage.repository.UserRepository;
import org.project.manage.repository.VoucherRepository;
import org.project.manage.request.CartAddRequest;
import org.project.manage.response.CartResponse;
import org.project.manage.response.ListOrderResponse;
import org.project.manage.response.ListSearchPromotionResponse;
import org.project.manage.response.PaymentOrderDetailResponse;
import org.project.manage.response.PaymentOrderResponse;
import org.project.manage.response.ProductCartResponse;
import org.project.manage.response.PromotionProductOrderResponse;
import org.project.manage.services.EmailService;
import org.project.manage.services.FCMService;
import org.project.manage.services.OrderProductService;
import org.project.manage.services.SystemSettingService;
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
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

	@Autowired
	CartTempRepository cartTempRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	private EmailService emailService;

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

	@Autowired
	private OrderProductHistoryRepository orderProductHistoryRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private OrderProductDao orderProductDao;

	@Autowired
	private SystemSettingService systemSettingService;

	@Autowired
	private MailTemplateRepository mailTemplateRepository;
	
	@Autowired
	private NotificationTemplateRepository notificationTemplateRepository;
	
    @Autowired
    private FCMService fcmService;

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
			cartTempRepository.deleteProductCardTemp(request.getId(), user.getId());
		} catch (Exception e) {
			e.printStackTrace();
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
				productCart.setPartnerId(product.getUserId());
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
			response.setPriceTotal(total);
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
		SystemSetting systemSetting = systemSettingService.findByCode(SystemConfigUtil.SEND_EMAIL);
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
			orderEnity.setPromotionId(0L);// mặc đinh = 0
			orderEnity.setVoucherId(0L);// mặc đinh = 0
			orderEnity.setUserId(user.getId());
			orderEnity.setUuidId(uuid);
			orderEnity.setDeliveryAddress(request.getDeliveryAddress());
			orderEnity.setPartnerId(k);
			orderEnity.setCreatedBy(user.getUsername());
			orderEnity.setCreatedDate(new Date());
			orderEnity.setModifiedDate(new Date());
			String orderCode = StringHelper.randomString(3) + yyyymmddhhmmss;
			orderEnity.setCodeOrders(orderCode);
			orderEnity.setStatus(OrderStatusEnum.NEW.getValue());
			orderEnity.setPaymentStatus(PaymentStatusEnum.UNPAID.getValue());
			orderEnity.setNote(request.getNote());
			Long totalAmount = 0L;
			Long totalDiscount = 0L;
			for (CartDto cartTemp : v) {
				Long priceAfterPromotion = 0L;
				boolean flagPromotion = false;
				TransactionProductOrder transaction = new TransactionProductOrder();
				Product product = productRepository.findById(cartTemp.getProductId()).orElse(null);
				if (product == null) {
					continue;
				}
				if (product.getStatus() != SystemConfigUtil.STATUS_ACTIVE) {
					throw new AppException(
							product.getCode() + "- " + product.getProductName() + ": " + MessageResult.GRD006_PRODUCT);
				}
				if (product.getTotalProduct() == 0) {
					throw new AppException(
							product.getCode() + "- " + product.getProductName() + ": " + MessageResult.GRD007_PRODUCT);
				}
				if (cartTemp.getTotalProduct() > product.getTotalProduct()) {
					throw new AppException(MessageResult.GRD015_PRODUCT);
				}
				product.setTotalProduct(product.getTotalProduct() - cartTemp.getTotalProduct());
				if (product.getTotalProduct() <= Long.valueOf(systemSetting.getValue())) {
					this.sendEmailProduct(SystemConfigUtil.MAIL_PRODUCT, product);

				}
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
								flagPromotion = true; // Nếu = false thì totalAmount = totalAmount + productPrice;
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
								if (!Objects.isNull(request.getMaxAmountDiscount())) {
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
				if (flagPromotion) {
					totalAmount = totalAmount + (product.getPrice() * cartTemp.getTotalProduct()) - totalDiscount;
					priceAfterPromotion = (product.getPrice() * cartTemp.getTotalProduct()) - totalDiscount;
				} else {
					totalAmount = totalAmount + (product.getPrice() * cartTemp.getTotalProduct());
					priceAfterPromotion = (product.getPrice() * cartTemp.getTotalProduct());
				}
				orderEnity.setTotalDiscount(totalDiscount);
				if (StringUtils.equals(SystemConfigUtil.WALLET, request.getPaymentMethod())) {

					if (user.getPoint() == null || totalAmount > user.getPoint()) {
						throw new AppException(MessageResult.GRD011_PAYMENT);
					}
					user.setPoint(user.getPoint() - totalAmount);
					userRepository.save(user);
					orderEnity.setPaymentStatus(PaymentStatusEnum.PAID.getValue());
				}
				if (!Objects.isNull(cartTemp.getCartId())) {
					CartTemp cart = cartTempRepository.findById(cartTemp.getCartId()).orElse(null);
					if (cart != null) {
						cartTempRepository.delete(cart);
					}
				}
				transaction.setPriceAfterPromotion(priceAfterPromotion);
				transaction.setOrderStatus(OrderStatusEnum.NEW.getValue());
				transactionProductOrderRepository.save(transaction);
			}
			orderEnity.setPaymentMethod(request.getPaymentMethod());
			orderEnity.setTotalAmount(totalAmount);
			orderProductRepository.save(orderEnity);
			// push noti
			Optional<NotificationTemplateEntity>  notificationTemplateEntityOptional = notificationTemplateRepository.findByNotiType("ORDER_PRODUCT");
			if(notificationTemplateEntityOptional.isPresent()) {
			NotificationTemplateEntity notificationTemplateEntity= notificationTemplateEntityOptional.get();
			PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
			String title = notificationTemplateEntity.getTitle();
			DecimalFormat formatter = new DecimalFormat("###,###,###");
			String body = notificationTemplateEntity.getBody().replace("[order_code]", orderEnity.getCodeOrders());
			body = body.replace("[amount]", formatter.format(orderEnity.getTotalAmount()));
			pushNotificationRequest.setTitle(title);
			pushNotificationRequest.setBody(body);
			pushNotificationRequest.setUserId(user.getId());
			pushNotificationRequest.setNotificationTemplateId(notificationTemplateEntity.getId());
			pushNotificationRequest.setType(notificationTemplateEntity.getNotiType());
			pushNotificationRequest.setToken(user.getTokenFirebase());
			fcmService.pushNotification(pushNotificationRequest);
			this.sendEmailOrder(SystemConfigUtil.MAIL_ORDER, orderEnity);
			}
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

	private void sendEmailOrder(String code, OrderProduct orderEnity) {
		User userSendMail = userRepository.findById(orderEnity.getPartnerId()).orElse(null);
		MailTemplate mailTemplate = mailTemplateRepository.findByCode(code).orElse(null);
		if (!Objects.isNull(userSendMail) && !Objects.isNull(mailTemplate) && StringUtils.isNotBlank(userSendMail.getEmail())) {
			MailDto emailDto = new MailDto();
			emailDto.setMailCc(mailTemplate.getCc());
			emailDto.setMailBcc(mailTemplate.getBcc());
			emailDto.setMailSubject(mailTemplate.getSubject());
			String content = mailTemplate.getContent();
			content = content.replace("[codeOrders]", orderEnity.getCodeOrders());
			emailDto.setMailContent(content);
			emailDto.setMailTo(userSendMail.getEmail());
			emailService.sendEmail(emailDto);
		}
		
	}

	@Override
	public void sendEmailProduct(String code, Product product) {
		// TODO Auto-generated method stub
		User userSendMail = userRepository.findById(product.getUserId()).orElse(null);
		MailTemplate mailTemplate = mailTemplateRepository.findByCode(code).orElse(null);
		if (!Objects.isNull(userSendMail) && !Objects.isNull(mailTemplate) && StringUtils.isNotBlank(userSendMail.getEmail())) {
			MailDto emailDto = new MailDto();
			emailDto.setMailCc(mailTemplate.getCc());
			emailDto.setMailBcc(mailTemplate.getBcc());
			emailDto.setMailSubject(mailTemplate.getSubject());
			String content = mailTemplate.getContent();
			content = content.replace("[productName]", product.getProductName());
			emailDto.setMailContent(content);
			emailDto.setMailTo(userSendMail.getEmail());
			emailService.sendEmail(emailDto);
		}
	}

	@Override
	@SneakyThrows
	public PromotionProductOrderResponse promotionOrder(CartResponse request, User user) {
		PromotionProductOrderResponse response = new PromotionProductOrderResponse();
		List<PromotionDto> listData = new ArrayList<PromotionDto>();
		if (request != null) {
			List<CartDto> listCart = request.getListCart();
			listCart.stream().forEach(x -> {
				Product product = productRepository.findById(x.getProductId()).orElse(null);
				List<PromotionDto> listPromotion = promotionRepository
						.findByAndProductCategoryIdAndUserType(product.getProductCategoryId(), user.getUserType())
						.stream()
						.filter(promotion -> (promotion.getPromotionTotal() == null ? 9999L
								: promotion.getPromotionTotal()) > (userPromotionRepository
										.findByUserIdAndPromotionId(user.getId(), promotion.getId())).size())
						.map(promote -> new PromotionDto(promote)).collect(Collectors.toList());
				for (PromotionDto promotionDto : listPromotion) {
					promotionDto.setBannerPath("\\opt\\application-data\\upload\\image-promotion-temp\\1111.jpg");
					ProductCategory ProductCategory = productCategoryRepository
							.getDataProductCategoryById(product.getProductCategoryId(), 1L);
					promotionDto.setProductCateName(ProductCategory.getName());
				}
				listData.addAll(listPromotion);
			});
		}
		List<PromotionDto> listvoucher = voucherRepository.findByUserId(user.getId()).stream()
				.map(voucher -> new PromotionDto(voucher)).collect(Collectors.toList());
		for (PromotionDto promotionDto : listvoucher) {
			promotionDto.setBannerPath("\\opt\\application-data\\upload\\image-promotion-temp\\1111.jpg");
		}
		listData.addAll(listvoucher);
		response.setListPromotion(listData);
		return response;
	}

	@Override
	@SneakyThrows
	public PaymentOrderDetailResponse getOrderDetail(String orderId, String orderCode, User user) {
		PaymentOrderDetailResponse response = new PaymentOrderDetailResponse();
		List<OrderPaymentDto> orderDetails = new ArrayList<OrderPaymentDto>();
		if (StringUtils.isNotBlank(orderCode)) {
			OrderProduct orderProduct = orderProductRepository.findByCodeOrders(orderCode)
					.orElseThrow(() -> new AppException(MessageResult.GRD012_ORDER));
			response.setOrderId(orderProduct.getUuidId());
			OrderPaymentDto dto = new OrderPaymentDto();
			this.convertEntityToDtoOrder(orderProduct, dto);
			orderDetails.add(dto);
			response.setOrderDetail(orderDetails);
		} else {
			List<OrderProduct> orderProductList = orderProductRepository.findByUuidId(orderId);
			if (Objects.isNull(orderProductList) || orderProductList.isEmpty()) {
				throw new AppException(MessageResult.GRD012_ORDER);
			}
			response.setOrderId(orderId);
			orderDetails = orderProductList.stream().map(x -> {
				OrderPaymentDto dto = new OrderPaymentDto();
				convertEntityToDtoOrder(x, dto);
				return dto;
			}).collect(Collectors.toList());
			response.setOrderDetail(orderDetails);

		}
		return response;
	}

	private void convertEntityToDtoOrder(OrderProduct orderProduct, OrderPaymentDto dto) {
		dto.setDeliveryAddress(orderProduct.getDeliveryAddress());
		dto.setCodeOrders(orderProduct.getCodeOrders());
		dto.setStatus(OrderStatusEnum.getByValue(orderProduct.getStatus()).getName());
		dto.setPartnerId(orderProduct.getPartnerId());
		User partner = userRepository.findById(orderProduct.getPartnerId()).orElse(null);
		if (!Objects.isNull(partner)) {
			dto.setPartnerName(partner.getFullName());
		}
		dto.setTotalAmount(orderProduct.getTotalAmount());
		dto.setTotalDiscount(orderProduct.getTotalDiscount());
		dto.setPaymentMethod(orderProduct.getPaymentMethod());
		dto.setPaymentStatus(PaymentStatusEnum.getByValue(orderProduct.getPaymentStatus()).getName());
		dto.setCreatedDate(DateHelper.convertDateTime(orderProduct.getCreatedDate()));
		if (!Objects.isNull(orderProduct.getVoucherId())) {
			Voucher voucher = voucherRepository.findById(orderProduct.getVoucherId()).orElse(null);
			if (!Objects.isNull(voucher)) {
				dto.setPromotionName(voucher.getVoucherName());
			}
		}
		if (!Objects.isNull(orderProduct.getPromotionId())) {
			Promotion promotion = promotionRepository.findById(orderProduct.getPromotionId()).orElse(null);
			if (!Objects.isNull(promotion)) {
				dto.setPromotionName(promotion.getPromotionName());
			}
		}
		List<OrderProductDto> listProductDto = transactionProductOrderRepository
				.findByOrderProductCode(orderProduct.getCodeOrders()).stream()
				.filter(d -> productRepository.findById(d.getProductId()).isPresent()).map(x -> {
					OrderProductDto productDto = new OrderProductDto();
					Optional<Product> product = productRepository.findById(x.getProductId());
					String bannerProduct = productDocumentRepository
							.getdocPathProductByIdAndPosition(product.get().getId(), 1L);
					productDto.setProductId(x.getProductId());
					productDto.setProductName(product.get().getProductName());
					productDto.setBannerPath(bannerProduct);
					productDto.setPrice(x.getAmount());
					productDto.setTotalProduct(x.getTotalProduct());
					return productDto;
				}).collect(Collectors.toList());
		dto.setProduct(listProductDto);
//		List<OrderProductDto> listProductDto = orderProductHistoryRepository
//				.findByOrderProductCode(orderProduct.getCodeOrders()).stream()
//				.filter(d -> productRepository.findById(d.getProductId()).isPresent()).map(x -> {
//					OrderProductDto productDto = new OrderProductDto();
//					Optional<Product> product = productRepository.findById(x.getProductId());
//					String bannerProduct = productDocumentRepository
//							.getdocPathProductByIdAndPosition(product.get().getId(), 1L);
//					productDto.setProductId(x.getProductId());
//					productDto.setProductName(product.get().getProductName());
//					productDto.setBannerPath(bannerProduct);
//					productDto.setPrice(x.getAmount());
//					productDto.setTotalProduct(x.getTotalProduct());
//					return productDto;
//				}).collect(Collectors.toList());

		List<OrderProductHistoryDto> history = orderProductHistoryRepository
				.findByCodeOrdersOrderByCreatedDateAsc(orderProduct.getCodeOrders()).stream()
				.map(x -> new OrderProductHistoryDto(x)).collect(Collectors.toList());
		OrderProductHistoryDto historyDto = new OrderProductHistoryDto();
		historyDto.setCodeOrders(orderProduct.getCodeOrders());
		historyDto.setPaymentMethod(orderProduct.getPaymentMethod());
		historyDto.setPaymentStatus(orderProduct.getPaymentStatus());
		historyDto.setStatus(OrderStatusEnum.getByValue(orderProduct.getStatus()).getName());
		historyDto.setCreatedDate(DateHelper.convertDateTime(orderProduct.getModifiedDate()));
		historyDto.setCreatedBy(orderProduct.getCreatedBy());
		history.add(historyDto);
		dto.setHistory(history);

	}

	@Override
	public ListOrderResponse getListOrder(User user, String orderStatus) {
		ListOrderResponse response = new ListOrderResponse();
		try {
			// List<OrderProduct> orderProducts =
			// orderProductRepository.findByUserIdByCreatedDateDesc(user.getId());

			List<OrderStatusProducDto> listResponse = orderProductDao.getListOrderProductStatus(user.getId(),
					orderStatus);
			// List<OrderPaymentDto> listResponse = orderProducts.stream().sorted().map(x ->
			// {
//			OrderPaymentDto dto = new OrderPaymentDto();
//			convertEntityToDtoOrderList(x, dto);
//			dto.setOrderId(x.getUuidId());
//			return dto;
//		}).collect(Collectors.toList());

//			List<OrderPaymentDto> listResponse = new ArrayList<OrderPaymentDto>();
//			for (OrderProduct orderProduct : orderProducts) {
//				OrderPaymentDto dto = new OrderPaymentDto();
//				this.convertEntityToDtoOrderList(orderProduct, dto);
//				dto.setOrderId(orderProduct.getUuidId());
//				listResponse.add(dto);
//			}
//			

			for (OrderStatusProducDto dto : listResponse) {
//				OrderStatusProducDto orderStatusProducDto = new OrderStatusProducDto();
//				//this.convertEntityToDtoOrderList(orderProduct, dto);
//				String paymentStatusStr = dto.getPaymentStatus().
				dto.setPaymentName(PaymentStatusEnum.getByValue(dto.getPaymentStatus()).getName());
//				dto.setOrderId(orderProduct.getUuidId());
//				listResponse.add(dto);

				String bannerProduct = productDocumentRepository.getdocPathProductByIdAndPosition(dto.getProductId(),
						1L);
				dto.setBannerPath(bannerProduct);
				if (!Objects.isNull(dto.getVoucherId())) {
					Voucher voucher = voucherRepository.findById(dto.getVoucherId()).orElse(null);
					if (!Objects.isNull(voucher)) {
						dto.setPromotionName(voucher.getVoucherName());
					}
				}
				if (!Objects.isNull(dto.getPromotionId())) {
					Promotion promotion = promotionRepository.findById(dto.getPromotionId()).orElse(null);
					if (!Objects.isNull(promotion)) {
						dto.setPromotionName(promotion.getPromotionName());
					}
				}
			}

			response.setListOrder(listResponse);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}

	private void convertEntityToDtoOrderList(OrderProduct orderProduct, OrderPaymentDto dto) {
		dto.setNote(orderProduct.getNote());
		dto.setDeliveryAddress(orderProduct.getDeliveryAddress());
		dto.setCodeOrders(orderProduct.getCodeOrders());
		// dto.setStatus(OrderStatusEnum.getByValue(orderProduct.getStatus()).getName());
		dto.setPartnerId(orderProduct.getPartnerId());
		User partner = userRepository.findById(orderProduct.getPartnerId()).orElse(null);
		if (!Objects.isNull(partner)) {
			dto.setPartnerName(partner.getFullName());
		}
		dto.setTotalAmount(orderProduct.getTotalAmount());
		dto.setTotalDiscount(orderProduct.getTotalDiscount());
		dto.setPaymentMethod(orderProduct.getPaymentMethod());
		dto.setPaymentStatus(PaymentStatusEnum.getByValue(orderProduct.getPaymentStatus()).getName());
		dto.setCreatedDate(DateHelper.convertDateTime(orderProduct.getCreatedDate()));
		if (!Objects.isNull(orderProduct.getVoucherId())) {
			Voucher voucher = voucherRepository.findById(orderProduct.getVoucherId()).orElse(null);
			if (!Objects.isNull(voucher)) {
				dto.setPromotionName(voucher.getVoucherName());
			}
		}
		if (!Objects.isNull(orderProduct.getPromotionId())) {
			Promotion promotion = promotionRepository.findById(orderProduct.getPromotionId()).orElse(null);
			if (!Objects.isNull(promotion)) {
				dto.setPromotionName(promotion.getPromotionName());
			}
		}
	}

	@Override
	@Transactional
	public String cancelOrder(User user, String orderCode) {
		OrderProduct orderProduct = orderProductRepository.findByCodeOrders(orderCode)
				.orElseThrow(() -> new AppException(MessageResult.GRD012_ORDER));
		if (!StringUtils.equals(orderProduct.getStatus(), OrderStatusEnum.NEW.getValue())) {
			throw new AppException(MessageResult.GRD014_ORDER);
		}
		if (StringUtils.equals(SystemConfigUtil.WALLET, orderProduct.getPaymentMethod())) {
			user.setPoint(user.getPoint() + orderProduct.getTotalAmount());
			userRepository.save(user);
			PaymentHistory paymentHistory = new PaymentHistory();
			paymentHistory.setAmount(orderProduct.getTotalAmount());
			paymentHistory.setCreatedDate(new Date());
			paymentHistory.setUserId(user.getId());
			paymentHistory.setCodeOrders(orderCode);
			paymentHistory.setChargeType(3);
			paymentHistory.setCreatedBy(user.getUsername());
			paymentHistory.setDescription("Hoàn tiền đơn hàng: " + orderCode);
			paymentHistoryRepository.save(paymentHistory);
		}
		transactionProductOrderRepository.findByOrderProductCode(orderProduct.getCodeOrders()).stream().forEach(x -> {
			Product product = productRepository.findById(x.getProductId()).orElse(null);
			product.setTotalProduct(product.getTotalProduct() + x.getTotalProduct());
			productRepository.save(product);

		});

		OrderProductHistory history = new OrderProductHistory();
		history.setCodeOrders(orderProduct.getCodeOrders());
		history.setStatus(orderProduct.getStatus());
		history.setPaymentStatus(orderProduct.getPaymentStatus());
		history.setPaymentMethod(orderProduct.getPaymentMethod());
		history.setCreatedDate(orderProduct.getModifiedDate());
		history.setCreatedBy(user.getUsername());
		orderProductHistoryRepository.save(history);

		orderProduct.setModifiedDate(new Date());
		orderProduct.setStatus(OrderStatusEnum.FAIL.getValue());
		orderProductRepository.save(orderProduct);
		return orderCode;
	}

	@Override
	public ListSearchPromotionResponse getSearchPromotion(User user, String promotionCode) {

		ListSearchPromotionResponse response = new ListSearchPromotionResponse();
		List<PromotionSearchDto> listPromotion = new ArrayList<>();

		Voucher voucher = voucherRepository.findByUserIdAndVoucherCode(user.getId(), promotionCode);
		if (Objects.nonNull(voucher)) {
			PromotionSearchDto dto = new PromotionSearchDto();
			dto.setId(voucher.getId());
			dto.setPromotionCode(voucher.getVoucherCode());
			dto.setPromotionName(voucher.getVoucherName());
			dto.setPromotionType("VOUCHER");
			dto.setDescription(voucher.getDescription());
			listPromotion.add(dto);
		}
		Promotion promotion = promotionRepository.findByPromotionCode(promotionCode);
		if (Objects.nonNull(promotion)) {
			PromotionSearchDto dto = new PromotionSearchDto();
			dto.setId(promotion.getId());
			dto.setPromotionCode(promotion.getPromotionCode());
			dto.setPromotionName(promotion.getPromotionName());
			dto.setPromotionType("PROMOTION");
			dto.setDescription(promotion.getDescription());
			listPromotion.add(dto);
		}

		response.setListPromotion(listPromotion);
		return response;
	}
}
