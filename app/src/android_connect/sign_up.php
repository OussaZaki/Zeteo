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
     if($num != 3)
		exit(1);
	$comp = strcmp ($_POST['2'], $_POST['3'] );
	$username = mysql_real_escape_string($_POST['1']); 
	$password = mysql_real_escape_string($_POST['2']);
	$password = hash( 'sha256', $password.$username );
	
	if($comp != 0)
		exit(1);
		
	$sth = mysql_query("
			select _id, user_name
			from users
			where user_name = '".$username."'
			;");
	if( mysql_num_rows($sth) == 0 ){
	
  		$sth = mysql_query("
			insert into users (user_name, password)
			values ('".$username."', '".$password."')
			;");
		$sth = mysql_query("
			select _id, user_name
			from users
			where user_name = '".$username."' and password = '".$password."'
			;");
	}			
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