<?php
$control = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
if($control == 1){
$cek = $db->prepare("SELECT * FROM DiniBilgiler ORDER BY id DESC LIMIT 1");
$cek->execute();
foreach ($cek as $key) {
$data = $key["kelimei_sehadet"];
}

if(isset($_POST["kaydet"])){
$sv_data = $_POST["data"];
$warning = "";
if(empty($sv_data)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız';
$warning .= '</div>';
} else {
$up = $db->prepare("UPDATE DiniBilgiler SET kelimei_sehadet=?");
$up->execute(array($sv_data));
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlem başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=kelimei_sehadet'>";
}
}

?>

<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Kelime-i Şehadet</h2>
<div>
</div>
</div>
</div>
</div>





<div class="row">
<div class="col-xl-12">
<?php echo @$warning; ?>
<div class="widget has-shadow">
<div class="widget-header bordered no-actions d-flex align-items-center">
<h4>Kelime-i Şehadet Yazısı</h4>
</div>

<div class="widget-body">
<form class="form-horizontal" action="" method="POST">

<div class="form-group row d-flex align-items-center mb-5">
<div class="col-lg-12">
<textarea name="data" id="terms_en" rows="10" cols="80"  class="ckeditor"><?php echo @$data; ?></textarea>
</div>
</div>


<div class="text-right">
<button type="submit" class="btn btn-warning" name="kaydet"><i class="la la-save"></i> Güncelle</button>
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