app.factory('FileSystem', ['$http', '$q', function ($http, $q) {
    var service = {};

    service.isPrivate = function (file) {
        return file.accessiblity == 'private';
    }

    service.isDirectory = function (file) {
        if (!file) return false;
        return file.type == 'directory';
    }

    service.isRoot = function (file) {
        return !file.root || file.root == file.id;
    }

    service.download = function (file) {
        var d = $q.defer();
        /*if(angular.isArray(file)){
            if(file.length == 1){
                d.resolve('/fs/download?id=' + file[0].id);
            }
        }else{
            d.resolve('/fs/download?id=' + file.id);
        }*/
        var id = file.id;
        if (angular.isArray(file)) {
            id = Util.Array.copyProperty(file, "id").join(",");
        }
        $http.get("/fs/download", {
            params: {
                id: id
            }
        }).success(function () {
            d.resolve();
        });
        return d.promise;
    }

    /**
     * 创建文件夹
     */
    service.createDirectory = function (params) {
        var d = $q.defer();
        $http.post("/fs/createDirectory", params).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    /**
     * 加载文件夹
     * @param mount
     * @param id
     */
    service.load = function (mount, id) {
        var d = $q.defer();
        $http.get("/fs/directory/", {
            params: {
                id: id,
                mount: mount
            }
        }).success(function (result) {
            if (result.success) {
                d.resolve(result.data);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }


    /**
     * 添加收藏
     */
    service.addFavorite = function (file) {
        var d = $q.defer();
        $http.post("/favorite/add", {
            documentId: file.id
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    /***
     * 取消收藏
     * */
    service.removeFavorite = function (file) {
        var d = $q.defer();
        var id = file.id;
        if (angular.isArray(file)) {
            id = Util.Array.copyProperty(file, "id");
        } else {
            id = [id];
        }
        $http.post("/favorite/remove", {
            documentIds: id
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    service.setTag = function (file) {
        var d = $q.defer();
        $http.post("/fs/tag", {
            id: file.id,
            tags: file.tags
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    service.updateDescription = function (file) {
        var d = $q.defer();
        $http.post("/fs/updateDescription", {
            id: file.id,
            description: file.description
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }


    /**
     * 复制文件
     */
    service.copy = function (documentIds, target, mount) {
        var d = $q.defer();
        if (angular.isArray(documentIds)) {
            documentIds = Util.Array.copyProperty(documentIds, 'id');
        } else {
            documentIds = [documentIds.id];
        }
        $http.post("/fs/copy", {
            documentIds: documentIds,
            parent: target,
            mount: mount
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    /**
     * 移动文件
     */
    service.move = function (documentIds, parent, mount) {
        var d = $q.defer();
        if (angular.isArray(documentIds)) {
            documentIds = Util.Array.copyProperty(documentIds, 'id');
        } else {
            documentIds = [documentIds.id];
        }
        $http.post("/fs/move", {
            documentIds: documentIds,
            parent: parent,
            mount: mount,
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;
    }

    /**
     * 删除文件
     * @param {Object} file
     * @param {Object} callback
     */
    service.remove = function (file) {
        var d = $q.defer();

        var id = file.id
        if (angular.isArray(file)) {
            id = Util.Array.copyProperty(file, 'id');
        } else {
            id = [id];
        }

        $http.post("/fs/remove", {
            documentIds: id
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(data.message || status);
        });
        return d.promise;
    }

    /**
     * 锁定文件
     * @param {Object} file
     * @param {Object} callback
     */
    service.lock = function (file) {
        var d = $q.defer();

        $http.post("/fs/lock", {
            documentIds: [file.id]
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(data.message || status);
        });
        return d.promise;
    }

    /**
     * 锁定文件
     * @param {Object} file
     * @param {Object} callback
     */
    service.unlock = function (file) {
        var d = $q.defer();

        $http.post("/fs/unlock", {
            documentIds: [file.id]
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(data.message || status);
        });
        return d.promise;
    }
    //重命名
    service.rename = function (id, name) {
        var d = $q.defer();
        $http.post("/fs/rename", {
            id: id,
            name: name
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result.message);
            }
        }).error(function (data, status) {
            d.reject(status);
        });
        return d.promise;

    }

    service.verify = function (file) {
        var d = $q.defer();
        $http.post("/fs/upload/verify", {
            md5: file.md5,
            name: file.name,
            path: file.path,
            ext: file.ext,
            mount: file.mount,
            parent: file.parent,
            size: file.size,
            master: file.master
        }).success(function (result) {
            if (result.success) {
                d.resolve(result);
            } else {
                d.reject(result);
            }
        }).error(function (data, status) {
            d.reject(data);
        });
        return d.promise;
    }
    return service;
}]);