app.service("fileUploadService",function ($http) {


    //上传文件的数据模型

    this.uploadFile=function(){
        var formData = new FormData();
        formData.append("file",file.files[0])
        return $http({
            url:'../upload/fileUpload.do',
            method:'post',
            data:formData,
            headers:{'Content-Type' : undefined},
            transformRequest:angular.identity
        });
    }

    
})