app.controller("shopping-favorite-ctrl", function ($scope, $http) {
	let accountId = null;
	// Hàm để lấy giá trị của một cookie
	$scope.items = [];
	$scope.form = {};

	function load_all() {
		$http.get("http://localhost:8080/rest/favorites")
			.then(response => {
				// Gán dữ liệu vào biến favoriteProducts để hiển thị
				$scope.favoriteProducts = response.data;
				console.log($scope.favoriteProducts);
				const userCookie = getCookie("id");
				console.log(userCookie);
				$http.get(`http://localhost:8080/rest/accounts/${userCookie}`)
					.then(accountResponse => {
						accountId = accountResponse.data.id;
						console.log(accountId);

						// Tạo mảng mới chứa các sản phẩm có userId trùng với accountId
						$scope.filteredFavoriteProducts = $scope.favoriteProducts.filter(product => product.account.id === accountId);
						console.log($scope.filteredFavoriteProducts);
						$scope.favoriteProducts = $scope.filteredFavoriteProducts;

					})
					.catch(error => {
						// Xử lý lỗi ở đây nếu cần
					});

			})
			.catch(error => {
				console.error(error);
			});
	}
	function getCookie(name) {
		const value = `; ${document.cookie}`;
		const parts = value.split(`; ${name}=`);

		if (parts.length === 2) {
			return parts.pop().split(';').shift();
		}

		return null;
	}
	var username = getCookie('username');
	$scope.fullName = getCookie('fullName').replaceAll("_", " ");
	// Hàm để gửi thông tin sản phẩm qua email
	$scope.shareProduct = function (productId, recipientEmail) {
		const userCookie = getCookie("id");

		if (userCookie) {
			const url = `http://localhost:8080/rest/products/send-email/${productId}/${userCookie}`;
			const data = {
				recipientEmail: recipientEmail
			};

			$http.post(url, data)
				.then(response => {
					alert("Thông tin sản phẩm đã được chia sẻ qua email!");
				})
				.catch(error => {
					console.error(error);
				});
		} else {
			console.log("Không tìm thấy cookie username.");
		}
	};
	// Hàm để mở modal gửi email
	$scope.openShareModal = function (productId) {
		$scope.sharedProductId = productId;
		$scope.recipientEmail = "huynqpc03028@fpt.edu.vn"; // Đặt lại email người nhận
		$('#shareModal').modal('show');
	};

	// Hàm gửi thông tin sản phẩm qua email
	$scope.sendEmail = function () {
		const productId = $scope.sharedProductId;
		const recipientEmail = $scope.recipientEmail;

		if (productId && recipientEmail) {
			$scope.shareProduct(productId, recipientEmail);
		} else {
			console.log("Vui lòng nhập đủ thông tin sản phẩm và email người nhận.");
		}
	};
	$scope.removeFromFavorites = function (productId) {
		const userCookie = getCookie("id");

		if (userCookie) {
			// Gọi API để tìm accountId dựa trên username
			$http.get(`http://localhost:8080/rest/accounts/${userCookie}`)
				.then(accountResponse => {
					const accountId = accountResponse.data.id;
					const url = `http://localhost:8080/rest/products/remove/favorite-product/${productId}/${accountId}`;

					$http.delete(url)
						.then(response => {
							alert("Bỏ thích thành công")
							console.log(response.data);
							// Sau khi xóa thành công, cập nhật lại danh sách sản phẩm yêu thích
							load_all();
						})
						.catch(error => {
							console.error(error);
						});
				})
				.catch(error => {
					console.error(error);
				});
		} else {
			console.log("Không tìm thấy cookie username.");
		}
	};

	$scope.cart = {
		items: [],
		async favorite(id) {
			const userCookie = getCookie("id");
			console.log(userCookie);

			if (userCookie) {
				try {
					const accountResponse = await $http.get(`http://localhost:8080/rest/accounts/${userCookie}`);
					const accountData = accountResponse.data;

					if (!Array.isArray(accountData)) {
						if (accountData.id) {
							accountId = accountData.id;
							const url = `http://localhost:8080/rest/products/add-to-favorite/${id}/${accountId}`;
							$scope.existingFavorite = $scope.favoriteProducts.filter(product => product.product.id === id);
							console.log($scope.existingFavorite);
							if ($scope.existingFavorite.length === 1) {
								alert("Sản phẩm đã có trong danh sách yêu thích!");
							}
							else {
								alert("Thêm sản phẩm yêu thích thành công!");
								const response = await $http.post(url);
								console.log(response.data);
								load_all();
							}

						} else {
							console.error("Không tìm thấy tài khoản hoặc tìm thấy nhiều tài khoản khớp.");
						}
					} else {
						console.error("Tìm thấy nhiều tài khoản khớp.");
					}
				} catch (error) {
					console.error(error);
				}
			} else {
				console.log("Không tìm thấy cookie username.");
			}
		},
		addToCart(ProductId) {
			alert("Thêm sản phẩm vào giỏ hàng thành công!");
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
					this.loadCartItems(username); // Load lại danh sách sản phẩm trong giỏ hàng sau khi cập nhật
				} else {
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
	load_all();
	// Load danh sách sản phẩm yêu thích ban đầu
});