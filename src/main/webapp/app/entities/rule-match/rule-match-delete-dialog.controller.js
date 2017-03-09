(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleMatchDeleteController',RuleMatchDeleteController);

    RuleMatchDeleteController.$inject = ['$uibModalInstance', 'entity', 'RuleMatch'];

    function RuleMatchDeleteController($uibModalInstance, entity, RuleMatch) {
        var vm = this;

        vm.ruleMatch = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RuleMatch.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
