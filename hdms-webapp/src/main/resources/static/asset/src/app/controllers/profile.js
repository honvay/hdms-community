app.controller('ProfileCtrl', ['$scope', '$http', function ($scope, $http) {
    //$scope.sortType = $scope.user.sortDesc === true ? 'desc' : 'asc';
    $scope.save = function () {
        //$scope.user.sortDesc = $scope.sortType == 'desc' ? true : false;
        var toast = $scope.toaster.wait("正在保存");
        $http.post("/user/updateProfile", {
            avatar: $scope.user.avatar,
            name: $scope.user.name,
            mode: $scope.user.mode,
            sortField: $scope.user.sortField,
            sortDesc: $scope.user.sortDesc
        }).success(function (result) {
            if (result.success) {
                $scope.userSettingForm.$setPristine();
                $scope.userSettingForm.$setUntouched();
                toast.doSuccess("个人设置已保存")
            } else {
                toast.doError("保存失败：" + result.message);
            }
        }).error(function (result, status) {
            toast.doError("保存失败：" + (result.message || status));
        });
    }

    $scope.selectAvatar = function () {
        angular.element("#avatarInput").click();
    }

    $scope.upload = function () {
        var files = angular.element("#avatarInput")[0].files;
        var fd = new FormData();
        fd.append("avatar", files[0]);
        var xhr = new XMLHttpRequest();
        xhr.onload = function (event) {
            $scope.uploading = false;
            if (xhr.status !== 200) {
                $scope.result = {
                    success: false,
                    message: "上传失败：请稍候再试：" + xhr.status
                }
            } else {
                var result = angular.fromJson(xhr.response);
                $scope.result = result;
                if (!result.success) {
                    $scope.message = "上传失败：" + result.message
                } else {
                    $scope.user.avatar = result.avatar;
                    var avatarImg = angular.element(".avatar-img");
                    avatarImg.attr("src", avatarImg.attr("src") + "?_dc=" + new Date().getTime());
                }
            }
            $scope.$apply();
        };
        xhr.onerror = function (response) {
            $scope.uploading = false;
            $scope.result = {
                success: false,
                message: "上传失败：请稍候再试：" + xhr.status
            }
        };
        xhr.open("POST", "/user/upload/avatar");
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest')
        xhr.setRequestHeader(crsf.headerName, crsf.token);
        $scope.uploading = true;
        xhr.send(fd);
    }
}]);