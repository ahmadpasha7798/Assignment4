<?php
require_once("MobileRestHandler.php");
		
$func = "";
if(isset($_GET["func"]))
	$func = $_GET["func"];
/*
controls the RESTful services
URL mapping
*/
switch($func){

	case "getApplicationList_student":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getApplicationList_student($_GET["roll"]);
		break;

	case "getUser":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getUser($_GET["id"]);
		break;

	case "getStaff":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getStaff($_GET["id"]);
		break;

	case "getStudent":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getStudent($_GET["id"]);
		break;

	case "getApplicationList_staff":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getApplicationList_staff($_GET["role"]);
		break;

	case "getAppNum":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->getAppNum();
		break;
	
	case "updateSection":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->updateSection($_GET["roll"], $_GET["sec"]);
		break;

	case "insertUser":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->insertUser($_GET["id"], $_GET["pwd"], $_GET["role"]);
		break;

	case "insertApplication":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->insertApplication($_GET["S_date"], $_GET["id"], $_GET["roll"], $_GET["old_sec"], $_GET["N_sec"], $_GET["description"], $_GET["staff_comment"], $_GET["app_status"]);
		break;

	case "deleteApplication":
		// to handle REST Url /mobile/list/
		$mobileRestHandler = new MobileRestHandler();
		$mobileRestHandler->deleteApplication($_GET["id"]);
		break;
	
	case "" :
		//404 - not found;
		break;
}
?>
