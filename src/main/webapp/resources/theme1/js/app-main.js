var webContextPath="${pageContext.request.contextPath}";
var hostLocation= window.location.protocol + "//" + window.location.hostname+":"+ window.location.port
var chrisApp=angular.module('chrisApp',[]);

chrisApp.filter('searchFor', function(){
    return function(arr, searchString){
        if(!searchString){
            return arr;
        }
        var result = [];
        searchString = searchString.toLowerCase();
        angular.forEach(arr, function(item){
            if(item.firstName.toLowerCase().indexOf(searchString) !== -1 ||
            		item.lastName.toLowerCase().indexOf(searchString) !== -1 ||
            		item.email.toLowerCase().indexOf(searchString) !== -1){
             result.push(item);
            }
        });
        return result;
    };
});