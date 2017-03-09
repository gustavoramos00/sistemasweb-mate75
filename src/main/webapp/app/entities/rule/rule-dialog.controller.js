(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleDialogController', RuleDialogController);

    RuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Rule', 'Ontology', 'User'];

    function RuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Rule, Ontology, User) {
        var vm = this;

        vm.rule = entity;
        vm.clear = clear;
        vm.save = save;
        vm.ontologies = Ontology.query({filter: 'rule-is-null'});
        $q.all([vm.rule.$promise, vm.ontologies.$promise]).then(function() {
            if (!vm.rule.ontology || !vm.rule.ontology.id) {
                return $q.reject();
            }
            return Ontology.get({id : vm.rule.ontology.id}).$promise;
        }).then(function(ontology) {
            vm.ontologies.push(ontology);
        });
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rule.id !== null) {
                Rule.update(vm.rule, onSaveSuccess, onSaveError);
            } else {
                Rule.save(vm.rule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sistemaswebappApp:ruleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
