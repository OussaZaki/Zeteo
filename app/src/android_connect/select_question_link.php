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
						SELECT questions._id, questions.body, users.user_name, questions.total_votes, questions.comments, questions.time_edited, questions.answers
						FROM questions
						JOIN users
						WHERE questions.user_id = users._id
						ORDER BY  questions.time_edited DESC
						LIMIT 100;
					");
  	$rows = array();
  	while($r = mysql_fetch_assoc($sth)) {
	  $rows[] = $r;
  	}
  print json_encode($rows);
?>