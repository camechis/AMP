define ['require', 'angular', 'app'], (require, angular) ->
    'use strict'
    require ['vendor/requirejs-domready/domReady!'], (document) ->
        angular.bootstrap document, ['app']
