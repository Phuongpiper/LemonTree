
app.controller("brand-ctrl", function ($scope, $http, $window) {
    $scope.itemsbr = [];
    $scope.formbr = {};
    $scope.lockbtnAdd = false;
    $scope.lockbtnDelete = true;
    $scope.lockbtnUpdate = true;
    $scope.initialize = function () {
        // load brands
        $http.get("/rest/brands").then(resp => {
            $scope.itemsbr = resp.data;
        });
    }

    // Khởi đầu
    $scope.initialize();

    // Xóa formbr
    $scope.resetbr = function () {
        $scope.formbr = {};
        $scope.lockbtnAdd = false;
        $scope.lockbtnDelete = true;
        $scope.lockbtnUpdate = true;
    }

    // Hiển thị lên formbr
    $scope.editbr = function (item) {
        $scope.formbr = angular.copy(item);
		$(".nav-tabs button:eq(1)").tab('show');
        $scope.lockbtnAdd = true;
        $scope.lockbtnDelete = false;
        $scope.lockbtnUpdate = false;
    }


    // Thêm  mới 
    $scope.createbr = function () {
        $scope.hassError = false;
        if ($scope.validate()) {
            $scope.hassError = false;
            var item = angular.copy($scope.formbr);
            $http.post('/rest/brands', item).then(resp => {
                $scope.itemsbr.push(resp.data);
                $scope.resetbr();
                alert("Thêm mới sản phẩm thành công!");
            }).catch(error => {
                alert("Lỗi thêm mới sản phẩm!");
                console.log("Error", error)
            })
        }
    }

    // Cập nhật 
    $scope.updatebr = function () {
        if ($scope.validate()) {
            var item = angular.copy($scope.formbr);
            $http.put(`/rest/brands/${item.id}`, item).then(resp => {
                var index = $scope.itemsbr.findIndex(
                    p => p.id == item.id);
                $scope.itemsbr[index] = item;
                alert("Cập nhật sản phẩm thành công!");
            })
                .catch(error => {
                    alert("Lỗi cập nhật sản phẩm!");
                    console.log("Error", error)
                })
        }
    }
    // Xóa 
    $scope.deletebr = function (item) {
        $http.delete(`/rest/brands/${item.id}`).then(resp => {
            var index = $scope.itemsbr.findIndex(
                p => p.id == item.id);
            $scope.itemsbr.splice(index, 1);
            $scope.resetbr();
            alert("Xóa sản phẩm thành công!");
        })
            .catch(error => {
                alert("Lỗi xóa sản phẩm!");
            })
    }
    $scope.pager = {
        page: 0,
        size: 5,
        get itemsbr() {
            var start = this.page * this.size;
            return $scope.itemsbr.slice(start, start + this.size);
        },

        get count() {
            return Math.ceil(1.0 * $scope.itemsbr.length / this.size);
        },

        first() {
            this.page = 0;
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.last();
            }
        },
        next() {
            this.page++;
            if (this.page >= this.count) {
                this.first();
            }
        },
        last() {
            this.page = this.count - 1;
        }
    }

    //validate
    $scope.brandMessages = {
        id: "ID is required",
        nameNull: "Tên nhãn hàng không được rỗng",
        nameNumber: "Tên nhãn hàng không được điền số",
        originNull: "Nguồn gốc không được rỗng",
        responsibleNameNull: "Tên người chịu trách nhiệm không được rỗng",
        // ...
    };

    $scope.validate = function () {
        $scope.message1 = "";
        $scope.message2 = "";
        $scope.message3 = "";
        $scope.checkV = false;
        if ($scope.formbr.name === undefined) {
            $scope.hassError = true;
            $scope.message1 = $scope.brandMessages.nameNull;
            $scope.checkV = true;
        }
        if (!isNaN($scope.formbr.name)) {
            $scope.hassError = true;
            $scope.message1 = $scope.brandMessages.nameNumber;
            $scope.checkV = true;
        }
        if ($scope.formbr.responsibleName === undefined) {
            $scope.hassError = true;
            $scope.message2 = $scope.brandMessages.responsibleNameNull;
            $scope.checkV = true;
        }
        if ($scope.formbr.origin === undefined) {
            $scope.hassError = true;
            $scope.message3 = $scope.brandMessages.originNull;
            $scope.checkV = true;
        }
        if ($scope.checkV === true) {
            return false;
        } else {
            return true;
        }

    }
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

})
