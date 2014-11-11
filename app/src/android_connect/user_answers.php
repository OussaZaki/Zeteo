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
	
	$user_id = filter_var($_POST['1'], FILTER_SANITIZE_NUMBER_INT); 
	
	/*echo "	insert into answers (body, question_id,  user_id)
			values ('".$body."', ".$question_id.", ".$user_id.")
			;";*/
  

  	$sth = mysql_query("select questions._id, questions.body, users.user_name,
						questions.total_votes, questions.comments, questions.time_edited
						from questions joing users
						where questions.user_id = users.user_id 
						order by time_edited desc
						limit 100;");
			
	$numrows = mysql_num_rows($sth);
  		$rows = array();
	  	while($r = mysql_fetch_assoc($sth)) {
	  		$rows[] = $r;
  		}
  		print json_encode($rows);
?>