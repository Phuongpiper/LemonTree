
app.controller("myCtrl4", function ($scope, $http) {
  $scope.form = {};
  $scope.categories = [];
  $scope.items = [];
  $scope.currentPage = 1;
  $scope.itemsPerPage = 2;
  $scope.isSubmitting = false;
  $scope.isSubediting = true;
  var form = null;
  $scope.reset = function () {
    $scope.form = {};
    $scope.categories = [];
  };

  $scope.load_all = function () {
    $http.get(host + "/ManagedCategories").then(resp => {
      $scope.categories = resp.data;
      console.log(form);
    }).catch(error => {
      console.log(error);
    })
  };

  $scope.totalPages = function () {
    if (!$scope.categories || !Array.isArray($scope.categories)) {
      return 0;
    }

    return Math.ceil($scope.categories.length / $scope.itemsPerPage);
  };

  $scope.setPage = function (page) {
    if (page >= 1 && page <= $scope.totalPages()) {
      $scope.currentPage = page;
    }
  };

  $scope.isCurrentPage = function (page) {
    return $scope.currentPage === page;
  };

  $scope.getCurrentPageCategories = function () {
    if (!$scope.categories || !Array.isArray($scope.categories)) {
      return [];
    }

    const startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
    const endIndex = startIndex + $scope.itemsPerPage;

    // Check if a search keyword is provided
    const searchKeyword = $scope.searchKeyword ? $scope.searchKeyword.toLowerCase() : '';

    const filteredCategories = $scope.categories.filter(category => {
      const categoryName = category.name ? category.name.toLowerCase() : '';
      return categoryName.includes(searchKeyword);
    });

    return filteredCategories.slice(startIndex, endIndex);
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
    console.log(item);
    var url = host + '/ManagedCategories';
    var isValid = validateCategory(item);
    if (!isValid) {
      return;
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

  $scope.edit = function (id) {
    var url = host + `/ManagedCategories/${id}`; // Thay 'ManagedProduct' bằng 'ManagedAccount'
    $http.get(url).then(resp => {
      form = resp.data;
      $scope.form = form;
      console.log(form);
      $scope.isSubmitting = true;
      $scope.isSubediting = false;

    }).catch(error => {
      console.log(error);
    });
  };

  $scope.update = function () {
    $scope.formErrors = {};
    var item = angular.copy($scope.form);
    var url = host + `/ManagedCategories/${$scope.form.id}`;
    var isValid = validateCategory(item);
    console.log($scope.form.id);

    if (!isValid) {
      console.error("Validation failed");
      return;
    }
    $http.put(url, item).then(resp => {
      var index = $scope.items.findIndex(p => p.id === $scope.form.id);
      console.log(index);
      $scope.items[index] = resp.data;
      $scope.load_all();
      $scope.reset();
      console.log("thanh cong", resp);
    }).catch(error => {
      console.log(error);
    });
  };

  function validateCategory(category) { // Thay đổi tên hàm 'validateDiscount' thành 'validateCategory'
    // Đặt lại formErrors để xóa các thông báo lỗi cũ
    $scope.formErrors = {};

    // Kiểm tra trường name
    if (!category.name) { // Thay đổi tên biến 'discount' thành 'category'
      $scope.formErrors.name = "Vui lòng nhập tên danh mục.";
      console.log($scope.formErrors.name);
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
      $scope.userLogin = resp.data; // Thay đổi tên biến 'users' thành 'discounts'
      console.log($scope.userLogin);
    }).catch(error => {
      console.log(error);
    });
  };
  $scope.user();
  $scope.load_all();
});
