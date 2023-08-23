app.controller("revenue-ctrl", function ($scope, $http, $window) {
    $scope.items = [];
    $scope.form = {};
    $scope.initialize = function () {
        // load brands
        $scope.formatTotalPrice = function (totalPrice) {
            return numeral(totalPrice).format('0,0') + ' VND';
        };

        $scope.formatDate = function (year, month) {
            const date = moment(`${year}-${month}`, 'YYYY-M').format('MM YYYY');
            return date.charAt(0).toUpperCase() + date.slice(1);
        };

        $http.get("/rest/Report").then(resp => {
            $scope.items = resp.data;
            // Extract data for chart
            const labels = $scope.items.map(item => $scope.formatDate(item.year, item.month));
            const data = $scope.items.map(item => item.totalRevenue);
            // Tính tổng doanh thu
            $scope.totalRevenue = $scope.items.reduce((total, item) => total + item.totalRevenue, 0);
            // Get the canvas element
            const canvas = document.getElementById("revenue-chart");

            // Create the chart using Chart.js
            const ctx = canvas.getContext("2d");
            new Chart(ctx, {
                type: "bar",
                data: {
                    labels: labels,
                    datasets: [{
                        label: "Tổng doanh thu/tháng",
                        data: data,
                        backgroundColor: "rgba(75, 192, 192, 0.2)",
                        borderColor: "rgba(75, 192, 192, 1)",
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function (value, index, values) {
                                    return $scope.formatTotalPrice(value);
                                }
                            }
                        }
                    }
                }
            });
        });

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

    // Khởi đầu
    $scope.initialize();

})