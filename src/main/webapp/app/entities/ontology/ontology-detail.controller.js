(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('OntologyDetailController', OntologyDetailController);

    OntologyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Ontology'];

    function OntologyDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Ontology) {
        var vm = this;

        vm.ontology = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sistemaswebappApp:ontologyUpdate', function(event, result) {
            vm.ontology = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
