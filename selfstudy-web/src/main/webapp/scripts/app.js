//Define an angular module for our app
var app = angular.module('projectsApp', ['ngRoute']);

//Define Routing for app
//Uri /ListProjects -> template list_projects.html and Controller ListProjectsController
//Uri /ViewProject/:id -> template view_project.html and Controller ViewProjectController
app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/ListProjects', {
                templateUrl: 'templates/list_projects.html',
                controller: 'ListProjectsController'
            })
            .when('/ViewProject/:id', {
                templateUrl: 'templates/view_project.html',
                controller: 'ViewProjectController'
            })
            .otherwise({
                redirectTo: '/ListProjects'
            });
    }
]);

app.controller('ListProjectsController', ['$sce', '$http', '$scope',
    function($sce, $http, $scope) {
        var url = "http://localhost:8080/SelfStudyWebApp/rest/projects/jsonp";
        $http.jsonp($sce.trustAsResourceUrl(url), {jsonpCallbackParam: 'callback'})
            .then(function successCallback(response) {
                $scope.message = "Got " + response.data.length + " projects:";
                $scope.projects = response.data;
            }, function errorCallback(response) {
                $scope.message = "No projects found";
                $scope.projects = [];
            });
        $scope.pSorting = 'id';
    }
]);

app.controller('ViewProjectController', ['$routeParams' ,'$sce', '$http', '$scope',
    function($routeParams, $sce, $http, $scope) {
        var id = $routeParams.id;
        var url = "http://localhost:8080/SelfStudyWebApp/rest/projects/" + id + "/jsonp";
        $http.jsonp($sce.trustAsResourceUrl(url), {jsonpCallbackParam: 'callback'})
            .then(function successCallback(response) {
                var data = response.data;
                $scope.message = "";
                $scope.project = data;
            }, function errorCallback(response) {
                $scope.message = "No project found with id " + id;
                $scope.project = {};
            });
    }
]);


