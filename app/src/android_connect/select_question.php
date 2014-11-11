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
  
  	$sth = mysql_query("
			select _id, user_id, head, body, total_votes, answers, comments, time_edited
			from questions
			where _id = ".$_POST['1'].";
			");
  	$rows = array();
  	while($r = mysql_fetch_assoc($sth)) {
	  $rows[] = $r;
  	}
  print json_encode($rows);
?>