package com.example.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.config.VNPayService;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.entity.Order;
import com.example.jparepository.AccountRepository;
import com.example.jparepository.OrderRepository;
import com.example.service.CartService;
import com.example.service.OderDetailService;
import com.example.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OderController {
	@Autowired
	OrderService OderService;
	@Autowired
	AccountRepository accountService;
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OderDetailService detailService;

	@Autowired
	CartService cartService;

	@Autowired
	private VNPayService vnPayService;

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return OderService.create(orderData);

	}

	@GetMapping("/client/checkout")
	public String home() {
		return "checkout";
	}

	@RequestMapping("/client/ordersuccess2")
	public String ordersuccess2() {
		return "ordersuccess2";
	}

	@PostMapping("/client/submitOrder")
	public String submidOrder(@RequestParam("amount") int orderTotal, @RequestParam("orderInfo") String orderInfo,
			HttpServletRequest request, @CookieValue(value = "username", defaultValue = "0") String userIdCookie,
			@RequestParam("address") String address, @RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("country") String country, @RequestParam("district") String district,
			@RequestParam("wards") String wards, @RequestParam("paymentMethod") String paymentMenThod) {

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
		HttpSession session = request.getSession();
		Account AccountSession = (Account) session.getAttribute("AccountSession");
		List<Cart> carts = cartService.findByUserId(AccountSession.getId());
		// Lấy thông tin từ form để tạo đơn hàng và lưu vào cơ sở dữ liệu
		Order order = new Order();
		// Assuming you have a service to retrieve the Account object by ID
		Account user = accountService.findById(AccountSession.getId());
		if (user != null) {
			order.setUser(user);
		} else {
			// Handle the case where the user is not found
			// You might want to redirect to an error page or handle it in some other way
		}
		// Lấy các thông tin còn lại từ form
		// Lấy đối tượng Account từ cơ sở dữ
		order.setUser(user);
		order.setAdress(address);
		order.setPhoneNumber(phoneNumber);
		order.setOrderDate(new Date()); // Ngày hiện tại
		order.setTotalPrice(orderTotal);
		order.setCountry(country);
		order.setDistrict(district);
		order.setWards(wards);
		System.out.println(carts.size());
		for (Cart cart : carts) {
			cart.setActive(false);
		}
		if (paymentMenThod.equals("COD")) {
			order.setActive(true);
			OderService.save(order);
			session.setAttribute("orderId", order.getId());
			return "redirect:" + "ordersuccess2";
		} else {

			order.setActive(false);
			OderService.save(order);
			session.setAttribute("orderId", order.getId());
			return "redirect:" + vnpayUrl;
		}

	}

	@GetMapping("/vnpay-payment")
	public String getVnpayPayment(HttpServletRequest request, Model model) {
		int paymentStatus = vnPayService.orderReturn(request);
		HttpSession session = request.getSession();
		Account AccountSession = (Account) session.getAttribute("AccountSession");
		String orderInfo = AccountSession.getFullName();

		String paymentTimeString = request.getParameter("vnp_PayDate");
		String Txnref = request.getParameter("vnp_TxnRef");
		String totalPrice = request.getParameter("vnp_Amount");

		double totalAmount = Double.parseDouble(String.valueOf(Double.valueOf(totalPrice) / 100));
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("vi", "VN"));
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		DecimalFormat currencyFormatter = new DecimalFormat("###,###,### VND");

		String formattedTotalAmount = currencyFormatter.format(totalAmount);

		// Chuyển đổi chuỗi thời gian sang đối tượng LocalDateTime
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime paymentTime = LocalDateTime.parse(paymentTimeString, inputFormatter);

		// Định dạng thời gian sang "dd/MM/yyyy HH:mm:ss"
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String formattedPaymentTime = paymentTime.format(outputFormatter);

		Integer orderIdObj = (Integer) session.getAttribute("orderId");

		if (orderIdObj != null) {
			if (paymentStatus == 1) {
				int orderId = orderIdObj.intValue();
				Order order = OderService.findById(orderId); // Lấy đối tượng Order từ cơ sở dữ liệu
				if (order != null) {
					order.setOrdresCode(Txnref); // Gán giá trị Txnref cho trường ordresCode
					order.setActive(true); // Đặt trạng thái đơn hàng thành đã kích hoạt (hoặc "true" tùy theo logic của
											// bạn)
					OderService.save(order); // Lưu lại đối tượng Order đã chỉnh sửa
				} else {
					// Xử lý trường hợp không tìm thấy đơn hàng với ID cụ thể
					// (ví dụ: thông báo lỗi, redirect, ...)
				}
			}
		} else {
			// Xử lý trường hợp không tìm thấy orderId trong session
		}
		model.addAttribute("orderId", orderInfo);
		model.addAttribute("totalPrice", formattedTotalAmount);
		model.addAttribute("paymentTime", formattedPaymentTime);
		model.addAttribute("Txnref", Txnref);

		return paymentStatus == 1 ? "ordersuccess" : "orderfail";
	}

	@RequestMapping("/cartDetail")
	public String checkOutCart() {
		return "cartDetail";
	}
}
