<?php
$control = $class->GetUserID($class->anti($_SESSION["username"]), "yetki");
$site_sahibi = $class->GetUserID($class->anti($_SESSION["username"]), "sitesahibi");
if ($control == 1) {
?>
    <div class="row">
        <div class="page-header">
            <div class="d-flex align-items-center">
                <h2 class="page-header-title">Kullanıcılar</h2>
                <div>
                    <div class="page-header-tools">
                        <a href="index.php?paginal=kullanici-ayarlari" class="btn btn-gradient-01">Yeni Kullanıcı Ekle</a>
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
                    <h4 class="modal-title beyaz"><i class="icon-trash"></i> Kullanıcı Sil</h4>
                </div>
                <div class="modal-body">
                    <div class="form" role="form">
                        <div class="form-group">
                            <label for="sart"><span style="font-size: 1.3em;color: #e91e63;">Uyarı:</span> Kullanıcı Silinecek !..</label>
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
if (isset($_POST["sil"])) {
    $oid = $class->crypto($_POST["silid"], "d");
    $sil = $class->RemoveAdmin($oid);
    if ($sil == "1") {
        echo "<meta http-equiv='refresh' content='0;url=index.php?paginal=kullanicilar'>";
    }
}
?>


<div class="col-xl-12">
    <div class="widget has-shadow">
        <div class="widget-header bordered no-actions d-flex align-items-center">
            <h4>Tanımlı Kullanıcıların Listesi</h4>
        </div>
        <div class="widget-body">
            <div class="table-responsive">

                <table id="sorting-table" class="table mb-0">
                    <thead>
                        <tr>
                            <th style="width:20px;">#</th>
                            <th>İsim</th>
                            <th>Mail</th>
                            <th style="width:200px;">Telefon</th>
                            <th style="width:250px;">Kayıt Tarihi</th>
                            <th style="width:120px;">İşlem</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        $cek = $db->prepare("SELECT * FROM Users WHERE sitesahibi != 1");
                        $cek->execute();
                        foreach ($cek as $key) {
                        ?>
                            <tr>
                                <td style="font-size: 1.5em;"><i class="la la-user"></i></td>
                                <td><?php echo $key["isim"]; ?></td>
                                <td><?php echo $key["mail"]; ?></td>
                                <td><?php echo $key["telefon"]; ?></td>
                                <td><?php echo $key["tarih"]; ?></td>
                                <?php
                                if ($site_sahibi != "1" || $_SESSION["username"] == "admin") {
                                ?>
                                    <td>
                                        <span style="color:#a20000">Yetkin Yok</span>
                                    </td>
                                <?php
                                } else {
                                ?>
                                    <td class="td-actions" style="text-align:center;">
                                        <a href="index.php?paginal=kullanici-ayarlari&token=<?php echo $class->crypto($key["id"], "e"); ?>"><i class="la la-edit edit"></i></a>
                                        <button data-toggle="modal" data-target="#ModalSil" onclick="S('<?php echo $class->crypto($key["id"], "e"); ?>')"><i class="la la-close delete"></i></button>
                                    </td>
                                <?php
                                }
                                ?>


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
        function S(id) {
            document.getElementById("silid").value = id;
        }
    </script>
<?php
} else {
    echo '<meta http-equiv="refresh" content="0;URL=index.php" /> ';
}
?>


















