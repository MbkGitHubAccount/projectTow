 //控制层 
app.controller('searchController' ,function($scope,$controller,$location,searchService){
	
	$controller('baseController',{$scope:$scope});//继承


	//初始化 搜索对象
	$scope.searchMap={
			keywords:"",// 搜索关键字
			category:"",//分类
			brand:"",//品牌
			spec:{},//规格
			price:"",//价格
			sortField:"",//排序字段
			sort:"ASC",//排序方式
			pageNo:1// 当前页码

	}

var searchKey= $location.search()["keywords"];
	if (searchKey!="undefined"){
		$scope.searchMap.keywords=searchKey;
	}



    //关键字查询
	$scope.search=function(){
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap=response;
				buildPageLabel();

			}			
		);
	}


	//过滤条件查询
	$scope.addFilterCondition=function (key,value) {
		//当页面传过来的字段是 分类 品牌 和价格的时候
		if (key=="category"|| key=="brand"|| key=="price") {
			$scope.searchMap[key]=value;
		}else {
			//item_spec_网络": "联通3G",
			$scope.searchMap.spec[key]=value;
		}
		//当条件改变的时候 得重现查询
		$scope.search();


	}

	//移除过滤条件
	$scope.removeSearchItem=function (key) {
		//移除过滤条件
		if (key=="category"|| key=="brand"|| key=="price") {
			$scope.searchMap[key]="";
		}else {
			//item_spec_网络": "联通3G",
		delete 	$scope.searchMap.spec[key];
		}
		//当条件改变的时候 得重现查询
		$scope.search();


	}


	//sortSearch('排序字段','排血方式')"
	$scope.sortSearch=function (sortField,sort) {
		$scope.searchMap.sortField=sortField;
		$scope.searchMap.sort=sort;
		$scope.search();
	}


	//构建分页工具条代码
	buildPageLabel=function(){
		$scope.pageLabel = [];// 新增分页栏属性
		var maxPageNo = $scope.resultMap.totalPages;// 得到最后页码

		// 定义属性,显示省略号
		$scope.firstDot = true;
		$scope.lastDot = true;

		var firstPage = 1;// 开始页码
		var lastPage = maxPageNo;// 截止页码

		if ($scope.resultMap.totalPages > 5) { // 如果总页数大于5页,显示部分页码
			if ($scope.resultMap.pageNo <= 3) {// 如果当前页小于等于3
				lastPage = 5; // 前5页
				// 前面没有省略号
				$scope.firstDot = false;

			} else if ($scope.searchMap.pageNo >= lastPage - 2) {// 如果当前页大于等于最大页码-2
				firstPage = maxPageNo - 4; // 后5页
				// 后面没有省略号
				$scope.lastDot = false;
			} else {// 显示当前页为中心的5页
				firstPage = $scope.searchMap.pageNo - 2;
				lastPage = $scope.searchMap.pageNo + 2;
			}
		} else {
			// 页码数小于5页  前后都没有省略号
			$scope.firstDot = false;
			$scope.lastDot = false;
		}
		// 循环产生页码标签
		for (var i = firstPage; i <= lastPage; i++) {
			$scope.pageLabel.push(i);
		}
	}


	//分页查询
	$scope.queryForPage=function(pageNo){
		$scope.searchMap.pageNo=pageNo;

		//执行查询操作
		$scope.search();

	}

	//分页页码显示逻辑分析：
	// 1,如果页面数不足5页,展示所有页号
	// 2,如果页码数大于5页
	// 1) 如果展示最前面的5页,后面必须有省略号.....
	// 2) 如果展示是后5页,前面必须有省略号
	// 3) 如果展示是中间5页,前后都有省略号

	// 定义函数,判断是否是第一页
	$scope.isTopPage = function() {
		if ($scope.searchMap.pageNo == 1) {
			return true;
		} else {
			return false;
		}
	}
	// 定义函数,判断是否最后一页
	$scope.isLastPage = function() {
		if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
			return true;
		} else {
			return false;
		}
	}







});	
