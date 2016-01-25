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

$query = $con->query("INSERT INTO userdata (username,password) VALUES ('{$username}','{$password}')");

echo "Thank you for registering!";

mysql_close($con);

?>