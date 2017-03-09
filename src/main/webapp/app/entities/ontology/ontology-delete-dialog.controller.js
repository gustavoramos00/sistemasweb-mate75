(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('OntologyDeleteController',OntologyDeleteController);

    OntologyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ontology'];

    function OntologyDeleteController($uibModalInstance, entity, Ontology) {
        var vm = this;

        vm.ontology = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ontology.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
