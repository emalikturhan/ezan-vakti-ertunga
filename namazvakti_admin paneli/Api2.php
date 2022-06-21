<?php
set_time_limit(20);
header('Content-Type: application/json; charset=utf-8');
require_once('Yonetim/config.php');
require_once('Botlar/NamazClass.php');

$db = getDB();
$class = new NamazClass();
error_reporting(E_ALL);
if(isset($_GET["data"])){

//region Get Sehir
if($_GET["data"]=="GetSehir"){
$get = $db->prepare("SELECT * FROM Sehirler WHERE ulkeid=2 ORDER BY sehir ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('sehir'=>$key["sehir"],'sehirid'=>$key["sehirid"]));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion


//region GetIlce
if($_GET["data"]=="GetIlce"){
$get = $db->prepare("SELECT * FROM Ilceler WHERE sehirid=? ORDER BY ilce ASC");
$get->execute(array($_GET["sehir"]));
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('ilce'=>$key["ilce"],'ilceid'=>$key["ilceid"]));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion


//region AylikNamaz
if($_GET["data"]=="AylikNamaz"){
if(isset($_GET["ilceid"])){
$obje->data = $class->AylikNamaz($_GET["ilceid"]);
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
} else {
echo "data yok";
}
}
//endregion

//region GetNamazSureleri
if($_GET["data"]=="GetNamazSureleri"){
$get = $db->prepare("SELECT * FROM NamazSureleri ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('baslik'=>$key["baslik"],'data'=>html_entity_decode($key["data"])));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion

//region GetNamazSureleri
if($_GET["data"]=="GetNamazDualari"){
$get = $db->prepare("SELECT * FROM NamazDualari ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('baslik'=>$key["baslik"],'data'=>html_entity_decode($key["data"])));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion

//region GetHadisler
if($_GET["data"]=="GetHadisler"){
$get = $db->prepare("SELECT * FROM Hadisler ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('baslik'=>$key["baslik"],'data'=>html_entity_decode($key["data"])));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion

//region GetDualar
if($_GET["data"]=="GetDualar"){
$get = $db->prepare("SELECT * FROM Dualar ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('baslik'=>$key["baslik"],'data'=>html_entity_decode($key["data"])));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion


//region GetDiniBilgiler
if($_GET["data"]=="GetDiniBilgiler"){
$get = $db->prepare("SELECT * FROM DiniBilgiler ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
$ibadet_nedir = html_entity_decode($key["ibadet"]);
$islamin_sartlari = html_entity_decode($key["islamin_sartlari"]);
$imanin_sartlari = html_entity_decode($key["imanin_sartlari"]);
$farz_nedir = html_entity_decode($key["farz"]);
$sunnet_nedir = html_entity_decode($key["sunnet"]);
$sehadet = html_entity_decode($key["kelimei_sehadet"]);
$ezan = html_entity_decode($key["ezan"]);

array_push($obje->state,array('ibadet_nedir'=>$ibadet_nedir,'islamin_sartlari'=>$islamin_sartlari,'imanin_sartlari'=>$imanin_sartlari,'farz_nedir'=>$farz_nedir,'sunnet_nedir'=>$sunnet_nedir,'sehadet'=>$sehadet,'ezan'=>$ezan));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion



//region MesajGonder
if($_GET["data"]=="MesajGonder"){
$isim = $_POST["isim"];
$msg  = $_POST["msg"];
$save = $db->prepare("INSERT INTO Iletisim SET isim=?,msg=?,kayit_tarihi=NOW()");
$save->execute(array($isim,$msg));
if($save->rowCount() > 0){
$obje->data = 1;
} else {
$obje->data = 0;
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion



//region GetFarzlar
if($_GET["data"]=="GetFarzlar"){
$get = $db->prepare("SELECT * FROM Farzlar ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('data'=>html_entity_decode($key["data"])));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion


//region GetZikirler
if($_GET["data"]=="GetZikirler"){
$get = $db->prepare("SELECT * FROM Zikirler ORDER BY id ASC");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('id'=>$key["id"],'baslik'=>$key["baslik"],'okunusu'=>$key["okunusu"],'meali'=>$key["meali"],'arapca'=>$key["arapca"],'adet'=>$key["adet"]));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion

//region GetFarzlar
if($_GET["data"]=="GetSosyalMedya"){
$vip = $db->prepare("SELECT * FROM Premium WHERE deviceid=? and statu=1 ORDER BY id DESC Limit 1");
$vip->execute(array($_POST["deviceid"]));
if($vip->rowCount() > 0){
$vip = 1;
} else {
$vip=0;
}

$get = $db->prepare("SELECT * FROM SosyalMedya ORDER BY id DESC Limit 1");
$get->execute();
$obje->state = array();
foreach($get as $key){
array_push($obje->state,array('facebook'=>$key["facebook"],'twitter'=>$key["twitter"],'instagram'=>$key["instagram"],'vip'=>$vip));
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion

//region MesajGonder
if($_GET["data"]=="VipAl"){
$pid       = $_POST["pid"];
$deviceid  = $_POST["deviceid"];

if($pid==1){
$ekle = $db->prepare("INSERT INTO Premium SET deviceid=?,statu=1,vip_date=(NOW() + INTERVAL 30 DAY) ,islem_tarihi=NOW(),paketid=?");
$ekle->execute(array($deviceid,$pid));
} else if($pid==2){
$ekle = $db->prepare("INSERT INTO Premium SET deviceid=?,statu=1,vip_date=(NOW() + INTERVAL 90 DAY) ,islem_tarihi=NOW(),paketid=?");
$ekle->execute(array($deviceid,$pid));
} else if($pid==3){
$ekle = $db->prepare("INSERT INTO Premium SET deviceid=?,statu=1,vip_date=(NOW() + INTERVAL 180 DAY) ,islem_tarihi=NOW(),paketid=?");
$ekle->execute(array($deviceid,$pid));
} else if($pid==4){
$ekle = $db->prepare("INSERT INTO Premium SET deviceid=?,statu=1,vip_date=(NOW() + INTERVAL 360 DAY) ,islem_tarihi=NOW(),paketid=?");
$ekle->execute(array($deviceid,$pid));
}
if($ekle->rowCount() > 0){
$obje->data = 1;
} else {
$obje->data = 0;
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion



//region PremiumControl
if($_GET["data"]=="PremiumControl"){
$get = $db->prepare("SELECT * FROM Premium WHERE deviceid=? ORDER BY id DESC Limit 1");
$get->execute(array($_POST["deviceid"]));
if($get->rowCount() > 0){
$obje->state = 1;
} else {
$obje->state = 0;
}
echo json_encode($obje,JSON_UNESCAPED_UNICODE);
}
//endregion




}
?>