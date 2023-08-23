var app = angular.module("myApp", ["ngRoute"]);

app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "/admin/ManagedProducts",
            controller: "myCtrl1"
        })
        .when("/account", {
            templateUrl: "/admin/ManagedAccount",
            controller: "myCtrl2"
        })
        .when("/discount", {
            templateUrl: "/admin/ManagedDiscount",
            controller: "myCtrl3"
        })
        .when("/categories", {
            templateUrl: "/admin/ManagedCategories",
            controller: "myCtrl4"
        })

        .when("/brands", {
            templateUrl: "/admin/ManagedBrands",
            controller: "brand-ctrl"
        })
        .when("/distributor", {
            templateUrl: "/admin/ManagedDistributors",
            controller: "distributor-ctrl"
        })
        .when("/orders", {
            templateUrl: "/admin/ManagedOrders", // Đường dẫn đến trang dashboard
            controller: "order-ctrl"
        })
        .when("/Revenue", {
            templateUrl: "/admin/ReportRevenue", // Đường dẫn đến trang dashboard
            controller: "revenue-ctrl"
        })
        .when("/ReportProduct", {
            templateUrl: "/admin/ReportProduct", // Đường dẫn đến trang dashboard
            controller: "reportProduct-ctrl"
        })
        .when("/ReportFavoriteProducts", {
            templateUrl: "/admin/ReportFavoriteProducts", // Đường dẫn đến trang dashboard
            controller: "reportFavorite-ctrl"
        })

        // ... Thêm các route khác tương tự ...
        .otherwise({
            redirectTo: "/"
        });
});