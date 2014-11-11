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
				select answers._id, answers.body, answers.comments, answers.total_votes, answers.time_edited, users.user_name
				from answers
				join users
				where answers.question_id = ".$_POST['1']." and answers.user_id = users._id;
			");
  	$rows = array();
  	while($r = mysql_fetch_assoc($sth)) {
	  $rows[] = $r;
  	}
  print json_encode($rows);
?>