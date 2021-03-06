 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=userService.update($scope.entity); //修改
		}else{
			serviceObject=userService.add($scope.entity);//增加
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


	//register  用户的注册
	$scope.register=function(){
		//判断两次输入的密码是否是一直的
		if ($scope.entity.password!=$scope.repassword){
			alert("两次输入的密码不一致！")
			return;
		}
		var serviceObject=userService.add($scope.entity,$scope.smsCode);//增加
		serviceObject.success(
			function(response){
				if(response.success){
					//注册成功！ 跳转到登录页面
				location.href="login.html";
				}else{
					//注册失败!
					alert(response.message);
				}
			}
		);

	}


	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
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
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//发送验证码
	//保存
	$scope.sendSms=function(){
		serviceObject=userService.sendSms($scope.entity.phone);//增加
		serviceObject.success(
			function(response){
				if(response.success){
					// 发送成功
				}else{
					alert(response.success);
				}
			}
		);
	}
    
});	
