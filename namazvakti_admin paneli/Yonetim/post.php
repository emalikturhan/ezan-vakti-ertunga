<?php ob_start();
if (session_status() !== PHP_SESSION_ACTIVE) {session_start();}
require_once("config.php");
require_once("Class.php");
$db = getDB();
$class = new NamazClass();


if(isset($_POST["get_sure"])){
$id =  $class->crypto($_POST["get_sure"],"d");
$get = $db->prepare("SELECT * FROM NamazSureleri WHERE id=?");
$get->execute(array($id));
$data="";
foreach ($get as $key) {
$data = $class->crypto($key["id"],"e")."|".$key["baslik"]."|".$key["arapca"]."|".$key["okunus"]."|".$key["anlami"];
}
echo $data;
}



?>