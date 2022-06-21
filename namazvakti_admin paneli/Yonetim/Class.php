<?php
class NamazClass{


/*************************  STANDAR FUNCTİONLAR ******************/
public function anti($tekst) {
$tekst = strip_tags($tekst);
$banlist = array ("drop", "DROP","echo", "ECHO", "insert", "INSERT", "select", "SELECT", "update", "UPDATE", "delete", "DELETE", "distinct", "DISTINCT", "having", "HAVING", "truncate", "TRUNCATE", "replace", "REPLACE", "handler", "HANDLER", "like", "LIKE", "procedure", "PROCEDURE", "limit", "LIMIT", "order by", "ORDER BY", "group by", "GROUP BY", "where", "WHERE", "from", "FROM", "asc", "ASC", "desc", "DESC", "union", "UNION", "include", "INCLUDE",  "table_name", "TABLE_NAME", "INFORMATION_SCHEMA", "information_schema", "information_schema.tables", "INFORMATION_SCHEMA.TABLES" , ".TABLES", ".tables", "eval","'");
$tekst = str_ireplace($banlist, " ", $tekst);
$tekst = trim($tekst);
return($tekst);
}

public function email_control($str){
return (!preg_match("/^([a-z0-9\+_\-]+)(\.[a-z0-9\+_\-]+)*@([a-z0-9\-]+\.)+[a-z]{2,6}$/ix", $str))?FALSE:TRUE;
}

public function crypto( $string, $action = 'e' ) {
$secret_key = 'server12020namaz48vakitleri';
$secret_iv  = 'server12020namaz48vakitleri';
$output = false;
$encrypt_method = "AES-256-CBC";
$key = hash( 'sha256', $secret_key );
$iv = substr( hash( 'sha256', $secret_iv ), 0, 16 );
if( $action == 'e' ) {
$output = base64_encode( openssl_encrypt( $string, $encrypt_method, $key, 0, $iv ) );
}
else if( $action == 'd' ){
$output = openssl_decrypt( base64_decode( $string ), $encrypt_method, $key, 0, $iv );
}
return $output;
}

public function DateCalculate($date){
date_default_timezone_set('Europe/Istanbul');
$bol=explode("-", $date);
$year=date('Y')-$bol[0];
return $year;
}


public function login($username,$pass){
try{
$db = getDB();
$query = $db->prepare("SELECT * FROM Users WHERE mail=? and sifre=?");
$query->execute(array($username,$pass));
if($query->rowCount() > 0)
{
return "1";
} else{
return "0";
}
} catch (PDOException $e) {
exit($e->getMessage());
}
}


public function UserLog($username,$durum){
try{
$db = getDB();
$ip = $_SERVER["REMOTE_ADDR"];
$query = $db->prepare("INSERT INTO UserLog SET mail = ?, giris_tarihi = NOW(), ip = ?, durum = ?");
$query->execute(array($username,$ip,$durum));
} catch(PDOException $e){
exit($e->getMessage());
}
}


public function GetUserID($username,$kolon){
try{
$db = getDB();
$veri = "";
$query = $db->prepare("SELECT $kolon as 'kolon' FROM Users WHERE mail=:username");
$query->bindParam(":username",$username,PDO::PARAM_STR);
$query->execute();
foreach ($query as $row) {
$veri = $row["kolon"];
}
return $veri;
} catch(PDOException $e){
exit($e->getMessage());
}
}


public function UserControl($kolon,$username){
try{
$db = getDB();
$cek = $db->prepare("SELECT $kolon AS 'kolon' FROM Users WHERE mail=?");
$cek->execute(array($username));
foreach ($cek as $key) {
return $key["kolon"];
}
} catch(PDOException $e) {
exit($e->getMessage());
}
}


public function LogDelete($id){
try{
$db = getDB();
$sil = $db->prepare("DELETE FROM UserLog WHERE id=?");
$sil->execute(array($id));
if($sil){
 return "1";
} else {
 return "0";
}
} catch(PDOException $e){
   exit($e->getMessage());
}
}


#region Admin Functions
public function AddAdmin($name,$pass,$mail,$phone,$menu){
$db = getDB();
$control = $db->prepare("SELECT * FROM Users WHERE mail=?");
$control->execute(array($name));
if($control->rowCount() > 0){
return "0";
} else {
$save = $db->prepare("INSERT INTO Users SET isim=?, sifre=?, tarih=NOW(), mail=?, telefon=?, menu=?, yetki='1'");
$save->execute(array($name,$pass,$mail,$phone,$menu)); 
return "1";
}
}

public function UpdateAdmin($name,$pass,$mail,$phone,$menu,$id){
$db = getDB();
$save = $db->prepare("UPDATE Users SET isim=?, sifre=?, mail=?, telefon=?, menu=?, yetki='1' where id=?");
$save->execute(array($name,$pass,$mail,$phone,$menu,$id));  
if($save){
return "1";
}
}


public function RemoveAdmin($id){
try{
$db = getDB();
$remove = $db->prepare("DELETE FROM Users WHERE id=?");
$remove->execute(array($id));
if($remove){
return "1";
} else {
return "0";
}
} catch(PDOException $e){
exit($e->getMessage());
}

}
#endregion



public function generateRandomString($length = 10) {
$characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
$charactersLength = strlen($characters);
$randomString = '';
for ($i = 0; $i < $length; $i++) {
$randomString .= $characters[rand(0, $charactersLength - 1)];
}
return $randomString;
}



}
?>



