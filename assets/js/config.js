var ServiceAddress =
{
    HOST: "http://123.57.207.170/api/"
	//HOST : "http://192.168.1.6/api/"
};

ServiceAddress["LOGIN"] = ServiceAddress.HOST + "APILogin.ashx";
ServiceAddress["HOUSE_LIST"] = ServiceAddress.HOST + "APIHouseList.ashx";
ServiceAddress["CUSTOM_LIST"] = ServiceAddress.HOST + "APICustomList.ashx";
ServiceAddress["HOUSE_DETAIL"] = ServiceAddress.HOST + "APIGetHouseInfo.ashx";
ServiceAddress["PICTURE_LOAD"] = ServiceAddress.HOST + "APIRecvUploadImage.ashx";
ServiceAddress["HOUSE_CREATE"] = ServiceAddress.HOST + "APICreateHouseInfo.ashx";
ServiceAddress["CUSTOM_CREATE"] = ServiceAddress.HOST + "APICreateCustom.ashx";
ServiceAddress["GET_SELECTS"] = ServiceAddress.HOST + "getSelectDataList.ashx";
ServiceAddress["HOUSE_UPDATE"] = ServiceAddress.HOST + "APIUpdateHouseInfo.ashx";
ServiceAddress["PICTURE_DELETE"] = ServiceAddress.HOST + "APIDeletePicture.ashx";
ServiceAddress["HOUSE_DELETE"] = ServiceAddress.HOST + "APIDeleteHouseInfo.ashx";
ServiceAddress["CUSTOM_DELETE"] = ServiceAddress.HOST + "APIDeleteHouseInfo.ashx";
ServiceAddress["UPDATE_PASS"] = ServiceAddress.HOST + "APIUpdateUserPassword.ashx";
ServiceAddress["VISIT_LIST"] = ServiceAddress.HOST + "APIGetVisitList.ashx";
ServiceAddress["CREATE_VISIT"] = ServiceAddress.HOST + "APIVisitHouseInfo.ashx";


function getLocalUserInfo()
{
	        try
            {
                jsonStr = UtilityObject.getUserInfoFromRes();
            }
            catch (err)
            {
				return null;
			}

            var user = null;

            try
            {
                user = jQuery.parseJSON(jsonStr);
            }
            catch(err)
            {
               return null;
            }
			
			return user;
}