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
    if($num == 2){
		$sth = mysql_query("
			select type from qvotes
			where question_id = '".$_POST['1']."' and user_id = '".$_POST['2']."'
			");
			
		$rows = array();
	
		while($r = mysql_fetch_assoc($sth)) {
			$rows[] = $r;
		}
		print json_encode($rows);
	}
	
	else if ($num == 3){
		$vtype = $_POST['3'];
		$sth = mysql_query("
			delete from qvotes
			where question_id = '".$_POST['1']."' and user_id = '".$_POST['2']."'
			");
		if(	$vtype != -1){
			$sth = mysql_query("
				INSERT INTO qvotes (question_id,user_id,type)
				VALUES ('".$_POST['1']."','".$_POST['2']."',".$vtype.");
				");
		}
	}
	else
		exit(1);
?>