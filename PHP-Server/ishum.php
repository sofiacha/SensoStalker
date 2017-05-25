<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
error_reporting(0);
require_once 'include/db_connect_.php';

// connecting to db
$db = new DB_CONNECT();

if (isset($_GET['hum']) ) {

    $hum = $_GET['hum'];
 
		$tmp = mysql_query("INSERT INTO `humh`(`humidity`) VALUES ($hum)") or die(mysql_error());
       if (mysql_num_rows($hum) >= 0) {
		
		echo $hum ;
		}
		 else {
      
        echo "There is no entries to display!";
    }
}
?>

