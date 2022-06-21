<?php
$kontrol = $class->GetUserID($class->anti($_SESSION["username"]), "yetki");
$site_sahibi = $class->UserControl("sitesahibi", $_SESSION["username"]);
if ($kontrol == 1) {
?>

    <div class="row">
        <div class="modal fade" id="ModalSil" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <form action="" method="POST">
                    <div class="modal-content">
                        <div class="modal-header" style="background: #e91e63; color:#fff; padding:10px;">
                            <h4 class="modal-title beyaz"><i class="icon-trash"></i> LOG Sil</h4>
                        </div>

                        <div class="modal-body">
                            <div class="form" role="form">
                                <div class="form-group">
                                    <label for="sart"><span style="font-size: 1.3em;color: #e91e63;">Uyarı:</span> Log Silinecek !..</label>
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
            $sil = $class->LogDelete($oid);
            if ($sil == "1") {
                $warning .= '<div class="alert alert-success-bordered alert-lg square fade show" role="alert">';
                $warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
                $warning .= '<strong><i class="la la-check" style="font-size:1.5em;"></i></strong> İşlem başarılı ...';
                $warning .= '</div>';
                echo "<meta http-equiv='refresh' content='2;url=index.php?paginal=loglama'>";
            } else {
                $warning .= '<div class="alert alert-danger-bordered alert-lg square fade show" role="alert">';
                $warning .= '<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>';
                $warning .= '<strong><i class="la la-remove" style="font-size:1.5em;"></i></strong> Böyle bir veri bulunmamakta !..';
                $warning .= '</div>';
            }
        }
        ?>


        <div class="page-header">
            <div class="d-flex align-items-center">
                <h2 class="page-header-title">Loglama</h2>
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

                <div class="widget-header bordered no-actions d-flex align-items-center">

                    <h4>Yönetim Paneline Giriş Çıkış Logları</h4>

                </div>

                <div class="widget-body">

                    <div class="table-responsive">

                        <table id="sorting-table" class="table mb-0">

                            <thead>

                                <tr>

                                    <th style="width:20px;">#</th>

                                    <th>Kullanıcı Adı</th>

                                    <th>Giriş Tarihi</th>

                                    <th>IP</th>

                                    <th>Durum</th>

                                    <th style="width:120px; text-align:center;">İşlem</th>

                                </tr>

                            <tbody>

                                <?php

                                $cek = $db->prepare("SELECT * FROM UserLog ORDER BY id DESC Limit 1000");

                                $cek->execute();

                                foreach ($cek as $key) {

                                ?>
                                <tr>
                                    <td style="font-size: 1.5em;"><i class="la la-bug text-red"></i></td>

                                    <td><span class="text-primary"><?php echo $key["mail"]; ?></span></td>

                                    <td><?php echo date("d-m-Y H:i:s", strtotime($key["giris_tarihi"])); ?></td>


                                    <td><span class="text-red"><?php echo $key["ip"]; ?></span></td>

                                    <td>
                                        <?php
                                        if ($key["durum"] == 1) {
                                            echo '<span style="color:green"><i class="icon-checkmark4"></i> Giriş Yapıldı</span>';
                                        } else if ($key["durum"] == 3) {
                                            echo '<span style="color:red"><i class="icon-cross2"></i> Çıkış Yapıldı</span>';
                                        } else if ($key["durum"] == 2) {
                                            echo '<span style="color:grey"><i class="icon-cross2"></i> Hatalı Giriş</span>';
                                        }
                                        ?>
                                    </td>

                                    <td class="td-actions" style="text-align:center;">

                                        <button data-toggle="modal" data-target="#ModalSil" onclick="S('<?php echo $class->crypto($key["id"], "e"); ?>')"><i class="la la-close delete"></i></button>

                                    </td>
                                    </tr>
                                <?php
                                }
                                ?>

                            </tbody>
                            </thead>

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

        function MesajOku(id) {
            document.getElementById("mesajid").value = id;
            var mesaj = document.getElementById(id).getAttribute("data-value");
            $("#mesaj_text").html(mesaj);
        }
    </script>


<?php
} else {
    echo '<meta http-equiv="refresh" content="0;URL=index.php" /> ';
}
?>