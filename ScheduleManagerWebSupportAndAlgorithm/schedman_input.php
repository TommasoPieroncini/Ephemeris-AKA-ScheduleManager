<?php

$user = "root";
$pass = "";
$db = "schedman";


$con = mysqli_connect("localhost", $user, $pass, $db);
if (!$con)
	{
		die("Could not connect: ".mysql_error());
	}

$class1 = $_POST["Class1"];
$class2 = $_POST["Class2"];
$class3 = $_POST["Class3"];
$class4 = $_POST["Class4"];

print($class1);
print($class2);
print($class3);
print($class4);
$output = shell_exec("java scheduleGenerator '{$class1}' '{$class2}' '{$class3}' '{$class4}'");

print($output);
$list = explode("-------",$output);
for ($x=0; $x < count($list); $x++){
	$courses = explode("?",$list[$x]);
	$con->query("INSERT INTO coursedata (course1,course2,course3,course4) VALUES ('{$courses[0]}','{$courses[1]}','{$courses[2]}','{$courses[3]}')");
}


echo "Thank you for your submission";



?>