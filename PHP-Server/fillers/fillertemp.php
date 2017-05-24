<?php
 // load the last run time from a file, database, etc
 require_once '/include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

for($l=0;$l<3;$l++){
for($k=0;$k<60;$k++){ 
  
    echo "top o da minute";
    
	$temph=rand(18,50);
	$humh=rand(50,99);
/**
 * Database config variables
 */
//error_reporting(0);


	//$temph=rand(18,15);
	//echo $temph;
	//if (isset($_GET['temph']) ) {

	// $temph=$_GET['temph'];
	 
	     // create a new temp
        $temper = $db->storeTemp($temph);
		$humid = $db->storeHum($humh);
		//echo json_encode($temper);
        if ($temper) {
            // temph stored successfully
            $response["error"] = FALSE;
			$response["temper"]["temph"] = $temper["temperature"];
           // $response["temper"]["timestemh"] = $temper["timestemh"];
		
           // echo json_encode($response);
        } else {
            // temp failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
	//} else {
   $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (temperature) is missing!";
  //  echo json_encode($response);
 // }
	 //sleep(60); 
	} 
}
?>
