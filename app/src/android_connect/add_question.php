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
	$body = mysql_real_escape_string($_POST['1']);
	$tags = mysql_real_escape_string($_POST['2']);
	$user_id = filter_var($_POST['3'], FILTER_SANITIZE_NUMBER_INT); 
	
/*	echo " insert into questions (head, body, tags, user_id)
			output Inserted._id
			values ('".$head."', '".$body."', '".$tags."', ".$user_id." )
			;";*/
  
  	$sth = mysql_query("
			insert into questions (body, tags, user_id)
			values ('".$body."', '".$tags."', ".$user_id." )
			;");
	$sth = mysql_query('SELECT LAST_INSERT_ID()');
				
	$numrows = mysql_num_rows($sth);
  		$rows = array();
	  	while($r = mysql_fetch_assoc($sth)) {
	  		$rows[] = $r;
  		}
  		print json_encode($rows);
?>