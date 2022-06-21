<?php
$kontrol = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");
if($kontrol == 1){
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
$response = sendMessage($baslik,$icerik);

$warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
$warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
$warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlemi başarılı';
$warning .= '</div>';
echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=bildirim_gonder'>";
}
}

} else {
echo "<meta http-equiv='refresh' content='2;url=index.php'>";
}
?>


<div class="row">
<div class="page-header">
<div class="d-flex align-items-center">
<h2 class="page-header-title">Bildirim Gönder</h2>
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

<div class="widget-body">
<form class="form-horizontal" action="" method="POST">

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-2 form-control-label">Bildirim Başlığı</label>
<div class="col-lg-10">
<input type="text" placeholder="Lütfen bildirim başlığı giriniz" name="baslik" class="form-control" value="<?php echo @$baslik; ?>">
</div>
</div>

<div class="form-group row d-flex align-items-center mb-5">
<label class="col-lg-12 form-control-label">Bildirim Kısa İçerik</label>
<div class="col-lg-12">
<textarea name="data" rows="10" cols="80"  class="form-control"><?php echo @$icerik; ?></textarea>
</div>
</div>



<div class="text-right">
<button type="submit" class="btn btn-success" name="kaydet"><i class="la la-save"></i> Gönder</button>

</div>


</form>
</div>
</div>

</div>
</div>





<?php

function sendMessage($title,$body){
    $content = array(
        "en" => $body
        );
        
        $heading = array(
        "en" => $title
        );
        
    $fields = array(
        'app_id' => "bc63e99e-c64d-4cfe-9047-9de16c13e781",
        'included_segments' => array('Active Users'),
        'data' => array("foo" => "bar"),
        'contents' => $content,
        'headings' => $heading
    );
    
    $fields = json_encode($fields);
    //print("\nJSON sent:\n");
   // print($fields);
    
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json; charset=utf-8',
                                               'Authorization: Basic Zjc3NjNmMjAtNDU0YS00NGRmLWI4OTItMjAwZTA3Mjc2NDUz'));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($ch, CURLOPT_HEADER, FALSE);
    curl_setopt($ch, CURLOPT_POST, TRUE);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);

    $response = curl_exec($ch);
    curl_close($ch);
    
    return $response;
}




?>