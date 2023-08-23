const app = angular.module('shopping-cart-app', ['ngCookies']);

app.filter("vnCurrency", function () {
	return function (input) {
		if (isNaN(input)) return input;
		return currency(input, { symbol: "₫", separator: ",", precision: 0 }).format();
	};
});
app.controller("shopping-cart-ctrl", ['$scope', '$http', '$cookies','$window', function ($scope, $http, $cookies, $window) {
	// Định nghĩa biến username và gán giá trị cho nó
	$scope.items = [];
	$scope.form = {};
	var username = $cookies.get('username');
	$scope.fullName = $cookies.get('fullName').replaceAll("_", " ");


	if ($scope.fullName) {
		console.log("Họ và tên đã có trong cookie:", $scope.fullName);
	} else {
		console.log("Họ và tên không có trong cookie.");
	}

	console.log(username);
	$scope.initialize = function () {
		$http.get(`/rest/carts/userCart/${username}`).then(resp => {
			console.log('Dữ liệu từ API:', resp.data);
			const activeItems = resp.data.filter(item => item.active);
			console.log('Sản phẩm có isActive = true:', activeItems);

			if (activeItems.length >= 0) {
				$scope.items = activeItems;
			} else {
				console.log('Không có sản phẩm nào có isActive = true.');
			}
		});
	};


	// Khởi đầu
	$scope.initialize();

	// Thêm  mới 
	$scope.cart = {
		items: [],
		addToCart(ProductId) {
			var item = this.items.find(item => item.product.id == ProductId);

			if (item) {
				item.quantity += 1;
				this.saveToDatabase(ProductId, username, item.quantity);
			} else {
				$http.get(`/rest/carts/product/${ProductId}`).then(resp => { // Thay đổi tham số thành ProductId
					this.items.push(resp.data);
					this.saveToDatabase(ProductId, username, 1);

				});
			}
		},
		saveToDatabase(ProductId, username, quantity) {
			// Thay thế bằng cách lấy username từ người dùng sau khi đăng nhập
			var url = `/rest/carts/add-to-cart/${ProductId}/${username}/${quantity}`;

			$http.post(url, {}).then(response => {
				if (response.data && response.data.message === "Thêm Thành Công") {
					$scope.alertSuccess("Thêm Thành Công");
					this.loadCartItems(username); // Load lại danh sách sản phẩm trong giỏ hàng sau khi cập nhật
				} else {
					$scope.alertSuccess("Thêm Thất Bại");
					// Xử lý phản hồi thất bại (nếu cần)
				}
			}).catch(error => {
				// Xử lý lỗi (nếu cần)
				console.error(error);
			});
		},
		loadCartItems(username) {
			var url = `/rest/carts/userCart/${username}`;
			$http.get(url).then(response => {
				this.items = response.data;
			}).catch(error => {
				// Xử lý lỗi (nếu cần)
				console.error(error);
			});
		},
		checkAndProceedToPayment() {
			if ($scope.items.length > 0) {
				$window.location.href = '/client/checkout';
			} else {
				// Cart is empty, show a message or perform desired action
				alert("Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
				return;
			}
		}
	
	};


	$scope.updateQty = function (item) {

		var url = `/rest/carts/update-quantity/${item.id}/${item.quantity}`;
		$http.put(url, {})
			.then(response => {
				console.log(response.data);
				// Load lại danh sách sản phẩm trong giỏ hàng sau khi cập nhật
				$scope.cart.loadCartItems(username);
			})
			.catch(error => {
				console.error(error);
			});
	};

	$scope.increaseQty = function (item) {
		item.quantity += 1;
		$scope.updateQty(item);
	};

	$scope.decreaseQty = function (item) {
		if (item.quantity > 1) {
			item.quantity -= 1;
			$scope.updateQty(item);
		}
	};
	$scope.getTotalQuantity = function () {
		let totalQuantity = 0;
		for (const item of $scope.items) {
			totalQuantity += item.quantity;
		}

		return totalQuantity;
	};

	$scope.getTotalPrice = function () {
		let totalPrice = 0;
		for (const item of $scope.items) {
			totalPrice += item.product.price * item.quantity;
		}
		return totalPrice;
	};
	$scope.removeItem = function (id) {
		$http.put(`/rest/carts/deleteCart/${id}`)
			.then(response => {
				// Xóa sản phẩm thành công, cập nhật lại danh sách sản phẩm trong giỏ hàng
				$scope.initialize(); // Gọi hàm loadCartItems để cập nhật danh sách giỏ hàng sau khi xóa
			})
			.catch(error => {
				console.error(error);
			});
	};
	$scope.removeAllItems = function() {
		var userId = $cookies.get('userId'); // Thay đổi tên biến cho phù hợp
		
		$http.put(`/rest/carts/removeAllItems/${userId}`)
			.then(response => {
				// Xử lý phản hồi thành công (nếu cần)
				
				$scope.initialize(); // Gọi lại hàm để cập nhật danh sách giỏ hàng sau khi xóa
			})
			.catch(error => {
				// Xử lý lỗi (nếu cần)
				console.error(error);
			});
	};
	
	
	$scope.alertSuccess = function (message) {
		Toastify({
			text: message,
			duration: 1000,
			newWindow: true,
			gravity: "top",
			position: "right",
			stopOnFocus: true,
			style: {
				background: "#34c240",
				color: "white",
			},
			onClick: function () { }
		}).showToast();
	};

	$scope.shipfee = function () {
		var ship;
		var header = {
			"token": "d6f64767-329b-11ee-af43-6ead57e9219a",
			"shop_id": "4421897"
		};
		var insurance_value = $scope.getTotalPrice();
		var body = {
			"service_id": 53320,
			"insurance_value": insurance_value,
			"coupon": null,
			"from_district_id": 1572,
			"to_district_id": 1574,
			"to_ward_code": null,
			"height": 15,
			"length": 15,
			"weight": 1000,
			"width": 15
		};
		$http.post('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', body, { headers: header })
			.then(response => {
				this.ship = response.data;
			})
			.catch(error => {
				console.error(error);
			});
	};
	/*	$http.get('/api/cities')
			.then(response => {
				$scope.cities = response.data;
			})
			.catch(error => {
				console.error(error);
			});
	
		// Lấy danh sách huyện
		$http.get('/api/distric	ts')
			.then(response => {
				$scope.districts = response.data;
			})
			.catch(error => {
				console.error(error);
			});*/
	$scope.submidOrder = function (total, fullName) {
		var requestData = {
			amount: total,
			orderInfo: fullName
		};
		console.log(requestData)
		$http.post('/rest/orders/client/submitOrder', requestData)
			.then(response => {
				// Xử lý phản hồi từ server (nếu cần)
			})
			.catch(error => {
				// Xử lý lỗi (nếu cần)
				console.error(error);
			});
	};
	$scope.test = function (total, fullName) {
		var requestData = {
			amount: total,
			orderInfo: fullName
		};
		console.log(requestData)
		$http.post('/rest/orders/client/submitOrder', requestData)
			.then(response => {
				// Xử lý phản hồi từ server (nếu cần)
			})
			.catch(error => {
				// Xử lý lỗi (nếu cần)
				console.error(error);
			});
	};
	$http.get('/rest/orders')
		.then(response => {
			$scope.districts = response.data;
		})
		.catch(error => {
			console.error("API Error:", error);
		});

	$scope.selectedDate = new Date();
	$scope.shipfee()
	$scope.cart.loadCartItems(username);

	
}]);
