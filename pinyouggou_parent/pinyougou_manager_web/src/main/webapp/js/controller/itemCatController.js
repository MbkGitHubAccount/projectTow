 //控制层 
app.controller('itemCatController' ,function($scope,$controller ,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		itemCatService.findOne(id).success(
			//$scope.parentId=parentId,
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update($scope.entity); //修改
		}else{
			$scope.entity.parentId=$scope.parentId;
			serviceObject=itemCatService.add($scope.entity );//增加
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询
					$scope.findByParentId($scope.parentId)
		        	//$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele($scope.selectIds).success(
			function(response){
				if(response.success){
					$scope.findByParentId($scope.parentId)//刷新列表
					$scope.selectIds={};
				}else{
					$scope.selectIds={};
					alert(response.message);
				}
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //查询下级分类列表

	$scope.parentId=0;
	$scope.findByParentId=function (parentId) {
		$scope.parentId=parentId;
		itemCatService.findByParentId(parentId).success(function (response) {

            $scope.list=response;
        })
    }

	//面包屑控制导航 展示级别
	$scope.gread=1;//默认为 1 级
	//设置等级
	$scope.setGread=function (gread) {

		$scope.gread=gread;
	}


	/*$scope.entity_1={};
	$scope.entity_2={};*/

	//判断
	$scope.selectList=function (entity_p) {


		if ($scope.gread==1){
			$scope.entity_1=null;
			$scope.entity_2=null;

		}
		if ($scope.gread==2){
			$scope.entity_1=entity_p;
			$scope.entity_2=null;

		}
		if ($scope.gread==3){

			$scope.entity_2=entity_p;

		}
		$scope.findByParentId(entity_p.id);
	}

		//新增魔板查询
//查询新增分类关联的模板列表数据
	$scope.selectTemplateList=function () {
		typeTemplateService.findAll().success(function (response) {
			$scope.templateList=response;
		})
	}

});	
