
<?php require_once("config.php"); require_once("Class.php"); ?><!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Yönetim Paneli</title>
<meta name="description" content="Elisyam is a Web App and Admin Dashboard Template built with Bootstrap 4">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Google Fonts -->
<script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js"></script>
<script>
WebFont.load({
google: {"families":["Montserrat:400,500,600,700","Noto+Sans:400,700"]},
active: function() {
sessionStorage.fonts = true;
}
});
</script>
<!-- Favicon -->
<link rel="apple-touch-icon" sizes="180x180" href="assets/img/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="assets/img/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="assets/img/favicon-16x16.png">

<link rel="stylesheet" href="assets/vendors/css/base/bootstrap.min.css">
<link rel="stylesheet" href="assets/vendors/css/base/elisyam-1.5.min.css">
</head>
<body class="bg-white">

<div id="preloader">
<div class="canvas">
<img src="assets/img/logo.png" alt="logo" class="loader-logo">
<div class="spinner"></div>   
</div>
</div>

        <!-- End Preloader -->
        <!-- Begin Container -->
<div class="container-fluid no-padding h-100">
<div class="row flex-row h-100 bg-white">

<div class="col-xl-8 col-lg-6 col-md-5 no-padding">
<div class="elisyam-bg background-01">
<div class="elisyam-overlay overlay-01"></div>
<div class="authentication-col-content mx-auto">
<h1 class="gradient-text-01">
Yönetim Paneline Hoş Geldiniz
</h1>
<span class="description">

</span>
</div>
</div>
</div>
               
<!-- Begin Right Content -->
<div class="col-xl-4 col-lg-6 col-md-7 my-auto no-padding">
<!-- Begin Form -->
<div class="authentication-form mx-auto">
<div class="logo-centered">
<a href="db-default.html">
<img src="assets/img/logo.png" alt="logo">
</a>
</div>
<h3>Giriş Paneli</h3>
<form action="" method="POST">
<div class="group material-input">
<input type="text" name="username" required >
<span class="highlight"></span>
<span class="bar"></span>
<label>Email</label>
</div>
<div class="group material-input">
<input type="password" name="password" required>
<span class="highlight"></span>
<span class="bar"></span>
<label>Şifre</label>
</div>


<div class="row">
<div class="col text-right">

</div>
</div>

<div class="sign-btn text-center">
<button type="submit" name="login_btn" class="btn btn-lg btn-gradient-01">Giriş</button>
</div>
</form>
<br>
<?php 
if(session_status()!=PHP_SESSION_ACTIVE); @session_start();
$ip = $_SERVER["REMOTE_ADDR"];
$class = new NamazClass(); 
if(isset($_POST["login_btn"])){
$mail = $class->anti($_POST["username"]);       
$pass = $class->crypto($_POST["password"],'e'); 
if(empty($mail) || empty($pass)){
echo '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
echo '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
echo '<strong>Uyarı:</strong> Lütfen boş alan bırakmayınız !..';
echo '</div>';
} else {
$login =$class->login($mail,$pass);
if($login == "1"){
echo '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
echo '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
echo '<strong>Başarılı:</strong> Yönlendiriliyorsunuz lütfen bekleyiniz ..';
echo '</div>';
$log = $class->UserLog($mail,"1");  
$_SESSION["username"] = $mail;
echo '<meta http-equiv="refresh" content="2;URL=index.php" /> ';
} else {
echo '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
echo '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
echo '<strong>Uyarı:</strong> Kullanıcı adı veya şifre hatalı !..';
echo '</div>'; 
$log = $class->UserLog($mail,"2");      
}
}
}


?>  

</div>
<!-- End Form -->                        
</div>
<!-- End Right Content -->
</div>
<!-- End Row -->
</div>

<script src="assets/vendors/js/base/jquery.min.js"></script>
<script src="assets/vendors/js/base/core.min.js"></script>
<script src="assets/vendors/js/app/app.min.js"></script>

</body>
</html>