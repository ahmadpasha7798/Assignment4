<?php
require_once("SimpleRest.php");
require_once("Mobile.php");
		
class MobileRestHandler extends SimpleRest {

	function getApplicationList_student($roll) {	

		$mobile = new Mobile();
		$rawData = $mobile->getApplicationList_student($roll);

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('S_date' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}


	function getUser($id) {	

		$mobile = new Mobile();
		$rawData = $mobile->getUser($id);

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('id' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}


	function getStaff($id) {	

		$mobile = new Mobile();
		$rawData = $mobile->getStaff($id);

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('id' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}
	

	function getStudent($roll) {	

		$mobile = new Mobile();
		$rawData = $mobile->getStudent($roll);

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('roll' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}

	function getApplicationList_staff($role) {	

		$mobile = new Mobile();
		$rawData = $mobile->getApplicationList_staff($role);

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('S_date' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}

	function getAppNum() {	

		$mobile = new Mobile();
		$rawData = $mobile->getAppNum();

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('max' => 0);		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}

	function updateSection($roll,$sec) {	

		$mobile = new Mobile();
		$mobile->updateSection($roll,$sec);
		$rawData = array('S_date' => 'empty');	

	}

	function insertUser($id,$pwd,$role) {	

		$mobile = new Mobile();
		$mobile->insertUser($id,$pwd,$role);
		$rawData = array('S_date' => 'empty');	

		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('S_date' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}
	}
	function insertApplication($S_date,$id,$roll,$old_sec,$N_sec,$description,$staff_comment,$app_status) {	

		$mobile = new Mobile();
		$mobile->insertApplication($S_date,$id,$roll,$old_sec,$N_sec,$description,$staff_comment,$app_status);
		$rawData = array('S_date' => 'empty');	
		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('S_date' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}

	}

	function deleteApplication($id) {	

		$mobile = new Mobile();
		$mobile->deleteApplication($id);
		$rawData = array('S_date' => 'empty');	
		if(empty($rawData)) {
			$statusCode = 404;
			$rawData = array('S_date' => 'empty');		
		} else {
			$statusCode = 200;
		}

		$requestContentType = 'application/json';//$_POST['HTTP_ACCEPT'];
		$this ->setHttpHeaders($requestContentType, $statusCode);
		
		$result["output"] = $rawData;
				
		if(strpos($requestContentType,'application/json') !== false){
			$response = $this->encodeJson($result);
			echo $response;
		}

	}
	

	public function encodeJson($responseData) {
		$jsonResponse = json_encode($responseData);
		return $jsonResponse;		
	}
}
?>