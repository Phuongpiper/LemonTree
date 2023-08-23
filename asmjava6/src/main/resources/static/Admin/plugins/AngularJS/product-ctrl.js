const host = "http://localhost:8080";
var queryString = new URLSearchParams(window.location.search);
var jsonData = queryString.get('data');
var form = JSON.parse(decodeURIComponent(jsonData));
var fileNameLabel = document.querySelector('.custom-file-label');

app.controller("myCtrl1", function ($scope, $http, $window) {
	$scope.form = {};
	$scope.items = [];
	$scope.currentPage = 1;
	$scope.itemsPerPage = 2;
	$scope.isSubcreateing = false;
	$scope.isSubediting = true;
	$scope.isSubreseting = false;
	$scope.formErrors = {}; // Khởi tạo biến lưu trữ các thông báo lỗi
	$scope.mgsBlock = "";

	$scope.reset = function () {
		$scope.form = {};
		$scope.items = [];
		$scope.load_all();
		$scope.isSubcreateing = false;
		$scope.isSubediting = true;
		$scope.isSubreseting = false;
		$(".nav-tabs button:eq(0)").tab('show');

	};

	$scope.load_all = function () {
		$http.get(host + "/ManagedProduct").then(resp => {
			$scope.items = resp.data;

			if (form != null) {
				$scope.form = form;
				fileNameLabel.innerText = $scope.form.image;

			}
			if ($scope.items != null) {
				$scope.products = $scope.items.filter(user => user.active == true);
				console.log($scope.products);

				$scope.productsBlock = $scope.items.filter(user => user.active == false);
				console.log($scope.productsBlock);
			}
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.totalPages = function () {
		return Math.ceil($scope.products.length / $scope.itemsPerPage);
	};

	$scope.setPage = function (page) {
		if (page >= 1 && page <= $scope.totalPages()) {
			$scope.currentPage = page;
		}
	};

	$scope.isCurrentPage = function (page) {
		return $scope.currentPage === page;
	};

	$scope.getCurrentPageItems = function () {
		const startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
		const endIndex = startIndex + $scope.itemsPerPage;
		const filteredItems = $scope.products.filter(item => {
			const keyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';
			const itemName = item.name ? item.name.toLowerCase() : '';
			const itemDescription = item.description ? item.description.toLowerCase() : '';
			return itemName.includes(keyword) || itemDescription.includes(keyword);
		});
		return filteredItems.slice(startIndex, endIndex);
	};

	$scope.getCurrentPageItemsBlock = function () {
		if ($scope.productsBlock.length == 0) {
			$scope.mgsBlock = "Không có sản phẩm nào bị khóa";

		} else {
			$scope.mgsBlock = "";

		}
		const filteredItems = $scope.productsBlock.filter(item => {
			const keyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';
			const itemName = item.name ? item.name.toLowerCase() : '';
			const itemDescription = item.description ? item.description.toLowerCase() : '';
			return itemName.includes(keyword) || itemDescription.includes(keyword);
		});
		return filteredItems.slice();
	};

	$scope.getPagesRange = function () {
		const totalPages = $scope.totalPages();
		const range = [];
		const min = Math.max(1, $scope.currentPage - 2);
		const max = Math.min(totalPages, $scope.currentPage + 2);

		for (let i = min; i <= max; i++) {
			range.push(i);
		}

		return range;
	};

	$scope.load_all_categories = function () {
		$http.get(host + "/ManagedCategories").then(resp => {
			$scope.Categories = resp.data;
		}).catch(error => {
			console.log(error);
		})
	};

	$scope.edit = function (id) {
		var url = host + `/ManagedProduct/${id}`;
		$http.get(url).then(resp => {
			$scope.form = resp.data;
			$(".nav-tabs button:eq(1)").tab('show');
			$scope.isSubcreateing = true;
			$scope.isSubediting = false;
			$scope.isSubreseting = true;
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.editRemove = function (id) {
		var url = host + `/ManagedProduct/${id}`;
		$http.get(url).then(resp => {
			$scope.form = resp.data;
			if ($scope.form.active == true) {
				$scope.form.active = false;
			} else {
				$scope.form.active = true;
			}
			$scope.update();
		}).catch(error => {
			console.log(error);
		});
	};

	// Hàm create
	$scope.create = function () {
		// Kiểm tra và gán thông báo lỗi vào biến formErrors
		$scope.formErrors = {};
		var item = angular.copy($scope.form);
		var url = host + '/ManagedProduct';

		// Gọi hàm validateProduct để kiểm tra và lấy thông báo lỗi
		var isValid = validateProduct(item);
		if (!isValid) {
			return; // Nếu có lỗi, không submit form
		}

		alert("Thêm thành công");
		$http.post(url, item).then(resp => {
			$scope.reset();
			$scope.load_all();
			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.update = function () {
		var item = angular.copy($scope.form);
		var url = host + `/ManagedProduct/${$scope.form.id}`;
		console.log(url);
		if (!validateProduct(item)) {
			console.error("Validation failed");
			return;
		}
		$http.put(url, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id === $scope.form.id);
			console.log(index);
			$scope.items[index] = resp.data;
			$(".nav-tabs button:eq(0)").tab('show');
			$scope.reset();
			$scope.load_all();
			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};

	// Hàm validateProduct
	function validateProduct(product) {
		$scope.formErrors = {}; // Đặt lại biến formErrors để xóa các thông báo lỗi cũ

		if (!product.name || product.name.trim().length === 0 || product.name.length > 100) {
			$scope.formErrors.name = "Tên sản phẩm không hợp lệ (phải có từ 1 đến 100 ký tự)";
		}

		if (!product.image || product.image.trim().length === 0) {
			$scope.formErrors.image = "Ảnh sản phẩm không được để trống";
		}

		if (!product.price) {
			$scope.formErrors.price = "Giá sản phẩm không được để trống";
		} else if (isNaN(product.price)) {
			$scope.formErrors.price = "Giá sản phẩm phải là một số";
		} else if (product.price <= 0) {
			$scope.formErrors.price = "Giá sản phẩm phải là số dương";
		}

		// Add more validation rules if needed.

		// Kiểm tra xem có lỗi không
		return Object.keys($scope.formErrors).length === 0;
	}

	$scope.uploadedFile = function (files) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post(host + '/upload/statis', data, {
			transformRequest: angular.identity,
			headers: { "Content-Type": undefined }
		}).then(resp => {
			$scope.form.image = resp.data.name;
		}).catch(error => {
			console.log(error);
		});

		fileNameLabel.innerText = files.length > 0 ? files[0].name : 'Choose file';
	};
	function getCookieValue(cookieName) {
		const cookies = document.cookie.split(';');
		for (let i = 0; i < cookies.length; i++) {
			const cookie = cookies[i].trim();
			if (cookie.startsWith(cookieName + '=')) {
				return cookie.substring(cookieName.length + 1);
			}
		}
		return null;
	}

	const usernameCookie = getCookieValue('id');
	if (usernameCookie !== null) {
		console.log('Giá trị của cookie username là:', usernameCookie);
	} else {
		console.log('Cookie username không tồn tại.');
	}
	$scope.user = function () {
		$http.get("http://localhost:8080/ManagedAccountByUserName/" + usernameCookie).then(resp => {
			$scope.userLogin = resp.data; // Thay đổi tên biến 'users' thành 'discounts'
			console.log($scope.userLogin);
		}).catch(error => {
			console.log(error);
		});
	};
	$scope.limitDescription = function (description, limit) {
		if (description.length <= limit) {
			return description;
		} else {
			var truncatedDescription = description.substring(0, limit);
			var lastSpaceIndex = truncatedDescription.lastIndexOf(' ');
			if (lastSpaceIndex !== -1) {
				truncatedDescription = truncatedDescription.substring(0, lastSpaceIndex);
			}
			return truncatedDescription + '...';
		}
	};

	$scope.user();
	$scope.load_all();
	$scope.load_all_categories();
	$scope.reset();
});
