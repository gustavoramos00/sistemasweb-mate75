(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleMatchDetailController', RuleMatchDetailController);

    RuleMatchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RuleMatch', 'Rule'];

    function RuleMatchDetailController($scope, $rootScope, $stateParams, previousState, entity, RuleMatch, Rule) {
        var vm = this;

        vm.ruleMatch = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sistemaswebappApp:ruleMatchUpdate', function(event, result) {
            vm.ruleMatch = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
