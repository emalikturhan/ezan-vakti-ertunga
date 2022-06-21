<?php
$kontrol = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
if($kontrol == 1){

if(isset($_REQUEST["id"])){
@$id = $_REQUEST["id"];
if(empty($_REQUEST["id"])){ 
$anahtar = 0; 
} else { 
$anahtar = 1; 
$id = $class->crypto($id,"d");
$baslik = "";
$icerik = "";
$cek = $db->prepare("SELECT * FROM Zikirler WHERE id =:id");
$cek-> bindParam(":id",$id,PDO::PARAM_STR);
$cek->execute();
foreach ($cek as $row) {
 $baslik = $row["baslik"];
 $meali  = $row["meali"];
 $arapca = $row["arapca"];
 $icerik = $row["okunusu"];
 $adet   = $row["adet"];
}
}
}

if(isset($_POST["kaydet"])){
$baslik = $_POST["baslik"];
$meali  = $_POST["meali"];
$arapca = $_POST["arapca"];
$adet   = $_POST["adet"];
$icerik = $_POST["data"]; 
$warning = "";
if(empty($baslik) || empty($icerik) || empty($meali) || empty($arapca) || empty($adet)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız !..';
$warning .= '</div>';
} else {
$ekle = $db->prepare("INSERT INTO Zikirler SET baslik=?,okunusu=?,meali=?,arapca=?,adet=?");
$ekle->execute(array($baslik,$icerik,$meali,$arapca,$adet));
if($ekle->rowCount() > 0){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlemi başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=zikirler'>";
}
}
}


if(isset($_POST["guncelle"])){
$baslik   = $_POST["baslik"];
$meali    = $_POST["meali"];
$arapca   = $_POST["arapca"];
$adet     = $_POST["adet"];
$icerik      = $_POST["data"]; 
$warning = "";
if(empty($baslik) || empty($icerik)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız !..';
$warning .= '</div>';
} else {
$guncelle = $db->prepare("UPDATE Zikirler SET baslik=?,okunusu=?,meali=?,arapca=?,adet=? WHERE id=?");
$guncelle->execute(array($baslik,$icerik,$meali,$arapca,$adet,$id));
if($guncelle->rowCount() > 0){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlemi başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=zikirler'>";
}
}
}
?>



<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Zikirler</h2>
<div>
<div class="page-header-tools">
<a href="index.php?paginal=zikirler" class="btn btn-gradient-03">Zikirler Listesi</a>
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
<?php
if($anahtar == 1){
?>
<h4>Zikir Güncelle</h4>
<?php
} else {
?>
<h4>Yeni Zikir Ekle</h4>
<?php
}
?>
</div>

<div class="widget-body">
<form class="form-horizontal" action="" method="POST">

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Zikir Adı</label>
<div class="col-lg-10">
<input type="text" placeholder="Lütfen namaz duası adı giriniz" name="baslik" class="form-control" value="<?php echo @$baslik; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Okunuşu</label>
<div class="col-lg-10">
<textarea name="data" rows="10" cols="80"  class="form-control"><?php echo @$icerik; ?></textarea>
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Meali</label>
<div class="col-lg-10">
<textarea name="meali" rows="10" cols="80"  class="form-control"><?php echo @$meali; ?></textarea>
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Arapça</label>
<div class="col-lg-10">
<textarea name="arapca" rows="10" cols="80"  class="form-control"><?php echo @$arapca; ?></textarea>
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Zikir Adeti</label>
<div class="col-lg-10">
<input type="text" placeholder="Lütfen zikirin adetini giriniz" name="adet" class="form-control" value="<?php echo @$adet; ?>">
</div>
</div>

<div class="text-right">
<?php
if($anahtar == 1){
?>
<button type="submit" class="btn btn-warning" name="guncelle"><i class="la la-save"></i> Güncelle</button>
<?php
} else {
?>
<button type="submit" class="btn btn-success" name="kaydet"><i class="la la-save"></i> Kaydet</button>
<?php
}
?>

</div>


</form>
</div>
</div>

</div>
</div>

<script type="text/javascript">
CKEDITOR.replace( 'deneme', {
height: 400
});
</script>

<?php
}
else {
 echo '<meta http-equiv="refresh" content="0;URL=index.php" /> '; 
}
?>