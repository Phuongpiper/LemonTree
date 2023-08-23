
var queryString = new URLSearchParams(window.location.search);
var jsonData = queryString.get('data');
var form = JSON.parse(decodeURIComponent(jsonData));
var fileNameLabel = document.querySelector('.custom-file-label');

app.controller("myCtrl3", function ($scope, $http, $window) {
	$scope.form = {};
	$scope.items = [];
	$scope.discounts = []; // Thay đổi tên biến 'users' thành 'discounts'
	$scope.currentPage = 1;
	$scope.itemsPerPage = 2;
	$scope.isSubmitting = false;
	$scope.isSubediting = true;
	$scope.formErrors = {}; // Khởi tạo biến lưu trữ các thông báo lỗi

	$scope.reset = function () {
		// Đặt giá trị mặc định cho các biến hoặc xóa dữ liệu nếu cần thiết.
		$scope.form = {};
		$scope.discounts = []; // Thay đổi tên biến 'users' thành 'discounts'
		$scope.items = [];
		$scope.load_all();
	};

	$scope.load_all = function () {
		$http.get(host + "/ManagedDiscount").then(resp => {
			$scope.discounts = resp.data; // Thay đổi tên biến 'users' thành 'discounts'
			if (form != null) {
				$scope.form = form;
				$scope.isSubmitting = true;
				$scope.isSubediting = false;
				// Gộp startDate và endDate thành dateRange bao gồm cả giờ
				const formattedStartDate = $scope.form.startDate;
				const formattedEndDate = $scope.form.endDate;
				$scope.dateRange = `${formattedStartDate} - ${formattedEndDate}`;
				alert($scope.dateRange);
			}
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.load_all_product = function () {
		$http.get(host + "/ManagedProduct").then(resp => {
			$scope.items = resp.data;
			console.log($scope.items);
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.totalPages = function () {
		if (!$scope.discounts || !Array.isArray($scope.discounts)) { // Thay đổi tên biến 'users' thành 'discounts'
			// Handle the case when $scope.discounts is not defined or not an array // Thay đổi tên biến 'users' thành 'discounts'
			return 0;
		}

		return Math.ceil($scope.discounts.length / $scope.itemsPerPage); // Thay đổi tên biến 'users' thành 'discounts'
	};

	$scope.setPage = function (page) {
		if (page >= 1 && page <= $scope.totalPages()) {
			$scope.currentPage = page;
		}
	};

	$scope.isCurrentPage = function (page) {
		return $scope.currentPage === page;
	};

	$scope.getCurrentPageDiscounts = function () {
		if (!$scope.discounts || !Array.isArray($scope.discounts)) {
			// Handle the case when $scope.discounts is not defined or not an array
			return [];
		}

		const startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
		const endIndex = startIndex + $scope.itemsPerPage;

		// Check if a search keyword is provided
		const searchKeyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';

		const filteredDiscounts = $scope.discounts.filter(discount => {
			const productName = discount.product && discount.product.name ? discount.product.name.toLowerCase() : '';
			return productName.includes(searchKeyword);
		});

		return filteredDiscounts.slice(startIndex, endIndex);
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


	$scope.create = function () {
		// Kiểm tra và gán thông báo lỗi vào biến formErrors
		$scope.formErrors = {};
		var item = angular.copy($scope.form);
		var url = host + '/ManagedDiscount';

		// Gọi hàm validateDiscount để kiểm tra và lấy thông báo lỗi // Thay đổi tên hàm 'validateAccount' thành 'validateDiscount'
		var isValid = validateDiscount(item); // Thay đổi tên hàm 'validateAccount' thành 'validateDiscount'
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
	$scope.delete = function (id) {
		var url = host + '/ManagedDiscount/' + id;
		$http.delete(url).then(resp => {
			$scope.reset();
			$scope.load_all();
			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};





	function validateDiscount(discount) { // Thay đổi tên hàm 'validateAccount' thành 'validateDiscount'
		// Đặt lại formErrors để xóa các thông báo lỗi cũ
		$scope.formErrors = {};

		// Kiểm tra trường product_id
		if (!discount.product || !discount.product.id) { // Thay đổi tên biến 'form' thành 'discount'
			$scope.formErrors.product_id = "Vui lòng chọn sản phẩm.";
			console.log($scope.formErrors.product_id);
		}

		// Kiểm tra trường discountAmount
		if (!discount.discountAmount) { // Thay đổi tên biến 'form' thành 'discount'
			$scope.formErrors.discountAmount = "Vui lòng nhập số tiền giảm giá.";
			console.log($scope.formErrors.discountAmount);
		}

		// Kiểm tra trường startDate
		if (!discount.startDate) { // Thay đổi tên biến 'form' thành 'discount'
			$scope.formErrors.startDate = "Vui lòng chọn ngày bắt đầu.";
			console.log($scope.formErrors.startDate);
		}

		// Kiểm tra trường endDate
		if (!discount.endDate) { // Thay đổi tên biến 'form' thành 'discount'
			$scope.formErrors.endDate = "Vui lòng chọn ngày kết thúc.";
			console.log($scope.formErrors.endDate);
		}

		// Nếu không có lỗi, trả về true để cho phép submit form
		return Object.keys($scope.formErrors).length === 0;
	};
	$scope.updateDateRange = function (dateRange) {
		function convertDateFormat(inputDate) {
			const dateParts = inputDate.split('/');
			const month = parseInt(dateParts[0], 10);
			const day = parseInt(dateParts[1], 10);
			const year = parseInt(dateParts[2].split(' ')[0], 10);
			const time = dateParts[2].split(' ')[1];

			// Chuyển đổi ngày tháng năm sang chuỗi ISO 8601
			return `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}T${time}:00.000+00:00`;
		}

		if (dateRange) {
			const [startDate, endDate] = dateRange.split(' -');
			$scope.form.startDate = convertDateFormat(startDate.trim());
			$scope.form.endDate = convertDateFormat(endDate.trim());

		} else {
			$scope.form.startDate = '';
			$scope.form.endDate = '';
			$scope.dateRange = ''; // Đặt giá trị cho dateRange là rỗng nếu không có dateRange
		}
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
	$scope.user();
	$scope.load_all();
	$scope.load_all_product();
});
