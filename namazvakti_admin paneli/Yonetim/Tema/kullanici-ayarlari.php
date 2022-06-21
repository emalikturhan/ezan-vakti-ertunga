<?php
$control = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
@$id = $_GET["token"];
if(empty($id)){ 
$statu = 0; 
} else { 
$statu = 1; 
$id = $class->crypto($id,"d");
$getuser = $db->prepare("SELECT * FROM Users WHERE id=?");
$getuser->execute(array($id));
foreach($getuser as $row){
$name  = $row["isim"];
$email = $row["mail"];
$pass  = $class->crypto($row["sifre"],"d");
$tel   = $row["telefon"];
@$menu = $row["menu"];
}
}

if($control == 1){
if(isset($_POST["kaydet"])){
$name  = $_POST["name"];
$pass  = $_POST["pass"];
$email = $_POST["mail"];
$tel   = $_POST["tel"];
@$menu = $_POST["menu"];
$warning = "";
$mlist = "";
if(empty($name) || empty($pass) || empty($email) || empty($tel)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız';
$warning .= '</div>';
} else {
if($class->email_control($email) == false){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen egeçerli bir mail adresi giriniz';
$warning .= '</div>';
} else { 
if(empty($menu)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen en az 1 menü seçiniz !..';
$warning .= '</div>';
} else {
foreach($menu as $key){
$mlist .= $key.",";
}
$ekle = $class->AddAdmin($class->anti($name),$class->anti($class->crypto($pass,"e")),$class->anti($email),$class->anti($tel),$mlist);
if($ekle == "0"){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Bu bilgilerde kullanıcı kaydı bulunmakta';
$warning .= '</div>';
} else if($ekle == "1"){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlem başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=kullanicilar'>";
}
}
}
}
}


if(isset($_POST["guncelle"])){
$name  = $_POST["name"];
$email = $_POST["mail"];
$tel   = $_POST["tel"];
$pass  = $_POST["pass"];

@$menu = $_POST["menu"];
$warning = "";
$mlist = "";
if(empty($name) || empty($pass) || empty($email) || empty($tel)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen boş alan bırakmayınız';
$warning .= '</div>';
} else {
if($class->email_control($email) == false){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen egeçerli bir mail adresi giriniz';
$warning .= '</div>';
} else { 
if(empty($menu)){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Lütfen en az 1 menü seçiniz';
$warning .= '</div>';
} else {
foreach($menu as $key){
$mlist .= $key.",";
}
$ekle = $class->UpdateAdmin($class->anti($name),$class->anti($class->crypto($pass,"e")),$class->anti($email),$class->anti($tel),$mlist,$id);
if($ekle == "0"){
$warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Bu mail ile başka kullanıcı bulunmakta';
$warning .= '</div>';
} else if($ekle == "1"){
$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlem başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=kullanicilar'>";
}
}
}
}
}

?>
<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Kullanıcı Ayarları</h2>
<div>

<div class="page-header-tools">
<a href="index.php?paginal=kullanicilar" class="btn btn-gradient-03">Kullanıcı Listesi</a>
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
if($statu == 0){
?>
<h4>Yeni Kullanıcı Oluştur</h4>
<?php
} else {
?>
<h4>Kullanıcı Güncelle</h4>
<?php
}
?>
</div>

<div class="widget-body">
<div class="widget-body sliding-tabs">
<form class="form-horizontal" action="" method="POST">

<ul class="nav nav-tabs" id="example-one" role="tablist">
<li class="nav-item"><a class="nav-link active" id="base-tab-1" data-toggle="tab" href="#tab-1" role="tab" aria-controls="tab-1" aria-selected="true">Kullanıcı Bilgileri</a></li>
<li class="nav-item"><a class="nav-link" id="base-tab-2" data-toggle="tab" href="#tab-2" role="tab" aria-controls="tab-2" aria-selected="false">İzinler</a></li>
</ul>    

<div class="tab-content pt-3">
<div class="tab-pane fade show active" id="tab-1" role="tabpanel" aria-labelledby="base-tab-1">


<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Ad Soyad</label>
<div class="col-lg-10">
<input type="text" class="form-control" placeholder="Ad Soyad Giriniz" name="name" value="<?php echo @$name; ?>" require>
</div>
</div>


<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">E-Mail</label>
<div class="col-lg-10">
<input type="mail" class="form-control" placeholder="Mail Giriniz" name="mail" value="<?php echo @$email; ?>" require>
</div>
</div>



<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Şifre</label>
<div class="col-lg-10">
<input type="password" class="form-control" placeholder="Şifre Giriniz" name="pass" value="<?php echo @$pass; ?>" require>
</div>
</div>



<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Telefon</label>
<div class="col-lg-10">
<input type="number" class="form-control" placeholder="Telefon Giriniz" name="tel" value="<?php echo @$tel; ?>" require>
</div>
</div>


</div>



<div class="tab-pane fade" id="tab-2" role="tabpanel" aria-labelledby="base-tab-2">

<?php
if($statu == 0){
$getmenu = $db->prepare("SELECT * FROM yonetim_menuler");
$getmenu->execute();
foreach($getmenu as $key){
?>
<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label"><?php echo $key["menu"]; ?></label>
<div class="col-lg-10">
<input type="checkbox" data-toggle="toggle" name="menu[]" value="<?php echo $key["id"];?>">
</div>
</div>

<?php    
}
} else {
$getmenu = $db->prepare("SELECT * FROM yonetim_menuler");
$getmenu->execute();
$menus = explode(",",$menu);
foreach($getmenu as $key){
if(in_array($key["id"],$menus,TRUE)){
$check = "checked";    
} else {
$check = "";       
}
?>
<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label"><?php echo $key["menu"]; ?></label>
<div class="col-lg-10">
<input type="checkbox" <?php echo @$check; ?> data-toggle="toggle" name="menu[]" value="<?php echo $key["id"];?>">
</div>
</div>
<?php
}
}
?>

</div>

<div class="text-right">
<?php
if($statu == 0){
?>
<button type="submit" class="btn btn-success" name="kaydet"><i class="la la-save"></i> Kaydet</button>
<?php
} else {
?>
<button type="submit" class="btn btn-warning" name="guncelle"><i class="la la-save"></i> Güncelle</button>
<?php
}
?>
</div>





</div>

</form>

</div>

</div>

</div>



</div>

</div>



<?php

}

else {

 echo '<meta http-equiv="refresh" content="0;URL=index.php" /> '; 

}

?>s