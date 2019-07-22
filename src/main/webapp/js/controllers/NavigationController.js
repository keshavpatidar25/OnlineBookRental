"use strict";
//Controller for Navigation
app.controller('NavigationController', [ '$scope', function($scope) {
	$scope.setActiveHeader = function(id) {
		for (var i = 1; i <= 4; i++) {
			var selectedNav = "nav" + i;
			if (id == selectedNav) {
				angular.element("#" + selectedNav).addClass("active");
			} else {
				angular.element("#" + selectedNav).removeClass("active");
			}
		}
	};
} ]);