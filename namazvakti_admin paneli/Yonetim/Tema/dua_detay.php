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
$cek = $db->prepare("SELECT * FROM Dualar WHERE id =:id");
$cek-> bindParam(":id",$id,PDO::PARAM_STR);
$cek->execute();
foreach ($cek as $row) {
 $baslik = $row["baslik"];
 $icerik = $row["data"];
}
}
}

if(isset($_POST["kaydet"])){
$baslik      = $_POST["baslik"];
$icerik      = $_POST["data"]; 
$warning = "";
if(empty($baslik) || empty($icerik)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız !..';
$warning .= '</div>';
} else {
$ekle = $db->prepare("INSERT INTO Dualar SET baslik=?,data=?");
$ekle->execute(array($baslik,$icerik));
if($ekle->rowCount() > 0){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlemi başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=dualar'>";
}
}
}


if(isset($_POST["guncelle"])){
$baslik      = $_POST["baslik"];
$icerik      = $_POST["data"]; 
$warning = "";
if(empty($baslik) || empty($icerik)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız !..';
$warning .= '</div>';
} else {
$guncelle = $db->prepare("UPDATE Dualar SET baslik=?,data=? WHERE id=?");
$guncelle->execute(array($baslik,$icerik,$id));
if($guncelle->rowCount() > 0){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlemi başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=dualar'>";
}
}
}
?>



<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Dualar</h2>
<div>
<div class="page-header-tools">
<a href="index.php?paginal=dualar" class="btn btn-gradient-03">Dualar Listesi</a>
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
<h4>Dua Güncelle</h4>
<?php
} else {
?>
<h4>Yeni Dua Ekle</h4>
<?php
}
?>
</div>

<div class="widget-body">
<form class="form-horizontal" action="" method="POST">

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Dua Adı</label>
<div class="col-lg-10">
<input type="text" placeholder="Lütfen namaz duası adı giriniz" name="baslik" class="form-control" value="<?php echo @$baslik; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-12 form-control-label">Dua İçeriği</label>
<div class="col-lg-12">
<textarea name="data" class="form-control" rows="10" cols="80"><?php echo @$icerik; ?></textarea>
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
CKEDITOR.replace( 'sayfa_icerik', {
height: 400
});
</script>

<?php
}
else {
 echo '<meta http-equiv="refresh" content="0;URL=index.php" /> '; 
}
?>