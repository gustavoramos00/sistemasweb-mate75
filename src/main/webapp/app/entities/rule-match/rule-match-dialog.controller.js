(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleMatchDialogController', RuleMatchDialogController);

    RuleMatchDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'RuleMatch', 'Rule'];

    function RuleMatchDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, RuleMatch, Rule) {
        var vm = this;

        vm.ruleMatch = entity;
        vm.clear = clear;
        vm.save = save;
        vm.rules = Rule.query({filter: 'rulematch-is-null'});
        $q.all([vm.ruleMatch.$promise, vm.rules.$promise]).then(function() {
            if (!vm.ruleMatch.rule || !vm.ruleMatch.rule.id) {
                return $q.reject();
            }
            return Rule.get({id : vm.ruleMatch.rule.id}).$promise;
        }).then(function(rule) {
            vm.rules.push(rule);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ruleMatch.id !== null) {
                RuleMatch.update(vm.ruleMatch, onSaveSuccess, onSaveError);
            } else {
                RuleMatch.save(vm.ruleMatch, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sistemaswebappApp:ruleMatchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
