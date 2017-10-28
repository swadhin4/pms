
chrisApp.controller('loginController', 
		 ['$rootScope', '$scope', '$filter', '$location','userService','serviceProviderService',
	function ($rootScope, $scope , $filter,$location,userService,serviceProviderService) {
		$scope.user={};	 
		 var loggedInuser =null;
	    $scope.validateUser=function(){
	    		var appuser=this;
	    	    appuser.validatedUser={};
	    	    $scope.loader1=true;
		    	appuser.validateUser=function(){
		    		userService.validateUser( $scope.user.email, $scope.user.password)
		    		.then(function(data) {
		    			 $scope.savedUser = angular.copy(data);
		    			 $scope.savedUser.password="";
		    			 console.log(data);
		    			 $scope.user ={};
		    			 loggedInuser = $.jStorage.set('loggedInUser', $scope.savedUser);
		    			 $location.path('/userhome');
		            },
		            function(data) {
		               console.log(data)
		            }); 	
		    	}
		    appuser.validateUser();
	    };
	    
	    $scope.validateExternalSP=function(){
	    	console.log($scope.sp);
	    	$scope.validateSPLogin($scope.sp);
	    }
	    
	    $scope.validateSPLogin=function(sp){
	    	serviceProvider.validateLogin(sp)
	    	.then(function(data){
	    		console.log(data)
	    	},function(data){
	    		console.log(data)
	    	});
	    }
	    
}]);

