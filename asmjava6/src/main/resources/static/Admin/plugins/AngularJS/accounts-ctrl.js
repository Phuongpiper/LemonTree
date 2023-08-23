var queryString = new URLSearchParams(window.location.search);
var jsonData = queryString.get('data');
var form = JSON.parse(decodeURIComponent(jsonData));
var fileNameLabel = document.querySelector('.custom-file-label');


app.controller("myCtrl2", function ($scope, $http, $window) {
	$scope.isSubcreateing = false;
	$scope.isSubediting = true;
	$scope.isSubreseting = false;
	$scope.form = {};
	$scope.items = [];
	$scope.currentPage = 1;
	$scope.itemsPerPage = 2;
	$scope.users = [];
	$scope.formErrors = {}; // Khởi tạo biến lưu trữ các thông báo lỗi
	$scope.user = {};
	$scope.reset = function () {
		// Đặt giá trị mặc định cho các biến hoặc xóa dữ liệu nếu cần thiết.
		$scope.form = {};
		$scope.users = [];
		$scope.usersBlock = [];
		$scope.load_all();
		$(".nav-tabs button:eq(0)").tab('show');
		$scope.isSubcreateing = false;
		$scope.isSubediting = true;
		$scope.isSubreseting = false;
	};

	$scope.load_all = function () {
		$http.get(host + "/ManagedAccount").then(resp => {
			$scope.form = resp.data;
			if ($scope.user != null) {
				console.log($scope.user);
				$scope.users = $scope.form.filter(user => user.id != $scope.userLogin.id);
				$scope.users = $scope.users.filter(user => user.active == true);
				console.log($scope.users);


				$scope.usersBlock = $scope.form.filter(user => user.active == false);
				console.log($scope.usersBlock);
			}
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.totalPages = function () {
		if (!$scope.users || !Array.isArray($scope.users)) {
			// Handle the case when $scope.users is not defined or not an array
			return 0;
		}

		return Math.ceil($scope.users.length / $scope.itemsPerPage);
	};


	$scope.setPage = function (page) {
		if (page >= 1 && page <= $scope.totalPages()) {
			$scope.currentPage = page;
		}
	};

	$scope.isCurrentPage = function (page) {
		return $scope.currentPage === page;
	};

	$scope.getCurrentPageUsers = function () {
		if (!$scope.users || !Array.isArray($scope.users)) {
			// Handle the case when $scope.users is not defined or not an array
			return [];
		}

		const startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
		const endIndex = startIndex + $scope.itemsPerPage;

		// Check if a search keyword is provided
		const searchKeyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';

		const filteredUsers = $scope.users.filter(user => {
			const fullName = user.fullName ? user.fullName.toLowerCase() : '';
			const userName = user.userName ? user.userName.toLowerCase() : '';
			const email = user.email ? user.email.toLowerCase() : '';
			return fullName.includes(searchKeyword) || userName.includes(searchKeyword) || email.includes(searchKeyword);
		});

		return filteredUsers.slice(startIndex, endIndex);
	};

	$scope.getCurrentPageUsersBlock = function () {
		if (!$scope.usersBlock || !Array.isArray($scope.usersBlock)) {
			// Handle the case when $scope.users is not defined or not an array
			return [];
		}

		// Check if a search keyword is provided
		const searchKeyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';

		const filteredUsers = $scope.usersBlock.filter(user => {
			const fullName = user.fullName ? user.fullName.toLowerCase() : '';
			const userName = user.userName ? user.userName.toLowerCase() : '';
			const email = user.email ? user.email.toLowerCase() : '';
			return fullName.includes(searchKeyword) || userName.includes(searchKeyword) || email.includes(searchKeyword);
		});

		return filteredUsers;
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

	$scope.edit = function (id) {
		var url = host + `/ManagedAccount/${id}`;
		$http.get(url).then(resp => {
			$scope.form = resp.data;
			if ($scope.form.admin == true) {
				alert("Không thể sửa tài khoản admin");
			} else {
				$(".nav-tabs button:eq(2)").tab('show');
				$scope.isSubcreateing = true;
				$scope.isSubediting = false;
				$scope.isSubreseting = false;
			}
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.create = function () {
		// Kiểm tra và gán thông báo lỗi vào biến formErrors
		$scope.formErrors = {};
		var item = angular.copy($scope.formInput);
		var url = host + '/ManagedAccount';
		item.isActive = true;
		console.log(item);
		// Gọi hàm validateProduct để kiểm tra và lấy thông báo lỗi
		var isValid = validateAccount(item);
		if (!isValid) {
			return; // Nếu có lỗi, không submit form
		}
		$http.post(url, item).then(resp => {
			$scope.reset();
			$scope.load_all();
			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.update = function () {
		$scope.formErrors = {};
		var item = angular.copy($scope.form);
		$(".nav-tabs button:eq(0)").tab('show');
		var isValid = validateAccount1(item);
		var url = host + `/ManagedAccount/${$scope.form.id}`;
		console.log($scope.form.id);
		if (!isValid) {
			console.error("Validation failed");
			return;
		}
		$http.put(url, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id === $scope.form.id);
			console.log(index);
			$scope.items[index] = resp.data;
			$scope.reset();
			$scope.load_all();
			$(".nav-tabs button:eq(0)").tab('show');

			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};

	$scope.updateInLog = function () {
		$scope.formErrors = {};
		var item = angular.copy($scope.userLogin);
		$(".nav-tabs button:eq(0)").tab('show');
		alert("Cập nhật thành công");
		var url = host + `/ManagedAccount/${$scope.userLogin.id}`;
		console.log($scope.userLogin.id);
		$http.put(url, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id === $scope.userLogin.id);

			// Kiểm tra và sử dụng dữ liệu cũ nếu password và fullName là rỗng
			if (!$scope.userLogin.password) {
				if ($scope.items[index] && $scope.items[index].password) {
					resp.data.password = $scope.items[index].password;
					alert(resp.data.password);
				}
			}

			if (!$scope.userLogin.fullName) {
				if ($scope.items[index] && $scope.items[index].fullName) {
					resp.data.fullName = $scope.items[index].fullName;
					alert(resp.data.fullName);
				}
			}
			alert(resp.data.password);
			console.log(resp.data.password);
			$scope.items[index] = resp.data;
			console.log($scope.items[index]);
			$scope.reset();
			$scope.load_all();
			$(".nav-tabs button:eq(0)").tab('show');

			console.log("thanh cong", resp);
		}).catch(error => {
			console.log(error);
		});
	};




	$scope.editRemove = function (id) {
		var url = host + `/ManagedAccount/${id}`;
		$http.get(url).then(resp => {
			$scope.form = resp.data;
			if ($scope.form.admin == true) {
				alert("Không thể xóa tài khoản admin");
			} else {
				if ($scope.form.active == false) {
					$scope.form.active = true;
				} else if ($scope.form.active == true) {
					$scope.form.active = false;
				}
				console.log($scope.form);
				$scope.update();
			}
		}).catch(error => {
			console.log(error);
		});
	};

	function validateAccount(Account) {
		// Đặt lại formErrors để xóa các thông báo lỗi cũ
		$scope.formErrors = {};

		// Kiểm tra trường họ và tên
		if (!Account.fullName) {
			$scope.formErrors.fullName = "Vui lòng nhập họ và tên.";
		}

		// Kiểm tra trường email
		if (!Account.email) {
			$scope.formErrors.email = "Vui lòng nhập địa chỉ email.";
		} else if ($scope.users.some(user => user.email === $scope.form.email)) {
			console.log($scope.form.email);

			// Kiểm tra tên người dùng trong vòng lặp for
			for (let i = 0; i < $scope.users.length; i++) {
				if ($scope.users[i].email === $scope.form.email) {
					console.log($scope.users[i].email + " đã tồn tại.");
					break; // Thoát khỏi vòng lặp khi tìm thấy tên người dùng trùng
				}
			}

			$scope.formErrors.email = "Email đã tồn tại.";
		}

		// Kiểm tra trường username
		if (!Account.userName) {
			$scope.formErrors.userName = "Vui lòng nhập tên người dùng.";
		} else if ($scope.users.some(user => user.userName === $scope.form.userName)) {
			console.log($scope.form.userName);

			// Kiểm tra tên người dùng trong vòng lặp for
			for (let i = 0; i < $scope.users.length; i++) {
				if ($scope.users[i].userName === $scope.form.userName) {
					console.log($scope.users[i].userName + " đã tồn tại.");
					break; // Thoát khỏi vòng lặp khi tìm thấy tên người dùng trùng
				}
			}

			$scope.formErrors.userName = "Tên người dùng đã tồn tại.";
		}

		// Kiểm tra trường password
		if (!Account.password) {
			$scope.formErrors.password = "Vui lòng nhập mật khẩu.";
		}

		// Nếu không có lỗi, trả về true để cho phép submit form
		return Object.keys($scope.formErrors).length === 0;
	};


	function validateAccount1(Account) {
		// Đặt lại formErrors để xóa các thông báo lỗi cũ
		$scope.formErrors = {};

		// Kiểm tra trường họ và tên
		if (!$scope.form.fullName) {
			$scope.formErrors.fullName = "Vui lòng nhập họ và tên.";
		}

		// Kiểm tra trường email
		if (!$scope.form.email) {
			$scope.formErrors.email = "Vui lòng nhập địa chỉ email.";
		}

		// Kiểm tra trường username
		if (!$scope.form.userName) {
			$scope.formErrors.userName = "Vui lòng nhập tên người dùng.";
		}

		// Kiểm tra trường password
		if (!$scope.form.password) {
			$scope.formErrors.password = "Vui lòng nhập mật khẩu.";
		}

		// Nếu không có lỗi, trả về true để cho phép submit form
		return Object.keys($scope.formErrors).length === 0;
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
			$scope.userLogin = resp.data;
			console.log($scope.userLogin);
		}).catch(error => {
			console.log(error);
		});
	};
	$scope.user();
	$scope.load_all();
});
