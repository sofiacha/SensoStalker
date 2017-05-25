<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
error_reporting(0);

// json response array
$response = array();
require_once 'include/db_connect_.php';

// connecting to db
$db = new DB_CONNECT();
 
    // get the temp by email and password
 $temph = mysql_query("SELECT * FROM tempm") or die(mysql_error());
    
		if (mysql_num_rows($temph) > 0) {
		$response = array();
		
		while ($row= mysql_fetch_assoc($temph)) {
        // use is found
		$response[] = $row;       
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