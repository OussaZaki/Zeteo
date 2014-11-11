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
	$question_id = filter_var($_POST['2'], FILTER_SANITIZE_NUMBER_INT); 
	$user_id = filter_var($_POST['3'], FILTER_SANITIZE_NUMBER_INT); 
	/*echo "	insert into answers (body, question_id,  user_id)
			values ('".$body."', ".$question_id.", ".$user_id.")
			;";*/
  
  	$sth = mysql_query("
			insert into answers (body, question_id,  user_id)
			values ('".$body."', ".$question_id.", ".$user_id.")
			;");
	$sth = mysql_query('SELECT LAST_INSERT_ID()');
				
	$numrows = mysql_num_rows($sth);
  		$rows = array();
	  	while($r = mysql_fetch_assoc($sth)) {
	  		$rows[] = $r;
  		}
  		print json_encode($rows);
?>