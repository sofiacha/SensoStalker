<?php
$servername = "localhost";
$username = "root";
$password = "password";
$dbname = "sensostalker";
error_reporting(0);
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
	if (isset($_GET['temp']) ) {
		$gkfjgf=  $_GET['temp'];
$av=0;
$sql = "SELECT temperature FROM temph  ORDER by idtemph DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
	$i=0;
    while($row = $result->fetch_assoc()) {
		$i++;
        $av=$av+$row["temperature"];
		if ($i==240) { 
		break;}
    }
} else {
    echo "0 results";
}
$av = $av/240;
$my_date = strtotime($var2); 
$sql = "INSERT INTO tempm (temperature)
VALUES ($av);";
if ($conn->multi_query($sql) === TRUE) {
    echo "New records created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
$conn->close();
	}
?>