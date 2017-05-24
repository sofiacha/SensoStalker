<?php
 // load the last run time from a file, database, etc
 require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

for($k=0;$k<60;$k++){ 
  
    echo "top o da minute";
    

	$humh=rand(50,99);
  
		$humid = $db->storeHum($humh);
		//echo json_encode($temper);
        if ($humid) {
            // temph stored successfully
            $response["error"] = FALSE;
			$response["humid"]["humh"] = $humid["humidity"];
           // $response["temper"]["timestemh"] = $temper["timestemh"];
		
           // echo json_encode($response);
        } else {
            // temp failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }

	 
}
?>



