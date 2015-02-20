"use strict";

angular.module('smlBootzooka.profile')
    .controller('SuplerListCtrl', function SuplerListCtrl($scope, $resource) {

        var self = this;

        $scope.formRest = $resource('rest/supler/personlist', null, null, {});

        $scope.form = new SuplerForm(
            document.getElementById('person_form_container'),
            {});

        $scope.formRest.get(function (data) {
            $scope.form.render(data);
        });

    });