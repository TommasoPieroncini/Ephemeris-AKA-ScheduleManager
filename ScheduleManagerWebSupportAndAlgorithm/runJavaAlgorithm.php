<?php

$user = "root";
$pass = "";
$db = "schedman";

$con = mysqli_connect("localhost", $user, $pass, $db);
if (!$con)
	{
		die("Could not connect: ".mysql_error());
	}

$output = shell_exec("java scheduleGenerator CS1301/MWF/NP/SUMMET CS1331/NP/NP/SIMPKINS CS1332/MWF/NP/SWEAT MATH1552/MTWRF/NP/NP");
$list = explode("__________",$output);
//print_r($list);
for ($x=0; $x < count($list); $x++){
	$courses = explode("?",$list[$x]);
	$con->query("INSERT INTO coursedata (course1,course2,course3,course4) VALUES ('{$courses[0]}','{$courses[1]}','{$courses[2]}','{$courses[3]}')");
}

print($courses[0]);

mysqli_close($con);

echo "Thank you for your submission!";
?>