/**
 * Created by xyang on 2/22/17.
 * updated @ Nov 11 2020
 */
'use strict';
/**declare the uftApp module **/

var app = angular.module("uftApp", []);
(function () {
    app.service('invalidDomainService', function ($http) {
        var service = {
            async: function (url) {
                var promise = $http({
                    url: url,
                    method: "GET"
                }).then(function (response) {
                    return response.data;
                });
                return promise;
            }
        }
        return service;
    });
})();

(function () {
    app.controller("accountFormController", ['$scope', '$http', '$window', '$location','$rootScope','$timeout', function ($scope, $http, $window, $location, $rootScope, $timeout) {

        $scope.showPhone = false;
        $scope.isMember = false;
        $scope.updateMessage="Save";
        $scope.showBanner = false;
        $scope.communitylink='';
        $scope.loading = false;
        $scope.verifiedMember = false;
        $scope.verifiedStyle = false;
        $scope.showNewPassword = false;
        $scope.showConfirmPassword = false;

        function showCommunityBanner(){
            $http({
                url:'showCommunityBanner',
                method:"GET"
            }).then(function (response){
                if(response.data!=null && response.data =='none'){
                    $scope.showBanner = false;
                }
                else if(response.data!=null ){
                    $scope.showBanner = true;
                    $scope.communitylink=response.data;
                }
                $rootScope.chapterleadercommunity = $scope.showBanner;
            });
        }
        showCommunityBanner();
        var emailModal = document.getElementById("emailModal");
        var subscribeModal = document.getElementById("subscribeModal");
        var editStyle = document.getElementById("asideEdit").style;
        var wrapperAccount = document.getElementById("wrapperAccount").style;
        var confirmEmailInput = document.getElementById('confirmemail');
        var emailInput = document.getElementById('email');

        $rootScope.openEdit = function(){
            $scope.newpassword = '';
            $scope.confirmpassword = '';

            $scope.email = $scope.oldEmail;
            $scope.confirmemail = '';

            $scope.accountForm.newpassword.$setValidity('invalidPasswordFormat', true);
            $scope.accountForm.confirmpassword.$setValidity('newpasswordisEmpty', true);
            $scope.accountForm.confirmpassword.$setValidity('invalidconfirmPassword', true);

            $scope.accountForm.email.$setValidity('invalidEmail', true);
            $scope.accountForm.email.$setValidity('invalidEmailDomain', true);
            $scope.accountForm.confirmemail.$setValidity('invalidConfirmEmail', true);

            confirmEmailInput.disabled = true;

            editStyle.cssText = "flex: 1; opacity: 1; height: 100%; width: 100%;";
            wrapperAccount.cssText = "width: 0; flex: 0; opacity: 0; height: 0;";
        };

        function closeEdit() {
            editStyle.cssText = "flex: 0; opacity: 0; height: 0; width: 0;";
            wrapperAccount.cssText = "width: 100%; flex: 1; opacity: 1; height: 100%;";
            window.scrollTo({ top: 0 });
        };


        function loadPersonInfo() {
            $scope.loading = true;
            $http({
                url: "userInfo",
                method: "GET"
            }).then(function (response) {
                if(response.data!=null&&response.data['dbStatus']){
                   let personInfo = response.data['dbObject']['user'];
                   let memberId= response.data['dbObject']['memberId'];
                    var optin=response.data['dbObject']['optin'];
                    var optInNumber=response.data['dbObject']['optInNumber'];
                    var isMember=response.data['dbObject']['member'];
                    var unSubscribe = response.data['dbObject']['emailOptOut'];

                    $scope.verifiedMember = response.data['dbObject']['user']['userAttributes']['member_id'];
                    $rootScope.activeStatus = response.data['dbObject']['unionStatus'];
                    $rootScope.titleId = response.data['dbObject']['titleId'];
                    $rootScope.firstname=personInfo['firstname'];
                    $rootScope.lastname=personInfo['lastname'];
                    $scope.personInfo=personInfo;
                    $scope.isMember=isMember;
                    $scope.isCCP=response.data['dbObject']['ccp'];
                    $scope.memberId=memberId;
                    $scope.unSubscribe=unSubscribe;

                    $scope.email = personInfo.email;
                    $scope.oldEmail = personInfo.email;

                    //phone format xxx-xxx-xxxx
                    if(optin){
                        $scope.optin="yes";
                        $scope.showPhone=true;
                        $scope.optinNumber=optInNumber;
                        var phoneNumberArray=optInNumber.split("-");
                        $scope.phoneField=phoneNumberArray[0];
                        $scope.phoneField2=phoneNumberArray[1];
                        $scope.phoneField3=phoneNumberArray[2];
                    }
                    else{
                        $scope.showPhone=false;
                        $scope.optin="no";
                        $scope.phoneField="";
                        $scope.phoneField2="";
                        $scope.phoneField3="";
                        $scope.optinNumber="";
                    }
                }
                else{
                    $window.location.href="error";
                }
                $http({
                    url:"getNonMemberApp",
                    method:"GET"
                }).then(function(response){
                    if(response.data!=null&&response.data.length>0){
                        $scope.isMember=false;
                        $scope.nonMemberApplicationList=response.data;
                    }
                    else{
                        $scope.isMember=true;
                    }
                });
                $http({
                    url:"getAccessApp",
                    method:"Get"
                }).then(function(response){
                    if(response.data!=null&&response.data.length>0){
                        $scope.accessApp=true;
                        $scope.accessApplicationList=response.data;
                    }
                    else{
                        $scope.accessApp=false;
                    }
                });
                $scope.hasChpaterLeaderSection=false;
                $http({
                    url: "hasChpaterLeaderSection",
                    method: "GET"
                }).then(function (response) {
                    if(response.data!=null&&response.data.length>0){
                        $scope.webList=response.data;
                        $scope.hasChpaterLeaderSection=true;
                    }
                });
                $http({
                    url: "suggestApp",
                    method: "GET"
                }).then(function (response) {
                    if(response.data!=null&&response.data.length>0){
                        $scope.appList=response.data;
                    }
                });

                $scope.hasRetireeSection=false;
                $http({
                    url: "hasRetireeSection",
                    method: "GET"
                }).then(function (response) {
                    if(response.data!=null&&response.data.length>0){
                        $scope.webList=response.data;
                        $scope.hasRetireeSection=true;
                        $rootScope.hasRetireeSection = true;
                    }
                });
                $scope.hasHelpDeskSection=false;
                $http({
                    url: "hasHelpDeskSection",
                    method: "GET"
                }).then(function (response) {
                    if(response.data!=null&&response.data.length>0){
                        $scope.helpDeskList=response.data;
                        $scope.hasHelpDeskSection=true;
                    }
                });
                $scope.hasStaffSection=false;
                $http({
                    url: "hasStaffSection",
                    method: "GET"
                }).then(function (response) {
                    if(response.data!=null&&response.data.length>0){
                        $scope.welfareAdminApp=response.data;
                        $scope.hasStaffSection=true;
                    }
                });

                var url = $location.absUrl();
                if (url.indexOf('?edit') !== -1) {
                    if($scope.verifiedMember) {
                        $rootScope.openEdit();
                        document.getElementById("asideEdit").style.transition = '0s';
                        document.getElementById("wrapperAccount").style.transition = '0s';
                    } else {
                        $scope.verifiedStyle = true;

                    }
                }
            })
            .finally(function () {
                $scope.loading = false;
            });

        }
        loadPersonInfo();

        $scope.goToVerify = function() {
            var hostname = $window.location.hostname;
            var url = hostname + '/verify';
            $window.location.href = 'https://' + url;
        }


        let fixedButton = document.getElementById("btnFixed");

        let heightButton = $(document).height() - $(window).height() - $(window).scrollTop();;

        if(heightButton < 280) {
            fixedButton.style.display = "none"
        }

        window.onscroll = function () {
            scrollFunction();
        };
        function scrollFunction() {
            let scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();
            if (
                scrollBottom > 260 ||
                scrollBottom > 260
            ) {
                fixedButton.style.display = "block";
            } else {
                fixedButton.style.display = "none";
            }

        }

        $scope.isEdit = false;
        $scope.edit = "Edit";
        $scope.cancel=function(){

            $scope.isEdit=false;
            $scope.errorField=false;

            loadPersonInfo();
            closeEdit();
        };
        $scope.enableEdit = function () {
            //$scope.isEdit=!$scope.isEdit;

            $scope.isEdit = true;
        };
        $scope.issueAlert = function(){
            var isSubscribe = document.getElementById("subscribe").checked;
            if(!isSubscribe){
                subscribeModal.style.display = "block";
            }
        };
        $scope.closeSubscribeModal = function(){
            document.getElementById("subscribe").checked=true;
            subscribeModal.style.display ="none";
        };

        $scope.unsubscribeEmail = function(){
            document.getElementById("subscribe").checked=false;
            subscribeModal.style.display ="none";
        };

        window.disableEmail = function() {
          if (emailInput.value === $scope.oldEmail) {
            $scope.confirmemail = "";
            confirmEmailInput.disabled = true;
          } else {
            confirmEmailInput.disabled = false;
          }
        }
        emailInput.addEventListener('input', disableEmail);

        $scope.enableSave = function () {
            if($scope.accountForm.$valid) {
                if (!$scope.updating) {
                    var userInfo = {};
                    var personInfo = {};
                    personInfo['username'] = $scope.personInfo.username;
                    personInfo['lastname'] = $scope.personInfo.lastname;
                    personInfo['firstname'] = $scope.personInfo.firstname;
                    personInfo['email'] = $scope.email;
                    if (personInfo.userAttributes != null && personInfo.userAttributes.zipCode) {
                        personInfo['zipCode'] = personInfo.userAttributes.zipCode;
                    } else {
                        personInfo['zipCode'] = "";
                    }
                    userInfo['user'] = personInfo;
                    userInfo['newpassword'] = $scope.newpassword;
                    userInfo['confirmpassword'] = $scope.confirmpassword;
                    userInfo['optin'] = $scope.optin;
                    userInfo['memberId'] = $scope.memberId;
                    //userInfo['emailOptOut'] = $scope.emailSubscribe;
                    var isSubscribe = document.getElementById("subscribe").checked;

                    if(isSubscribe == $scope.unSubscribe){
                        userInfo['emailOptOut']=!isSubscribe;
                    }
                    if ($scope.optin == 'yes') {
                        userInfo['phoneField'] = $scope.phoneField + "-" + $scope.phoneField2 + "-" + $scope.phoneField3;
                        $scope.optinNumber = userInfo['phoneField'];
                    } else {
                        userInfo['phoneField'] = "";
                        $scope.phoneField = "";
                        $scope.phoneField2 = "";
                        $scope.phoneField3 = "";
                    }
                    $scope.closeEmailModal = function () {
                        $window.location.href = 'logout';
                    };

                    $http({
                        url: "updateUser",
                        method: "POST",
                        data: JSON.stringify(userInfo),
                        dataType: 'json',
                        contentType: 'application/json;charset=uft-8'
                    }).then(function (response) {
                        if (response.data == 'dbIssue') {
                            $window.location.href = "error";
                            $scope.updating = false;
                            $scope.updateMessage = "Save";
                        } else if (response.data == 'success') {
                            $scope.updating = false;
                            $scope.updateMessage = "Save";
                            loadPersonInfo();
                            $scope.edit = "Edit";
                            $scope.isEdit = false;
                            $scope.errorField = false;
                            $http({
                                url: "getEmailUpdateStatus",
                                method: "GET"
                            }).then(function (response) {
                                if (response.data != null && response.data) {
                                    emailModal.style.display = "block";
                                }
                            });
                            closeEdit();
                        } else {
                            $scope.updating = false;
                            $scope.updateMessage = "Save";
                            var errorMessage = response.data;
                            var errorArray = errorMessage.split("#");
                            $scope.errorField = true;
                            $scope.errors = errorArray;
                            document.getElementById("username").focus();
                        }
                    });

                }
                }

        };

        $scope.phonefocus = function (fieldName, event) {
            var value = event.target.value;
            if (fieldName == "phoneField") {
                if (value.length == 3) {
                    var element = $window.document.getElementById("phone2");
                    element.focus();
                }
            }
            if (fieldName == "phoneField2") {
                if (value.length == 3) {
                    var element = $window.document.getElementById("phoneField3");
                    element.focus();
                }
            }
        };


    }]);
})();
(function(){
    app.directive("uftHeader",function(){
        return {
            templateUrl:"uftHeader",
            controller:"logoutController"
        }
    });
})();
(function() {
  app.directive("spinner", function() {
    return {
      restrict: 'C',
      scope: {
        isLoading: '='
      },
      link: function(scope, element) {
        scope.$watch('loading', function(newVal) {
          if (newVal) {
            element.addClass('show-spinner');
          } else {
            element.removeClass('show-spinner');
          }
        });
      }
    };
  });
})();

