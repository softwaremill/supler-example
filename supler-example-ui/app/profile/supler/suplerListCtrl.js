"use strict";

angular.module('smlBootzooka.profile')
        .controller('SuplerListCtrl', function SuplerListCtrl($scope, $resource, $state) {

            var self = this;

            $scope.formRest = $resource('rest/supler/personlist', null, null, {});
            $scope.personRest = $resource('rest/supler/person/:id', null, null, {});

            $scope.postForm = function (formValue, renderResponseFn, sendErrorFn) {
                $scope.formRest.save(JSON.stringify(formValue), renderResponseFn, sendErrorFn);
            };

            $scope.handleData = function (data) {
                if (data.action == 'edit') {
                    $state.transitionTo('supler', {id: data.id});
                }
                else if (data.action == 'delete') {
                    $scope.personRest.delete({id: data.id}, function () {
                        $scope.loadForm();
                    })
                }
            };

            $scope.form = new Supler.Form(
                    document.getElementById('person_form_container'),
                    {
                        custom_data_handler: $scope.handleData,
                        send_form_function: $scope.postForm
                    });

            $scope.loadForm = function () {
                $scope.formRest.get(function (data) {
                    $scope.form.render(data);
                });
            };

            $scope.loadForm();
        });