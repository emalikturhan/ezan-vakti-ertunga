<?php
require_once 'simple_html_dom.php';
include('../Yonetim/config.php');
header('Content-Type: text/html; charset=utf-8');
class NamazClass{

public function AylikNamaz($ilceid){
$db = getDB();
$obje->data = array();
$ch = curl_init("https://namazvakitleri.diyanet.gov.tr/tr-TR/".$ilceid);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_ENCODING, 'UTF-8');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
$output = curl_exec($ch);
curl_close($ch);

$html_base = new simple_html_dom();
$html_base->load($output);
$hicri  = trim(htmlentities(strip_tags($html_base->find('div[class=ti-hicri]')[0])));
$div_aylik = $html_base->find('table[class=table vakit-table] tbody tr');
$div_kible = $html_base->find('div[class=today-day-info-container]')[0];
$kible = trim(htmlentities(strip_tags($div_kible->find('div[class=tpt-time]')[0])));


for($i=7; $i<=sizeof($div_aylik)-1; $i++){
$dizi = array();
$td = $div_aylik[$i]->find('td');
foreach($td as $key){
array_push($dizi,$key);
}


$zaman  = htmlentities(strip_tags($dizi[0]));
$imsak  = trim(htmlentities(strip_tags($dizi[1])));
$gunes  = trim(htmlentities(strip_tags($dizi[2])));
$oglen  = trim(htmlentities(strip_tags($dizi[3])));
$ikindi = trim(htmlentities(strip_tags($dizi[4])));
$aksam  = trim(htmlentities(strip_tags($dizi[5])));
$yatsi  = trim(htmlentities(strip_tags($dizi[6])));

$parcala = explode(" ",$zaman);
if(strlen($parcala[3]) > 15){
$guntxt = "Çarşamba";
} else {
$guntxt = $parcala[3];
}

$gethadis = $db->query("SELECT * FROM Hadisler ORDER BY RAND() Limit 1")->fetch(PDO::FETCH_ASSOC);
$hadis = $gethadis["data"];
$hadis_adi = $gethadis["baslik"];

$getdua = $db->query("SELECT * FROM Dualar ORDER BY RAND() Limit 1")->fetch(PDO::FETCH_ASSOC);
$dua = $getdua["data"];
$dua_adi = $getdua["baslik"];

if($parcala["1"] == "Eyl&amp;#252;l"){
$ay = "Eylül";    
} else {
$ay = $parcala["1"];     
}

array_push($obje->data,array('gun'=>$parcala[0],'ay'=>$ay,'yil'=>$parcala[2],'imsak'=>$imsak,'gunes'=>$gunes,'oglen'=>$oglen,'ikindi'=>$ikindi,'aksam'=>$aksam,'yatsi'=>$yatsi,'hicri'=>$hicri,'gun_txt'=>$guntxt,'kible'=>$kible,'hadis'=>$hadis,'hadis_adi'=>$hadis_adi,'dua'=>$dua,'dua_adi'=>$dua_adi));
}
return  $obje->data;
}





}

?>