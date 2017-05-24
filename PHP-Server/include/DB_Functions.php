<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */
error_reporting(0);
class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
		//echo hey;
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

	  /**
     * Update user
     * returns user details
     */
    public function upUser($name, $email, $password) {
		if (isset($password)){
			
		   // $uuid = uniqid('', true);
			$hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"]; // salt
				if (isset($name)){
					$stmt = $this->conn->prepare("UPDATE users SET name=?,encrypted_password=?, salt=?, updated_at=NOW() WHERE email=?");
					$stmt->bind_param("ssss", $name, $encrypted_password, $salt, $email);
					$result = $stmt->execute();
					$stmt->close();
				}
				else{
					$stmt = $this->conn->prepare("UPDATE users SET encrypted_password=?, salt=?, updated_at=NOW() WHERE email=?");
					$stmt->bind_param("sss", $encrypted_password, $salt, $email);
					$result = $stmt->execute();
					$stmt->close();
				}
		}
		else{
			if (isset($name)){
					$stmt = $this->conn->prepare("UPDATE users SET name=?, updated_at=NOW() WHERE email=?");
					$stmt->bind_param("ss", $name, $email);
					$result = $stmt->execute();
					$stmt->close();
				}
		}
		// check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
		public function storeTemp($temph) {
       
        $stmt = $this->conn->prepare("INSERT INTO temph(temperature) VALUES(?)");
        $stmt->bind_param("s", $temph);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM temph WHERE temperature = ?");
            $stmt->bind_param("s", $temph);
            $stmt->execute();
            $temper = $stmt->get_result()->fetch_assoc();
            $stmt->close();
			//echo json_encode($temper);
            return $temper;
        } else {
            return false;
        }
    }
	
		public function storeHum($humh) {
       
        $stmt = $this->conn->prepare("INSERT INTO humh(humidity) VALUES(?)");
        $stmt->bind_param("s", $humh);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM humh WHERE humidity = ?");
            $stmt->bind_param("s", $humh);
            $stmt->execute();
            $humid = $stmt->get_result()->fetch_assoc();
            $stmt->close();
			//echo json_encode($humid);
            return $humid;
        } else {
            return false;
        }
    }
	
	
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

	
	
	
	
	public function getAvTemp() {

        $stmt = $this->conn->prepare("SELECT * FROM temp");

        
        if ($stmt->execute()) {
            $temp = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $temp;
            
        } else {
            return NULL;
        }
    }
	
	
	
	
	
	
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
