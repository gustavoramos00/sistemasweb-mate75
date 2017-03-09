(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleDetailController', RuleDetailController);

    RuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rule', 'Ontology', 'User'];

    function RuleDetailController($scope, $rootScope, $stateParams, previousState, entity, Rule, Ontology, User) {
        var vm = this;

        vm.rule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sistemaswebappApp:ruleUpdate', function(event, result) {
            vm.rule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
