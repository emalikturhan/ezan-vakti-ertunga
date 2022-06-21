<?php

$kontrol = $class->GetUserID($class->anti($_SESSION["username"]),"yetki");

if($kontrol == 1){

?>



<div class="row">

<div class="page-header">

<div class="d-flex align-items-center">

<h2 class="page-header-title">Dualar</h2>

<div>

<div class="page-header-tools">

<a href="index.php?paginal=dua_detay" class="btn btn-gradient-01">Yeni Dua Ekle</a>

</div>

</div>

</div>

</div>

</div>











<div class="row">

<div class="modal fade" id="ModalSil" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

<div class="modal-dialog modal-sm">

<form action="" method="POST">

<div class="modal-content">

<div class="modal-header" style="background: #e91e63; color:#fff; padding:10px;">

<h4 class="modal-title beyaz"><i class="icon-trash"></i> Dua Sil</h4>

</div>



<div class="modal-body">   

<div class="form" role="form">

<div class="form-group">

<label for="sart"><span style="font-size: 1.3em;color: #e91e63;">Uyarı:</span> Dua Silinecek !..</label>

<input type="hidden" name="silid" id="silid" />

</div>

</div>

</div>

<div class="modal-footer">

<button type="submit" name="sil" class="btn btn-danger"><i class="la la-check"></i> Sil</button>

<button type="button" class="btn btn-shadow" data-dismiss="modal"><i class="la la-remove"></i> İptal</button>

</div>

</div>

</form>

</div>

</div>







<?php

if(isset($_POST["sil"])){

$oid = $class->crypto($_POST["silid"],"d"); 

$sil = $db->prepare("DELETE FROM Dualar WHERE id=?");

$sil->execute(array($oid));

if($sil->rowCount() > 0){

echo "<meta http-equiv='refresh' content='0;url=index.php?paginal=dualar'>";

}

}

?>











<div class="col-xl-12">

<div class="widget has-shadow">

<div class="widget-header bordered no-actions d-flex align-items-center">

<h4>Dua Listesi</h4>

</div>



<div class="widget-body">

<div class="table-responsive">



<table id="example" class="table mb-0">

<thead>

<tr>

<th style="width:20px;">#</th>

<th>Dua Adı</th>

<th style="width:140px; text-align:center;">İşlem</th>

</tr>

</thead>

<tbody>

<?php

$cek = $db->prepare("SELECT * FROM Dualar Order By id Desc");

$cek->execute();

foreach ($cek as $key) {

?>

<tr>

<td style="width:20px; font-size: 1.5em;"><i class="la la-file-text-o"></i></td>

<td><?php echo $key["baslik"]; ?></td>

<td class="td-actions" style="width:140px; text-align:center;">

<a href="index.php?paginal=dua_detay&id=<?php echo $class->crypto($key["id"],"e");?>"><i class="la la-edit edit"></i></a>

<button data-toggle="modal" data-target="#ModalSil" onclick="S('<?php echo $class->crypto($key["id"],"e"); ?>')"><i class="la la-close delete"></i></button>

</td>

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

function S(id){

document.getElementById("silid").value = id;

}





</script>

<?php

}

else {

 echo '<meta http-equiv="refresh" content="0;URL=index.php" /> '; 

}

?>