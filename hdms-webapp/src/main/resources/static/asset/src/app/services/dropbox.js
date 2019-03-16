app.factory('Dropbox', ['$rootScope', 'FileSystem', '$modal', function ($rootScope, FileSystem, $modal) {

    var service = {};

    service.init = function ($scope) {
        var dropbox = document.getElementById("dropbox");

        function stopDefault(e) {
            e.preventDefault();
            e.stopImmediatePropagation();
        };
        var mousedown = false;
        document.addEventListener('mousedown', function () {
            mousedown = true;
        });
        document.addEventListener('mouseup', function () {
            mousedown = false;
        });
        document.addEventListener("dragenter", function (e) {
            if (mousedown) {
                return;
            }
            dropbox.innerText = '文件拖放到此处上传'
            dropbox.style.display = 'block';
        }, false);

        dropbox.addEventListener("dragenter", function (e) {
            stopDefault(e);
        });
        dropbox.addEventListener("dragover", function (e) {
            stopDefault(e);
        });
        dropbox.addEventListener("dragleave", function (e) {
            stopDefault(e);
            dropbox.style.display = 'none';
        });
        dropbox.addEventListener("drop", function (e) {
            stopDefault(e);
            dropbox.style.display = 'none';
            var files = e.dataTransfer.files;
            if (files && files.length > 0) {
                //打开上传文件夹框
                var modalInstance = $modal.open({
                    templateUrl: 'asset/tpl/app/dropbox.html',
                    backdrop: 'static',
                    controller: 'DropboxCtrl',
                    resolve: {
                        data: function () {
                            return {
                                files: files
                            };
                        }
                    }
                });
                modalInstance.result.then(function (result) {
                    var files = result.files;
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        file.uploadContext = result.uploadContext;
                        $scope.webUploader.addFiles(file);
                    }
                    //delete $rootScope.uploadContext;
                }, function () {
                });
            }
            return;
        });
    }
    return service;
}]);