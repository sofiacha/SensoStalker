<?php


error_reporting(0);
//require_once 'include/DB_Functions.php';
//$db = new DB_Functions();

// json response array
//$response = array();
require_once 'include/db_connect_.php';

// connecting to db
$db = new DB_CONNECT();
 
    // get the temp by email and password
$humh = mysql_query("SELECT humidity FROM humh ORDER BY humid DESC LIMIT 1") or die(mysql_error());


if (mysql_num_rows($humh) >= 0) {
		//$response = array();
		
		while ($row= mysql_fetch_assoc($humh)) {
        // use is found
		$response = $row;
		
       
		}
		 echo json_encode($response);
		}
		 else {
        // temp is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "There is no entries to display!";
        echo json_encode($response);
    }	
	?>