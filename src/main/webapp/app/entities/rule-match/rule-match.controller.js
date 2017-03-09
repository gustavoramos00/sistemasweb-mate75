(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleMatchController', RuleMatchController);

    RuleMatchController.$inject = ['RuleMatch'];

    function RuleMatchController(RuleMatch) {

        var vm = this;

        vm.ruleMatches = [];

        loadAll();

        function loadAll() {
            RuleMatch.query(function(result) {
                vm.ruleMatches = result;
                vm.searchQuery = null;
            });
        }
    }
})();
