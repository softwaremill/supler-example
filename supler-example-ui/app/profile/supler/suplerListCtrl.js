"use strict";

angular.module('smlBootzooka.profile')
        .controller('SuplerListCtrl', function SuplerListCtrl($scope, $resource) {

            var self = this;

            $scope.formRest = $resource('rest/supler/personlist', null, null, {});

            $scope.form = new SuplerForm(
                    document.getElementById('person_form_container'),
                    {
                        custom_data_handler: $scope.handleData,
                        send_form_function: $scope.postForm
                    });

            $scope.formRest.get(function (data) {
                $scope.form.render(data);
            });

            $scope.postForm = function (formValue, renderResponseFn, sendErrorFn) {
                $scope.formRest.save(JSON.stringify(formValue), renderResponseFn, sendErrorFn);
            };

            $scope.handleData = function (data) {
                console.log(data);
                if (data.action == 'edit') {
                    console.log('edit');
                    console.log(data);
                }
                else if (data.action == 'delete') {
                    console.log('delete');
                    console.log(data);
                }
            };
        });