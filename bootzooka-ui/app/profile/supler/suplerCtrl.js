"use strict";

angular.module('smlBootzooka.profile')
    .controller('SuplerCtrl', function SuplerCtrl($scope, $resource) {

        var self = this;

        $scope.formDefinition = $resource('rest/supler/personform', null, null, {});

        $scope.form = new SuplerForm(
            document.getElementById('person_form_container', {})
        );

        $scope.formDefinition.get(function (data) {
            $scope.form.render(data);
        });
    });