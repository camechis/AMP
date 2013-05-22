define ['c/controllers'], (controllers) ->
    'use strict'

    controllers.controller 'example', ['$scope', ($scope) ->
        $scope.greeting = "hola"
    ]