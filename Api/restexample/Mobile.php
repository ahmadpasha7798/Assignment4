<?php
require_once("dbcontroller.php");
/* 
A domain Class to demonstrate RESTful web services
*/
Class Mobile {
	private $mobiles = array();
	/*
		you should hookup the DAO here
	*/
	public function getApplicationList_student($roll){
		$query = "SELECT * FROM Application_data WHERE rollno='".$roll."'";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function getUser($id){
		$query = "SELECT * FROM Login_data WHERE id='".$id."';";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function getStaff($id){
		$query = "SELECT * FROM Staff_data WHERE id='".$id."';";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function getStudent($roll){
		$query = "SELECT * FROM Student_data WHERE rollno='".$roll."'";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function getApplicationList_staff($role){
		if($role=="Coordinator")
			$query = "SELECT * FROM Application_data WHERE app_status='Pending';";
		else
			$query = "SELECT * FROM Application_data WHERE app_status='Under Processing';";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function getAppNum(){
		$query="SELECT MAX( id ) AS max FROM Application_data;";
		$dbcontroller = new DBController();
		$this->mobiles = $dbcontroller->executeSelectQuery($query);
		return $this->mobiles;
	}

	public function updateSection($roll,$sec){
		$query="Update Student_data SET section='".$sec."' WHERE rollno='".$roll."'";
		$dbcontroller = new DBController();
		$dbcontroller->executeinsertQuery($query);
	}


	public function insertUser($id,$pwd,$role){
		$query="INSERT INTO Login_data VALUES ('".$id."','".$pwd."','".$role."');";
		$dbcontroller = new DBController();
		$dbcontroller->executeinsertQuery($query);
	}
	

	public function insertApplication($S_date,$id,$roll,$old_sec,$N_sec,$description,$staff_comment,$app_status){
		$query="INSERT INTO Application_data VALUES ('".$S_date."',".$id.",'".$roll."','".$old_sec."','".$N_sec."','".$description."','".$staff_comment."','".$app_status."');";
		$dbcontroller = new DBController();
		$dbcontroller->executeinsertQuery($query);
	}

	public function deleteApplication($id){
		$dbcontroller = new DBController();
		$dbcontroller->deleteApplication($id);
	}
}
?>