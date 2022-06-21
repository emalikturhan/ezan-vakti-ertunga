<?php
$control = $class->GetUserID($class->anti($_SESSION["username"]), "yetki");
$site_sahibi = $class->GetUserID($class->anti($_SESSION["username"]), "sitesahibi");
if ($control == 1) {
?>

<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Premium Alanlar</h2>
<div>
<div class="page-header-tools">

</div>
</div>
</div>
</div>
</div>



<div class="row">
<div class="col-xl-12">
<?php echo @$warning; ?>
<div class="widget has-shadow">
<div class="widget-header bordered no-actions d-flex align-items-center">
<h4>Premium Listesi</h4>
</div>
<div class="widget-body">
<div class="table-responsive">

<table id="example" class="table mb-0">
<thead>
<tr>
<th>Device Id</th>
<th style="width:200px;">Paket Adı</th>
<th style="width:150px;">İşlem Tarihi</th>
</tr>
</thead>
<tbody>
<?php
$control = $db->prepare("SELECT * FROM Premium WHERE statu=1 and paketid!=0 ORDER BY id DESC");
$control->execute();
foreach($control as $key){
if($key["statu"] == 1){
$statu = "Aktif";
}

if($key["paketid"] == 1){
$pakcage = "1 Aylık VIP";
} else if($key["paketid"] == 2){
$pakcage = "3 Aylık VIP";
} else if($key["paketid"] == 3){
$pakcage = "6 Aylık VIP";
} else if($key["paketid"] == 4){
$pakcage = "1 Yıllık VIP";
}

?>
<tr>
<td><?php echo $key["deviceid"];?></td>
<td><?php echo $pakcage; ?></td>
<td><?php echo $key["islem_tarihi"]; ?></td>
</tr>
<?php
}
?>
</tbody>
</table>

</div>
</div>
</div>

</div>

</div>


<script>
function S(id) {
document.getElementById("silid").value = id;
}
function T(id) {
document.getElementById("checkid").value = id;
}
</script>


<?php
} else {
echo '<meta http-equiv="refresh" content="0;URL=index.php" /> ';
}
?>