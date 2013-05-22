requirejs.config
  urlArgs: "b=#{(new Date()).getTime()}"
  paths:
    c:"controllers",
    d:"directives"
    jquery: 'vendor/jquery/jquery'
    angular: 'vendor/angular/angular'
    i18n: 'vendor/requirejs-i18n/i18n'
  shim:
    'vendor/modernizr/modernizr':
      exports: 'Modernizr'
    'angular':
      deps: ['vendor/modernizr/modernizr']
      exports: 'angular'
  i18n:
    locale: 'fr-fr'

 requirejs ['app',
 'jquery'
 'bootstrap'
 'c/car'
 'angular'
 ], (app,$) ->
  rp = ($routeProvider) ->
    $routeProvider
      .when '/example',
        templateUrl: 'car-list.html'
        controller: 'car'
      .otherwise
        redirectTo: '/example'
  app.config ['$routeProvider', rp]