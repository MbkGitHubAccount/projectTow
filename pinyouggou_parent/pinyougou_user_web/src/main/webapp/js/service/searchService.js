//服务层
app.service('searchService',function($http){

	//基于广告分类id查询广告列表
	this.search=function(searchMap){
		return $http.post('../search/searchItem.do',searchMap);
	}

});
