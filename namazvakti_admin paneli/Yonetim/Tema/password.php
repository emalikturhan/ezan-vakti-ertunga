<?php
$kontrol = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
if($kontrol == 1){


if(isset($_POST["guncelle"])){
$pass = $_POST["pass"];
$repass  = $_POST["repass"];
$warning = "";
if(empty($pass) || empty($repass)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız !..';
$warning .= '</div>';
} else {
if($pass == $repass){
$guncelle = $db->prepare("UPDATE Users SET sifre=? WHERE mail=?");
$guncelle->execute(array($class->crypto($pass,"e"),$_SESSION["username"]));
echo "<meta http-equiv='refresh' content='2;url=index.php'>";
} else {
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Şifreler bir biri ile uyuşmuyor !..';
$warning .= '</div>';
}
}
}
?>



<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Şifre Değiştir</h2>
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
<label class="col-lg-2 form-control-label">Yeni Şifre Giriniz</label>
<div class="col-lg-10">
<input type="password" name="pass" class="form-control" value="<?php echo @$pass; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Şifrenizi Tekrarlayınız</label>
<div class="col-lg-10">
<input type="password" name="repass" class="form-control" value="<?php echo @$repass; ?>">
</div>
</div>
<div class="text-right">
<button type="submit" class="btn btn-warning" name="guncelle"><i class="la la-save"></i> Güncelle</button>
</div>


</form>
</div>
</div>

</div>
</div>


<?php
}
else {
 echo '<meta http-equiv="refresh" content="0;URL=index.php" /> '; 
}
?>