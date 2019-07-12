 //控制层 
app.controller('indexController' ,function($scope,$controller,contentService){
	
	$controller('baseController',{$scope:$scope});//继承

	$scope.contentList=[];


    //读取列表数据绑定到表单中  
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
			function(response){
				//[ null [[{},{},{}] ,[],[]]  根据分类Id 向数组中添加对象
				$scope.contentList[categoryId]=response;
			}			
		);
	}    
	


    
});	
