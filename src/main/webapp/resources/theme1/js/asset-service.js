

chrisApp.factory('assetCreationService',  ['$http', '$q',function ($http, $q) {
 		var AssetCreationService = {
 			assetFinalObject:{},
 			createAssetObject:createAssetObject,
 			saveAssetObject:saveAssetObject
        };
 		return AssetCreationService;
		
 		function createAssetObject(finalObject){
 				assetObject={
 					
 				}
 				AssetCreationService.assetFinalObject=angular.copy(assetObject);
 				return SiteCreationService.assetFinalObject;
 		}
 		
 	
		
}]);

chrisApp.factory('assetService',  ['$http', '$q',function ($http, $q) {
		var AssetService = {
				retrieveAssetCategories:retrieveAssetCategories,
				getAssetLocations:getAssetLocations,
				saveAssetObject:saveAssetObject,
				findAllAssets:findAllAssets,
				getAssetBySite:getAssetBySite
		};
		return AssetService;
		// implementation
 	    function findAllAssets() {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/list")
 	            .success(function(data) {
 	            	console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
		
 	    
 	// implementation
 	    function getAssetBySite(siteId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/site/list/"+siteId)
 	            .success(function(data) {
 	            	console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
	
		// implementation
 	    function saveAssetObject(assetObject) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/asset/create",assetObject)
 	            .success(function(data) {
 	            	console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
		
	// implementation
	    function retrieveAssetCategories() {
	        var def = $q.defer();
	        $http.get(hostLocation+"/asset/categories")
	            .success(function(data) {
	            	console.log(data)
	                def.resolve(data);
	            })
	            .error(function(data) {
	            	console.log(data)
	                def.reject(data);
	            });
	        return def.promise;
	    }
	    
	 // implementation
	    function getAssetLocations() {
	        var def = $q.defer();
	        $http.get(hostLocation+"/asset/locations")
	            .success(function(data) {
	            	console.log(data)
	                def.resolve(data);
	            })
	            .error(function(data) {
	            	console.log(data)
	                def.reject(data);
	            });
	        return def.promise;
	    }
	
}]);

