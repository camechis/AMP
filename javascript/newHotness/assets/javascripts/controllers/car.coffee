define [
    'c/controllers'
    'i18n!nls/hello'
    ], (controllers, hello) ->
    'use strict'

    controllers.controller 'car', ['$scope', ($scope) ->
        $scope.hello = hello.hello
        $scope.cars = [
            {name: "Honda Civic", owner: "Drew"}
            {name: "BMW X5", owner: "Rich Drew"}
            {name: "Ford Pinto", owner: "Poor Drew"}
        ]
    ]