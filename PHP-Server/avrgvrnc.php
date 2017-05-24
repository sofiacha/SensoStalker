<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "sensostalker";
error_reporting(0);
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
//for($j=0;$j<3;$j++){
	//sleep(3600);
	if (isset($_GET['temp']) ) {
		$gkfjgf=  $_GET['temp'];
$av=0;
$sql = "SELECT temperature FROM temph  ORDER by idtemph DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
	//echo $result->num_rows;
    // output data of each row
	$i=0;
    while($row = $result->fetch_assoc()) {
		$i++;
		//echo $row["temperature"];
        $av=$av+$row["temperature"];
		if ($i==240) { 
		break;}
    }
} else {
    echo "0 results";
}
$av = $av/240;
$temp=0;
if ($result->num_rows > 0) {
	//echo $result->num_rows;
    // output data of each row
	$i=0;
    while($row = $result->fetch_assoc()) {
		$i++;
		//echo $row["temperature"];
        $temp=$temp+pow(($row["temperature"]-$av),2);
		if ($i==240) { 
		break;}
    }
} else {
    echo "0 results";
}
//echo $temp;
$temp=$temp/240;
$temp=sqrt($temp);
$var1 = date('His');
$var2 = date("Ymd");
$my_date = strtotime($var2); 
$sql = "INSERT INTO temp (avertemp, day, hour,divergence)
VALUES ($av, $var2, $var1, $temp);";
if ($conn->multi_query($sql) === TRUE) {
    echo "New records created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
//sleep(3600);

$conn->close();
	}
?>