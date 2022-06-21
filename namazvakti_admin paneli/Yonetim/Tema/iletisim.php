<?php
$control = $class->GetUserID($class->anti($_SESSION["username"]), "yetki");
if ($control == 1) {

?>


    <div class="row">
        <div class="page-header">
            <div class="d-flex align-items-center">
                <h2 class="page-header-title">iletişim</h2>
                <div>
                    <div class="page-header-tools">
                    <form action="" method="post">
                       <button type="submit" class="btn btn-gradient-03" name="tumunu_sil">Tüm Mesajları Sil</a>
                       </form>
                    </div>
                </div>
            </div>
        </div>
    </div>






    <div class="modal fade" id="ModalSil" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

<div class="modal-dialog modal-sm">

<form action="" method="POST">

<div class="modal-content">

<div class="modal-header" style="background: #e91e63; color:#fff; padding:10px;">

<h4 class="modal-title beyaz"><i class="la la-remove"></i> MESAJ SİL</h4>

</div>

<div class="modal-body">   

<div class="form" role="form">

<div class="form-group">

<label for="sart"><span style="font-size: 1.3em;color: #e91e63;">Uyarı:</span> Mesaj Silinecek !..</label>

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
$sil = $db->prepare("DELETE FROM Iletisim WHERE id=?");
$sil->execute(array($oid));
}

if(isset($_POST["tumunu_sil"])){
$tumunu_sil = $db->prepare("DELETE FROM Iletisim");
$tumunu_sil->execute();
}

?>



    <div class="row">






        <div class="col-xl-12">

            <?php echo @$warning; ?>

            <div class="widget has-shadow">

                <div class="widget-header bordered no-actions d-flex align-items-center">

                    <h4>Site Üzerinden Gelen Mesajlar</h4>

                </div>

                <div class="widget-body">

                    <div class="table-responsive">



                        <table id="sorting-table" class="table mb-0">

                            <thead>

                                <tr>

                                    <th style="width:20px;">#</th>

                                    <th>Adı Soyadı</th>

                                    <th>Tarih</th>

                                    <th>Saat</th>

                                    <th>Mesaj</th>

                                    <th style="width:120px; text-align:center;">İşlem</th>  

                                </tr>

                            </thead>

                            <tbody>

                                <?php

                                $cek = $db->prepare("SELECT * FROM Iletisim");

                                $cek->execute();

                                foreach ($cek as $key) {

                                ?>

                                    <tr>

                                        <td style="font-size: 1.5em;"><i class="la la-envelope text-red"></i></td>

                                        <td><span class="text-primary"><?php echo $key["isim"]; ?></span></td>

                                        <td><?php echo date("d-m-Y", strtotime($key["kayit_tarihi"])); ?></td>

                                        <td><?php echo date("H:i:s", strtotime($key["kayit_tarihi"])); ?></td>

                                        <td><?php echo $key["msg"]; ?></td>

                                        <td class="td-actions" style="text-align:center;">

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

} else {

    echo '<meta http-equiv="refresh" content="0;URL=index.php" /> ';
}

?>