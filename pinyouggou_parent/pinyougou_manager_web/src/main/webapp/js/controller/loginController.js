//定义控制器，需要先初始化模块
//参数一：控制器名称  参数二：控制器要处理的事情
//$scope angularjs内置对象 可以理解为全局的作用对象，可以应用在js和html代码 ，相当于js代码和html代码交换的桥梁
//$http内置服务，发起ajax请求
//$controller实现控制器继承的内置对象
app.controller("loginController",function ($scope,$controller,loginService) {

    //控制器继承 $controller
    //参数一：继承的父控制器名称 参数二：固定写法：共享$scope
    $controller("baseController",{$scope:$scope});

    //查询所有品牌列表
    $scope.getLoginName=function () {
        //发起get请求，success是请求成功后回调函数
        loginService.getLoginName().success(
            //function中的参数就是返回值
            function (response) {
                $scope.loginName=response.userName;
            }
        );
    };


});