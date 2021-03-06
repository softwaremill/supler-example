"use strict";

angular.module('smlBootzooka.notifications')
    .factory('NotificationsService', function () {
        var messages = [];

        function checkPreconditions(title, content) {
            return angular.isDefined(content) || angular.isDefined(title);
        }

        function show(type) {
            return function (content, title) {
                if (!checkPreconditions(content, title)) {
                    return;
                }
                messages.push({
                    type: type || 'info',
                    title: title,
                    content: content
                });
            };
        }

        function dismiss(message) {
            var index = messages.indexOf(message);
            if (index >= 0) {
                messages.splice(index, 1);
            }
        }

        return {
            messages: messages,
            dismiss: dismiss,
            show: show,
            showInfo: show('info'),
            showError: show('danger'),
            showSuccess: show('success'),
            showWarning: show('warning')
        };
    });