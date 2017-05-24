<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
error_reporting(0);
require_once 'include/db_connect_.php';

// connecting to db
$db = new DB_CONNECT();


if (isset($_GET['temp']) ) {


    $temp = $_GET['temp'];
 
		$tmp = mysql_query("INSERT INTO `temph`(`temperature`) VALUES ($temp)") or die(mysql_error());
       if (mysql_num_rows($temp) >= 0) {
		
		echo $temp ;
		}
		 else {
      
        echo "There is no entries to display!";
    }
}
?>

