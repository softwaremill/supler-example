"use strict";

angular.module('smlBootzooka.profile')
    .controller('SuplerCtrl', function SuplerCtrl($scope, $resource) {

        var self = this;

        $scope.formRest = $resource('rest/supler/personform', null, null, {});

        $scope.postForm = function(formValue, renderResponseFn, sendErrorFn) {
            $.ajax({
                url: 'rest/supler/personform',
                type: 'POST',
                data: JSON.stringify(formValue),
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: renderResponseFn,
                error: sendErrorFn
            });
            //$scope.formRest.post(JSON.stringify(formValue), renderResponseFn, sendErrorFn)
        };

        $scope.form = new SuplerForm(
            document.getElementById('person_form_container'),
            {
                send_form_function: $scope.postForm
            });

        $scope.formRest.get(function (data) {
            $scope.form.render(data);
        });

    });