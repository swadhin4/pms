

chrisApp.factory('siteCreationService',  ['$http', '$q',function ($http, $q) {
 		var SiteCreationService = {
 			siteFinalObject:{},
 			createSiteObject:createSiteObject,
 			saveSiteObject:saveSiteObject
 			
        };
 		return SiteCreationService;
		
 		function createSiteObject(finalObject){
 				siteObject={
 					siteId:finalObject.siteData.siteId || null,
 					siteName:finalObject.siteData.siteName || null,
 					owner:finalObject.siteData.owner || null,
 					
 					electricityId:finalObject.siteData.electricityId || null,
 					country:finalObject.siteData.country || null,
 					operator : finalObject.siteData.operator || null,
 					district:finalObject.siteData.district || null,
 					area:finalObject.siteData.area || null,
 					
 					cluster:finalObject.siteData.cluster || null,
 					siteNumber1:parseInt(finalObject.siteData.siteNumber1) || null,
 					siteNumber2:parseInt(finalObject.siteData.siteNumber2) || null, 
 					fileInput:finalObject.uploadedFile || null,
 					fileExtension:finalObject.extension || null,
 					
 				//-------------- X -----------------------//
 					
 					contactName:finalObject.siteData.contactName || null,
 					email:finalObject.siteData.email || null,
 					longitude:finalObject.siteData.longitude || null,
 					latitude:finalObject.siteData.latitude || null,
 					primaryContact:finalObject.siteData.primaryContact || null,
 					secondaryContact:finalObject.siteData.secondaryContact || null,
 					address:finalObject.siteData.address || null,
 					
 				//-------------- X -----------------------//	
 					
 					siteLicense:finalObject.siteLicense || null,
 					siteOperation:finalObject.siteOperation || null,
 					siteDelivery:finalObject.siteDelivery || null,
 					siteSubmeter:finalObject.siteSubmeter || null
 				}
 				SiteCreationService.siteFinalObject=angular.copy(siteObject);
 				return SiteCreationService.siteFinalObject;
 		}
 		
 	// implementation
 	    function saveSiteObject(siteObject) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/site/create",siteObject)
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

