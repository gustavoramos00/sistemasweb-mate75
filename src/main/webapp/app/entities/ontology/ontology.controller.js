(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .controller('OntologyController', OntologyController);

    OntologyController.$inject = ['DataUtils', 'Ontology'];

    function OntologyController(DataUtils, Ontology) {

        var vm = this;

        vm.ontologies = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Ontology.query(function(result) {
                vm.ontologies = result;
                vm.searchQuery = null;
            });
        }
    }
})();
