###
  Main configures requirejs and begins the application load

  The application startup flow is like this main -> app -> bootstrap
    main configures requirejs and requires (among other things)
###
requirejs.config
  #append timestamp to resource requests to prevent caching
  urlArgs: "b=#{(new Date()).getTime()}"

  #define shortcuts to various modules
  paths:
    c:"controllers",
    d:"directives"
    jquery: 'vendor/managed/jquery/jquery'
    angular: 'vendor/managed/angular/angular'
    i18n: 'vendor/managed/requirejs-i18n/i18n'
    domReady: 'vendor/managed/requirejs-domready/domReady'
    modernizr: 'vendor/managed/modernizr/modernizr'

  #Non-compliant AMD modules are explicitly defined here along with their dependencies
  shim:
    'modernizr':
      exports: 'Modernizr'
    'angular':
      deps: ['modernizr']
      exports: 'angular'


#Begin loading the application.
#Also wire routes here since providers aren't available once the injector is created
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