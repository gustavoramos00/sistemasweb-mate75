(function() {
    'use strict';
    angular
        .module('sistemaswebappApp')
        .factory('RuleMatch', RuleMatch);

    RuleMatch.$inject = ['$resource'];

    function RuleMatch ($resource) {
        var resourceUrl =  'api/rule-matches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
