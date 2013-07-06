'use strict';

angular.module('admin').factory('baseUri', function() {
   return location.href.replace(/^https?:\/\/[^\/]+\//, '/').replace(/\/@.*/, '');
});

angular.module('admin').factory('AdminPages', function($resource, baseUri) {
  return $resource(baseUri + '/@/pages');
});

angular.module('admin').factory('Sessions', function($resource, baseUri) {
  return $resource(baseUri + '/sessions/:sessionKey', {sessionKey: 'current'});
});

angular.module('admin').controller('AdminController', function($scope, $http, baseUri, AdminPages, Sessions) {
    $scope.pages = AdminPages.query();

    $scope.session = Sessions.get()

    $scope.logout = function() {
        Sessions.delete(function() { location.reload() });
    }
});

angular.module('admin').config(function($httpProvider) {
    $httpProvider.responseInterceptors.push(function($q, baseUri) {
      return function(promise) {
          return promise.then(function(response) {
                return response;
              }, function(response) {
                if (response.status == 401 || response.status == 403) {
                    // onSecurityException should be loaded by /@/ui/js/securityHandling.js
                    if (window.onSecurityException) {
                        window.onSecurityException(baseUri, response.status, location);
                    } else {
                        // default implementation
                        window.location = baseUri + '/@/ui/login.html?backTo=' + location;
                    }
                }
                return $q.reject(response);
              });
      }
    });
});