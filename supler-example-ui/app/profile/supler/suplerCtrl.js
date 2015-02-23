"use strict";

angular.module('smlBootzooka.profile')
        .controller('SuplerCtrl', function SuplerCtrl($scope, $resource, $stateParams) {

            var self = this;

            $scope.formRest = $resource('rest/supler/personform/:entityId', null, null, {});

            $scope.postForm = function (formValue, renderResponseFn, sendErrorFn) {
                $scope.formRest.save(JSON.stringify(formValue), renderResponseFn, sendErrorFn);
            };

            $scope.form = new SuplerForm(
                    document.getElementById('person_form_container'),
                    {
                        send_form_function: $scope.postForm,
                        after_render_function: function () {
                            $('[name = "dob"]').datepicker({
                                autoclose: true,
                                format: 'yyyy-mm-dd'
                            });
                        }
                    });

            $scope.formRest.get({entityId: $stateParams.id}, function (data) {
                $scope.form.render(data);
            });

        });