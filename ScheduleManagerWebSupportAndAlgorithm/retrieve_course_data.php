<?php

$user = "root";
$pass = "";
$db = "schedman";

$con = mysqli_connect("localhost", $user, $pass, $db);
if (!$con)
	{
		die("Could not connect: ".mysql_error());
	}

$con = mysqli_connect("localhost", $user, $pass, $db);
if (!$con)
        {
                die("Could not connect: ".mysql_error());
        }

$result = $con->query("SELECT * FROM coursedata");

while($row = mysqli_fetch_assoc($result))
        {
                $output[]= $row;
        }

print(json_encode($output));

mysqli_close($con);

?>