(function () {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