(function(){
    app.controller("logoutController",['$scope','$http','$window','$rootScope',function($scope,$http,$window,$rootScope){
        $scope.logout=function(){
            sessionStorage.clear();
            $window.location.href = 'logout';
        };
        $scope.toggleDropdown = function($event){
            if (document.getElementById("menuDropdown").classList.contains("show")){
                document.getElementById("menuDropdown").classList.remove("show");
            }else{
                document.getElementById("menuDropdown").classList.toggle("show");
            }
        }
        $scope.firstname = $rootScope.firstname;
        $scope.lastname = $rootScope.lastname;
        $scope.activeStatus = $rootScope.activeStatus;

        $scope.chapterleadercommunity = $rootScope.chapterleadercommunity;
        $scope.hasRetireeSection = $rootScope.hasRetireeSection;


        $window.onclick = function(event){

            if(!event.target.matches('.showmenutag')){
                let dropdowns = document.getElementsByClassName("dropdown-menu");
                let i;
                for(i=0;i<dropdowns.length;i++){
                    let openDropdown = dropdowns[i];
                    if(openDropdown.classList.contains("show")){
                        openDropdown.classList.remove("show")
                    }
                }
            }
        }

    }]);
})();
(function () {
    app.directive("uftFooter", function () {
        return {
            templateUrl: "uftFooter",
            link: function (scope, elm, attrs, ctrl) {
                scope.currentYear = new Date().getFullYear();
            }
        }
    });
})();
(function () {
    app.directive("nameVerify", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = /^([a-zA-Z]+[\.\-'\s]?)+[a-zA-Z]*$/.test(value);
                        if (valid) {
                            var lastValue = value.charAt(value.length - 1);
                            if (lastValue == '.' || lastValue == '-' || lastValue == "'") {
                                valid = false;
                            }
                        }
                        ctrl.$setValidity('invalidName', valid);
                    }
                    return value;
                });
            }
        }
    });
})();
(function () {
    app.directive("isZip", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = value.length == 5;
                        ctrl.$setValidity('invalidLength', valid);
                        valid = /^\d+$/.test(value);
                        ctrl.$setValidity('invalidNumber', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("isSsn", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = value.length == 9;
                        if (valid && (value == '123456789' || value == '987654321')) {
                            valid = false;
                        }
                        if (valid) {
                            valid = !(/^(\d+)\1{8}/.test(value));
                        }
                        if (valid) {
                            valid = !(/^666/.test(value));
                        }
                        if (valid && ((value.substring(0, 3) == '000') || value.substring(3, 5) == '00' || value.substring(5) == '0000')) {
                            valid = false;
                        }
                        ctrl.$setValidity('invalidNumber', valid);
                        valid = /^\d+$/.test(value);
                        ctrl.$setValidity('invalidssn', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("isEis", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = value.length <= 10;
                        ctrl.$setValidity('invalidNumber', valid);
                        valid = /^\d+$/.test(value);
                        ctrl.$setValidity('invalideis', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("isFile", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = value.length <= 7;
                        ctrl.$setValidity('invalidNumber', valid);
                        valid = /^\d+$/.test(value);
                        ctrl.$setValidity('invalidfile', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("passwordValid", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = /^(?=.*\d)(?=.*[A-Z])[a-zA-Z0-9\w~@#$%^&*+=`|{}:;!.?\"()\[\]-]{8,}$/.test(value);
                        ctrl.$setValidity('invalidPassword', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("numberValid", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = /^\d+$/.test(value);
                        ctrl.$setValidity('invalidNumber', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("newpasswordValid", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    var valid = /^(?=.*\d)(?=.*[A-Z])[a-zA-Z0-9\w~@#$%^&*+=`|{}:;!.?\"()\[\]-]{8,}$/.test(value);
                    ctrl.$setValidity('invalidPasswordFormat', valid);
                    if (scope.confirmpassword != null) {
                        scope.confirmpassword = "";
                    }
                    if((value==null||value=="")&&(scope.confirmpassword==null||scope.confirmpassword=="")){
                        ctrl.$setValidity('invalidPasswordFormat', true);

                    }
                    return value;

                })
            }
        }
    });
})();
(function () {
    app.directive("confirmpasswordValid", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {

                    if(value == null || value === "") {
                        var valid = true;
                        if(scope.newpassword != null && scope.newpassword !== '') {
                            valid = false;
                        }
                        ctrl.$setValidity('newpasswordisEmpty', valid);
                        ctrl.$setValidity('invalidconfirmPassword', true);
                    }
                    else if(value) {

                        valid = value === scope.newpassword;
                        ctrl.$setValidity('invalidconfirmPassword', valid);
                        ctrl.$setValidity('newpasswordisEmpty', true);
                    }

                    return value;
                })
            }
        }
    });
})();
(function () {
    app.directive("emailVerify", function ($http, invalidDomainService) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    var invalidDomainBackUp = ["BEACONSCHOOL.ORG", "BOE.NET", "DOE.COM", "DOE.ORG", "EASTBRONXACADEMY.ORG", "HEALTH.NYC.GOV",
                        "HRA.NYC.GOV", "IS89.ORG", "LMCMC.COM", "MIHS.NYCDOE.ORG", "NNCDOE.GOV", "NYC.BOE", "NYC.BOE.NET", "NYC.DOE.GOV",
                        "NYC.GOV", "NYC.SCHOOLS", "NYC.SCHOOLS.GOV", "NYC.SCHOOLS.NET", "NYC.SCHOOLS.ORG", "NYCBOD.NET", "NYCBOE.COM",
                        "NYCBOE.EDU", "NYCBOE.GOV", "NYCBOE.NET", "NYCBOE.ORG", "NYCBOE.SCHOOLS", "NYBOR.NET", "NYCDOE.COM",
                        "NYCDOE.EDU", "NYCDOE.GOV", "NYCDOE.NET", "NYCDOE.ORG", "NYCE.BOE.NET", "NYCLABSCHOOL.ORG", "NYCSHOOLS.EDU",
                        "NYCSCHOOLS.GOV", "SCHOOL.NYC.GOV", "SCHOOLS.EDU", "SCHOOLS.EDU.NYC.GOV", "SCHOOLS.GOV", "SCHOOLS.GOV.NYC",
                        "SCHOOLS.NET.GOV", "SCHOOLS.NYC", "SCHOOLS.NYC.EDU", "SCHOOLS.NYC.GOV", "SCHOOLS.NYC.GOV.ORG", "SCHOOLS.NYC.ORG",
                        "SCHOOLSNYC.DOE.ORG", "SCHOOLSNYC.GOV", "VNSNY.ORG", "2NYCBOE.NET"];


                    invalidDomainService.async("forbidden_domain").then(function (data) {
                        var invalidDomain = data;
                        if (invalidDomain == null || invalidDomain.length <= 0) {
                            invalidDomain = invalidDomainBackUp;
                        }
                        var valid = true;
                        for (var i = 0; i < invalidDomain.length; i++) {
                            if (value.toUpperCase().indexOf(invalidDomain[i]) > 0) {
                                scope.invalidDomain = invalidDomain[i];
                                valid = false;
                                break;
                            }
                        }
                        ctrl.$setValidity('invalidEmailDomain', valid);
                    });
                    var valid = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
                    ctrl.$setValidity('invalidEmail', valid);

                    if (scope.confirmemail != null) {
                        scope.confirmemail = "";
                    }

                   if (value === scope.oldEmail) {
                    scope.accountForm.confirmemail.$setValidity('invalidConfirmEmail', true);
                   }

                    return value;
                });
            }
        }
    });
})();
(function () {
    app.directive("confirmemailVerify", function ($http, invalidDomainService) {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value == null || value === "" ) {
                        ctrl.$setValidity('invalidConfirmEmail', false);
                        if(scope.email === scope.oldEmail) {
                            ctrl.$setValidity('invalidConfirmEmail', true);
                        }
                    } else {
                        ctrl.$setValidity('invalidConfirmEmail', false);
                        if (value === scope.email) {
                        ctrl.$setValidity('invalidConfirmEmail', true);
                        }
                    }
                    return value;
                });
            }
        }
    });
})();
(function () {
    app.controller("versionController", ['$scope', '$http', function ($scope, $http) {
        $http({
            url: "versionInfo",
            method: "GET",
        }).then(function (response) {
            if (response.data != null) {
                $scope.projectname = response.data.name;
                $scope.projectdescription = response.data.description;
                $scope.projectversion = response.data.version;
            }
        }, function (response) {
            //failure
        });
    }]);
})();
(function () {
    app.directive("usernameVerify", function () {
        return {
            restrict: 'A',
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (value) {
                    if (value) {
                        var valid = /^([a-zA-Z]+[\.\-'\s]?)+[a-zA-Z]*$/.test(value);
                        ctrl.$setValidity('invalidusername', valid);
                    }
                    return value;
                })
            }
        }
    });
})();
(function () {
    app.controller("verifyController", ['$scope', '$http', function ($scope, $http) {
        $http({
            url: "verifylink",
            method: "GET",
        }).then(function (response) {
            if (response.data != null) {
                $scope.verifyUrl =response.data;
            }
        }, function (response) {
            //failure
        });
    }]);
})();


