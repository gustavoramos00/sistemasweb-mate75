(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('OntologyDialogController', OntologyDialogController);

    OntologyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Ontology'];

    function OntologyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Ontology) {
        var vm = this;

        vm.ontology = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ontology.id !== null) {
                Ontology.update(vm.ontology, onSaveSuccess, onSaveError);
            } else {
                Ontology.save(vm.ontology, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sistemaswebappApp:ontologyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
