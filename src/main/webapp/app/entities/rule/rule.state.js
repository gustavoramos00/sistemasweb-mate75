(function() {
    'use strict';

    angular
        .module('sistemaswebappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rule', {
            parent: 'entity',
            url: '/rule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rules'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rule/rules.html',
                    controller: 'RuleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rule-detail', {
            parent: 'rule',
            url: '/rule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rule'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rule/rule-detail.html',
                    controller: 'RuleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Rule', function($stateParams, Rule) {
                    return Rule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rule-detail.edit', {
            parent: 'rule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule/rule-dialog.html',
                    controller: 'RuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rule', function(Rule) {
                            return Rule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rule.new', {
            parent: 'rule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule/rule-dialog.html',
                    controller: 'RuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rule: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rule', null, { reload: 'rule' });
                }, function() {
                    $state.go('rule');
                });
            }]
        })
        .state('rule.edit', {
            parent: 'rule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule/rule-dialog.html',
                    controller: 'RuleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rule', function(Rule) {
                            return Rule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rule', null, { reload: 'rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rule.delete', {
            parent: 'rule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rule/rule-delete-dialog.html',
                    controller: 'RuleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rule', function(Rule) {
                            return Rule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rule', null, { reload: 'rule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
