
<?php @session_start (); require_once("config.php"); require_once("Class.php"); 
$class = new NamazClass(); 
$log = $class->UserLog($_SESSION["username"],"3");  
session_destroy();
echo "<meta http-equiv='refresh' content='0;url=login.php'>";
?>


