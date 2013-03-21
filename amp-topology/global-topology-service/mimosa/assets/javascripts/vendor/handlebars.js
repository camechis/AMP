// This is a Ghetto solution to David's MimosaJS relying on Handlebars to be AMD compat!
define(["vendor/handlebars.1.0.rc.2"], 
	function(){ 
		return Handlebars; 
	});