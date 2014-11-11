<?php
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
	$db = new DB_CONNECT();
  // Check connection
  if (mysqli_connect_errno())
	{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	$num = count($_POST);     
     if($num != 2)
		exit(1);
	$id = filter_var($_POST['1'], FILTER_SANITIZE_NUMBER_INT); 
	$username = mysql_real_escape_string($_POST['2']);
  
  	$sth = mysql_query("
			select _id, user_name
			from users
			where _id = ".$id." and user_name = '".$username."'
			;");
	$numrows = mysql_num_rows($sth);
	if($numrows > 1) {
		 echo "to do hacker alert";
	}else{
  		$rows = array();
	  	while($r = mysql_fetch_assoc($sth)) {
	  		$rows[] = $r;
  		}
  		print json_encode($rows);
	}
?>