 //控制层 
app.controller('contentController' ,function($scope,$controller   ,contentService,fileUploadService,contentCategoryService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		contentService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		contentService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		contentService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=contentService.update( $scope.entity ); //修改  
		}else{
			serviceObject=contentService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		contentService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		contentService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};



	//当上传图片的的时候 查询所有 广告的分类  让用户选择

	$scope.categoryList=[];
	$scope.selectCategoryList=function(){
		contentCategoryService.findAll().success(function (response) {

			$scope.categoryList=response;
		})

	}



	//上传图片
	$scope.imageEntity={};
	$scope.uploadFile=function () {
		fileUploadService.uploadFile().success(function (response) {

			if (response.success){
				$scope.entity.pic=response.message;
			}
			else {
				alert(response.message)
			}

		})/*.error(function () {//比如请求接口都没有的错误，上面的response.success后端还能返回
			alert("上传时出错！")
		})*/

	}

    
});	
