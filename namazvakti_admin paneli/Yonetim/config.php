<?php

date_default_timezone_set('Europe/Istanbul');

error_reporting(E_ALL);



/* DATABASE Ayarları */

define('DB_SERVER', 'localhost');

define('DB_USERNAME', 'mehmet79_namaz');

define('DB_PASSWORD', 'D045namaz_');

define('DB_DATABASE', 'mehmet79_namazvakti');



function getDB() 

{

$dbhost=DB_SERVER;

$dbuser=DB_USERNAME;

$dbpass=DB_PASSWORD;

$dbname=DB_DATABASE;

try {

$dbConnection = new PDO("mysql:host=$dbhost;dbname=$dbname", $dbuser, $dbpass); 

$dbConnection->exec("set names utf8mb4");

$dbConnection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

return $dbConnection;

}

catch (PDOException $e) {

echo 'Connection failed: ' . $e->getMessage();

}



}





?>