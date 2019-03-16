angular.module('hc.ui').directive('hcComboTree', ['$compile', function ($compile) {
    return {
        restrict: 'E',
        scope: {
            selectedItem: "=ngModel",
            options: "=",
            placeholder: "=",
            disabled: "=",
            readOnly: "=",
            multiSelect: "@",
            onChange: '&',
            textField: '@',
            treeConfig: "=",
            onBeforeLoad: '&'
        },
        controllerAs: "combotreeCtrl",
        link: function (scope, element, attrs) {
            var config = angular.fromJson(scope.treeConfig);
            scope.combotreeCtrl.readOnly = scope.readOnly;
            scope.combotreeCtrl.disabled = scope.disabled;
            var treeAttrs = "";
            for (var option in config) {
                treeAttrs += (option + "=\"" + config[option]) + "\" ";
            }
            var treeTpl = '<hc-tree ' + treeAttrs + ' multi-select="{{multiSelect}}" on-before-load="combotreeCtrl.onBeforeLoad(node,params);" on-selection-change="combotreeCtrl.onSelectionChange(selection);"></hc-tree>';

            element.find(".dropdown-menu").html(treeTpl);
            $compile(element.contents())(scope);

            function hideDropdown(e) {
                if (!element[0].contains(e.target)) {
                    scope.combotreeCtrl.pop = false;
                    scope.$digest();
                }
            }

            scope.combotreeCtrl.placeholder = scope.combotreeCtrl.placeholder || "请选择";
            var closeEvent = $(window).on('click', hideDropdown);
            scope.$on('$destroy', function () {
                $(window).unbind('click', hideDropdown);
            });
        },

        template: function (element, scope) {
            var tpl = "<div class=\"input-group\" ng-class=\"{'open':combotreeCtrl.pop}\">" +
                "    <input type=\"text\" placeholder='{{combotreeCtrl.placeholder}}' ng-readonly='{{combotreeCtrl.readOnly}}' class=\"form-control\" value=\"{{combotreeCtrl.getText()}}\" ng-focus=\"combotreeCtrl.pop = true\" ng-disabled=\"combotreeCtrl.disabled\" aria-label=\"...\">" +
                "    <div class=\"dropdown-menu\" style=\"max-height:300px;overflow:auto;min-width:200px;width:100%\"></div>" +
                "    <div class=\"input-group-btn\">" +
                "        <button type=\"button\" ng-disabled=\"combotreeCtrl.disabled\" ng-click=\"combotreeCtrl.pop=true\" class=\"btn btn-default dropdown-toggle\">" +
                "            <span class=\"caret\"></span>" +
                "        </button>" +
                "    </div>" +
                "</div>";
            /*var tpl = '<div class="hc-combotree dropdown" ng-class="{\'open\':combotreeCtrl.pop}">' +
                '<input class="form-control dropdown-toggle" type="text" ng-click="combotreeCtrl.showPop()"  ng-disabled="combotreeCtrl.disabled">' +
                '{{combotreeCtrl.getText()}}' +
                '<span class="caret"></span>' +
                '</input>' +

                "<div class=\"input-group-btn\">\n" +
                "        <button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">Action <span class=\"caret\"></span></button>\n" +
                '<div class="dropdown-menu" style="max-height:300px;overflow:auto;min-width:200px;width:100%" >' +
                '</div>' +
                "      </div>";
            '</div>';*/
            return tpl;
        },

        controller: ['$scope', '$element', function ($scope, $element) {

            this.onSelectionChange = function (selection) {
                this.select(selection);
            }

            this.onBeforeLoad = function (node, params) {
                if (angular.isFunction($scope.onBeforeLoad)) {
                    $scope.onBeforeLoad({
                        node: node,
                        params: params
                    });
                }
            }

            this.showPop = function () {
                if (this.disabled) {
                    return;
                }
                this.pop = true;
            }

            this.getText = function () {
                if ($scope.selectedItem) {
                    return $scope.selectedItem[$scope.textField || 'name'];
                } else {
                    return $scope.placeholder;
                }
            }

            this.select = function (item) {
                var fireEvent = false;
                if (item !== $scope.selectedItem) {
                    fireEvent = true;
                }
                $scope.selectedItem = item;
                if (fireEvent === true) {
                    $scope.onChange({
                        node: $scope.selectedItem
                    });
                }
                if ($scope.multiSelect !== 'true') {
                    this.pop = false;
                }
            }
        }]
    };
}]);