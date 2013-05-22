define ['jquery', 'templates', 'angular'], ($, templates, angular) ->

  class ExampleView

    render: (element) ->
      $(element).append templates.example
      $(element).append templates['another-example']

  ExampleView