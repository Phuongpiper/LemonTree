app.controller("reportProduct-ctrl", function ($scope, $http, $window) {
    $scope.items = [];
    $scope.form = {};
    $scope.initialize = function () {
        // Load product data
        $http.get("/rest/Report/product").then(resp => {
            $scope.items = resp.data;

        });
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
    // Khởi đầu
    $scope.initialize();

})