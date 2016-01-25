<?php

$user = "root";
$pass = "";
$db = "schedman";

$con = mysqli_connect("localhost", $user, $pass, $db);
if (!$con)
	{
		die("Could not connect: ".mysql_error());
	}

$password = $_POST["password"];
$username = $_POST["username"];

$response = $con->query("SELECT from userData (username,password) password WHERE username = '{$username}'");

if ($response == $password){
	echo "allowed";
} else {
	echo "allowed";
}

mysqli_close($con);

?>