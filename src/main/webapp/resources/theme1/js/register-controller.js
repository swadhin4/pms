chrisApp.controller('registerController', 
		 ['$rootScope', '$scope', '$filter','userService',
function  ($rootScope, $scope , $filter,userService) {

		$scope.customer={};	 
		$scope.responseMessage={};
		
	    $scope.saveCustomer=function(){
    		var customer=this;
    		var newCustomer = angular.copy($scope.customer);
    		customer.registerUser=function(){
	    		userService.registerUser(newCustomer)
	    		.then(function(data) {
	    			 console.log(data);
	    			 if(data.statusCode == 200){
	    				 $scope.responseMessage={
	    						 info:data.message,
	    						 color:"green"
	    				 };
	    				// $scope.customer={};
	    				 $('#successMessageDiv').show();
	    				 $('#successMessageDiv').alert();
	    				 $('#infoMessageDiv').hide();
	    				 $('#resetRegisterFormBtn').click();
	    			 }else{
	    				 $scope.responseMessage={
    						 info:data.message,
    						 color:"red"
	    			 	 };
	    			 }
	            },
	            function(data) {
	            	console.log(data)
	            	 $('#successMessageDiv').hide();
	            	 $('#infoMessageDiv').alert();
	            	 $('#infoMessageDiv').show();
	            	 $scope.responseMessage={
						 info:data.message,
						 color:"red"
   			 	    };
	            }); 	
	    	}
    		customer.registerUser();
      };
      
      $scope.verifyCustomer=function(){
    	  
      }
      
      $scope.verifyCustomer=function(){
    	  
      }
      
      $scope.closeMessageWindow=function(){
    	  $('#successMessageDiv').hide();
    	  $('#infoMessageDiv').hide();
      }
			 
}]);

chrisApp.controller('passwordController', 
		 ['$rootScope', '$scope', '$filter','userService','passwordService',
function  ($rootScope, $scope , $filter,userService,passwordService) {

	  $scope.customer={};	 
   
	  $scope.verifyCustomer=function(passwordForm){
		  $scope.sendPasswordResetLink($scope.customer);
	  }
     
	  $scope.sendPasswordResetLink=function(customer){
		  passwordService.resetPassword(customer.email)
		  .then(function(data){
			  console.log(data)
			  if(data.statusCode == 200){
				  $('#messageWindow').show();
				  $('#successMessageDiv').show();
				  $('#infoMessageDiv').hide();
				  $('#successMessageDiv').alert();
				  $scope.successMsg=data.message;
			  }
		  },function(data){
			  console.log(data)
			  $('#messageWindow').show();
			  $('#infoMessageDiv').show();
			  $('#successMessageDiv').hide();
			  $('#infoMessageDiv').alert();
		  });
	  }
	  
	  $scope.resetCustomerNewPassword=function(passwordForm){
   	  
	  }
     
	  $scope.closeMessageWindow=function(){
	   	  $('#successMessageDiv').hide();
	   	  $('#infoMessageDiv').hide();
	   }
			 
}]);
