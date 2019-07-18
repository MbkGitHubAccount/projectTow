//定义基础controller
app.controller("baseController",function ($scope) {

    //定义分页对象，分页配置
    $scope.paginationConf = {
        currentPage:1,  				//当前页
        totalItems:10,					//总记录数
        itemsPerPage:10,				//每页记录数
        perPageOptions:[10,20,30,40,50], //分页选项，下拉选择一页多少条记录
        onChange:function(){			//页面变更后触发的方法
            $scope.reloadList();		//启动就会调用分页组件
        }
    };

    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }



    //更新复选框勾选状态，往品牌id数组中添加值
    //选中的id数组
    $scope.selectIds=[];
    $scope.updateSelection=function ($event,id) {

        //判断复选框选中状态 $event.target 事件源对象此时就是复选框对象
        if($event.target.checked){
            //勾选
            $scope.selectIds.push(id);
        }else{
            //取消勾选
            //获取元素索引
            var index = $scope.selectIds.indexOf(id);
            //参数一：移除位置的元素的索引值  参数二：从该位置移除几个元素
            $scope.selectIds.splice(index,1);
        }
    }
    //解析json 格式字符串
    $scope.parseJsonString=function (JsonString,key) {

        var JsonArr = JSON.parse(JsonString);
        var value="";
        for (i=0;i<JsonArr.length;i++){

            if (i==0){
                value+=JsonArr[i][key];
            }
         value+=","+JsonArr[i][key];

        }
        return value;
        
    }







});