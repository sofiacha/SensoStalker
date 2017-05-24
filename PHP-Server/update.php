<?php


error_reporting(0);
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email']) && (isset($_POST['name']) || isset($_POST['password']))) {
	
    // receiving the POST params
    $name = $_POST['name'];
    $password = $_POST['password'];
	$email = $_POST['email'];
    // check if user is already existed with the same email
    if ($db->isUserExisted($email)) {
           
        $user = $db->upUser($name, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    } else {
		 // user already existed
      $response["error"] = TRUE;
        $response["error_msg"] = "User not found " . $email;
        echo json_encode($response);
		//echo json_encode($user);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

