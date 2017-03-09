(function() {
    'use strict';
    angular
        .module('sistemaswebappApp')
        .factory('Ontology', Ontology);

    Ontology.$inject = ['$resource'];

    function Ontology ($resource) {
        var resourceUrl =  'api/ontologies/:id';

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
