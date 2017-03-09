(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ontology', {
            parent: 'entity',
            url: '/ontology',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ontologies'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ontology/ontologies.html',
                    controller: 'OntologyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('ontology-detail', {
            parent: 'ontology',
            url: '/ontology/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ontology'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ontology/ontology-detail.html',
                    controller: 'OntologyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Ontology', function($stateParams, Ontology) {
                    return Ontology.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ontology',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ontology-detail.edit', {
            parent: 'ontology-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ontology/ontology-dialog.html',
                    controller: 'OntologyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ontology', function(Ontology) {
                            return Ontology.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ontology.new', {
            parent: 'ontology',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ontology/ontology-dialog.html',
                    controller: 'OntologyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ontology', null, { reload: 'ontology' });
                }, function() {
                    $state.go('ontology');
                });
            }]
        })
        .state('ontology.edit', {
            parent: 'ontology',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ontology/ontology-dialog.html',
                    controller: 'OntologyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ontology', function(Ontology) {
                            return Ontology.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ontology', null, { reload: 'ontology' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ontology.delete', {
            parent: 'ontology',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ontology/ontology-delete-dialog.html',
                    controller: 'OntologyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ontology', function(Ontology) {
                            return Ontology.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ontology', null, { reload: 'ontology' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
