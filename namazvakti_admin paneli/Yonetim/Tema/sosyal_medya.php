<?php
$kontrol = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
if($kontrol == 1){

$anahtar = 1; 
$facebook = "";
$twitter  = "";
$instagram = "";
$cek = $db->prepare("SELECT * FROM SosyalMedya");
$cek->execute();
foreach ($cek as $row) {
$facebook = $row["facebook"];
$twitter  = $row["twitter"];
$instagram = $row["instagram"];
}



if(isset($_POST["kaydet"])){
$facebook = $_POST["facebook"];
$twitter  = $_POST["twitter"];
$instagram = $_POST["instagram"];
$warning = "";

$ekle = $db->prepare("INSERT INTO SosyalMedya SET facebook=?,twitter=?,instagram=?");
$ekle->execute(array($facebook,$twitter,$instagram));
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=sosyal_medya'>";
}


if(isset($_POST["guncelle"])){
$facebook = $_POST["facebook"];
$twitter  = $_POST["twitter"];
$instagram = $_POST["instagram"];
$warning = "";
$guncelle = $db->prepare("UPDATE SosyalMedya SET facebook=?,twitter=?,instagram=?");
$guncelle->execute(array($facebook,$twitter,$instagram));
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=sosyal_medya'>";
}
?>



<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Sosyal Medya</h2>
<div>
</div>
</div>
</div>
</div>





<div class="row">
<div class="col-xl-12">
<?php echo @$warning; ?>

<div class="widget has-shadow">

<div class="widget-body">
<form class="form-horizontal" action="" method="POST">

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Facebook</label>
<div class="col-lg-10">
<input type="text" name="facebook" class="form-control" value="<?php echo @$facebook; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Twitter</label>
<div class="col-lg-10">
<input type="text" name="twitter" class="form-control" value="<?php echo @$twitter; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Instagram</label>
<div class="col-lg-10">
<input type="text" name="instagram" class="form-control" value="<?php echo @$instagram; ?>">
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