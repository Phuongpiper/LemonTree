package com.example.rest;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.EmailData;
import com.example.entity.Favorite;
import com.example.entity.Product;
import com.example.jparepository.AccountRepository;
import com.example.jparepository.FavoriteRepository;
import com.example.jparepository.ProductRepository;
import com.example.service.EmailService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	@Autowired
	ProductRepository daoProduct;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	FavoriteRepository favoriteRepository;

	@Autowired
	AccountRepository daoAccount;

	@GetMapping
	public List<Product> getAllProducts() {
		return daoProduct.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
		Product product = daoProduct.findById(id).orElse(null);
		if (product != null) {
			return new ResponseEntity<>(product, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		Product savedProduct = daoProduct.save(product);
		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
		Product existingProduct = daoProduct.findById(id).orElse(null);
		if (existingProduct != null) {
			existingProduct.setName(product.getName());
			existingProduct.setPrice(product.getPrice());
			Product updatedProduct = daoProduct.save(existingProduct);
			return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") Integer id) {
		Product product = daoProduct.findById(id).orElse(null);
		if (product != null) {
			daoProduct.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add-to-favorite/{id}/{userName}")
	public ResponseEntity<String> addToFavorite(@PathVariable("id") Integer itemId,
			@PathVariable("userName") int userId) {

		Account account = daoAccount.findById(userId);

		if (account == null) {
			return ResponseEntity.badRequest().body("Không tìm thấy thông tin!");
		}

		// Thêm yêu thích mới
		Product product = daoProduct.findById(itemId).get();
		Favorite favorite = favoriteRepository.findByAccountAndProduct(account, product);
		if (favorite == null || account.getId() == favorite.getAccount().getId()) {
			System.out.println("Sản phẩm yêu thích chưa tồn tại !");
			Favorite favorite2 = new Favorite();
			favorite2.setProduct(product);
			favorite2.setAccount(account);
			favoriteRepository.save(favorite2);
			return ResponseEntity.ok("{\"message\": \"Đã thêm sản phẩm vào trang yêu thích!\"}");
		} else {
			System.out.println("Sản phẩm đã được thích");
			return ResponseEntity.ok("{\"message\": \"Đã thêm sản phẩm vào trang yêu thích 2!\"}");
		}

	}

	@GetMapping("/rest/products/favorites/{accountId}")
	public List<Favorite> getFavoriteProductsByAccountId(@PathVariable Integer accountId) {
		Account account = new Account();
		account.setId(accountId);

		return favoriteRepository.findByAccount(account);
	}

	@DeleteMapping("/remove/favorite-product/{productId}/{accountId}")
	public ResponseEntity<Favorite> removeFromFavorite(@PathVariable Integer productId,
			@PathVariable Integer accountId) {

		Favorite Favorite = favoriteRepository.findByAccountAndProduct(accountRepository.findById(accountId).get(),
				daoProduct.findById(productId).get());
		favoriteRepository.delete(Favorite);
		return ResponseEntity.ok(Favorite);
	}

	@PostMapping(value = "/send-email/{productId}/{userCookie}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String, String>> sendEmail(@PathVariable("productId") int id,
			@PathVariable("userCookie") String user, @RequestBody EmailData emailData) {
		Product data = daoProduct.findById(id);

		try {
			String productName = "Tên sản phẩm: " + data.getName();
			String productPrice = "Giá: " + data.getPrice();
			String productDescription = "Mô tả: " + data.getDescription();
			String productLink = "Link: " + "http://localhost:8080/client/detail/" + id;

			// Tạo nội dung email với mã hóa UTF-8 và định dạng HTML
			String emailContent = "<html><body>" + "<p>" + productName + "</p>" + "<p>" + productPrice + "</p>" + "<p>"
					+ productDescription + "</p>" + "<p>" + productLink + "</p>" +

					"</body></html>";

			emailService.sendEmail(emailData.getRecipientEmail(), "Chia sẻ sản phẩm", emailContent);

			HashMap<String, String> responseMap = new HashMap<>();
			responseMap.put("status", "success");
			responseMap.put("message", emailContent);

			return ResponseEntity.ok(responseMap);
		} catch (Exception e) {
			HashMap<String, String> errorMap = new HashMap<>();
			errorMap.put("status", "error");
			errorMap.put("message", "Lỗi khi gửi email: " + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
		}
	}

}
