(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('RuleController', RuleController);

    RuleController.$inject = ['Rule'];

    function RuleController(Rule) {

        var vm = this;

        vm.rules = [];

        loadAll();

        function loadAll() {
            Rule.query(function(result) {
                vm.rules = result;
                vm.searchQuery = null;
            });
        }
    }
})();
