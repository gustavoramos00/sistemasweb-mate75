(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rule-match', {
            parent: 'entity',
            url: '/rule-match',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RuleMatches'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rule-match/rule-matches.html',
                    controller: 'RuleMatchController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rule-match-detail', {
            parent: 'rule-match',
            url: '/rule-match/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RuleMatch'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rule-match/rule-match-detail.html',
                    controller: 'RuleMatchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RuleMatch', function($stateParams, RuleMatch) {
                    return RuleMatch.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rule-match',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rule-match-detail.edit', {
            parent: 'rule-match-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule-match/rule-match-dialog.html',
                    controller: 'RuleMatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RuleMatch', function(RuleMatch) {
                            return RuleMatch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rule-match.new', {
            parent: 'rule-match',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule-match/rule-match-dialog.html',
                    controller: 'RuleMatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rule-match', null, { reload: 'rule-match' });
                }, function() {
                    $state.go('rule-match');
                });
            }]
        })
        .state('rule-match.edit', {
            parent: 'rule-match',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule-match/rule-match-dialog.html',
                    controller: 'RuleMatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RuleMatch', function(RuleMatch) {
                            return RuleMatch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rule-match', null, { reload: 'rule-match' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rule-match.delete', {
            parent: 'rule-match',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule-match/rule-match-delete-dialog.html',
                    controller: 'RuleMatchDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RuleMatch', function(RuleMatch) {
                            return RuleMatch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rule-match', null, { reload: 'rule-match' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
