<?php ob_start();

if (session_status() !== PHP_SESSION_ACTIVE) {
session_start();
}

if (isset($_SESSION["username"])) {

include("config.php");

include("Class.php");

$db = getDB();

$class = new NamazClass();

$yetki = $class->GetUserID($class->anti($_SESSION["username"]), "yetki");

if (($yetki == 1) || ($yetki == 2)) {

?>

<html lang="en">
        <?php include("Tema/head.php"); ?>

        <body id="page-top">
            <!-- Begin Preloader -->
            <div id="preloader">
                <div class="canvas">
                    <img src="assets/img/logo.png" alt="logo" class="loader-logo">
                    <div class="spinner"></div>
                </div>
            </div>

            <div class="page">
                <?php include("Tema/header.php"); ?>

                <div class="page-content d-flex align-items-stretch">
                    <?php include("Tema/left.php"); ?>
                    <div class="content-inner">
                        <div class="container-fluid">
                            <?php

                            if (isset($_GET["paginal"])) {
                                if ($_GET["paginal"] == "") {
                                    if ($yetki == "1" || $yetki = "2") {
                                        $app = "Tema/namaz_sureleri.php";
                                    } else {
                                        header("Location: ../index.php?paginal=404");
                                    }
                                } else {
                                    $app = "Tema/" . $_GET["paginal"] . ".php";
                                }
                            } else {
                                if ($yetki == "1" || $yetki = "2") {
                                    $app = "Tema/namaz_sureleri.php";
                                } else {
                                    header("Location: ../index.php?paginal=404");
                                }
                            }

                            if (file_exists($app)) {

                                include($app);
                            } else {

                                header("Location: ../index.php?paginal=404");
                            }

                            ?>
                        </div>

                        <?php include("Tema/footer.php"); ?>
                        <a href="#" class="go-top"><i class="la la-arrow-up"></i></a>
                    </div>
                </div>
            </div>

            <?php include("Tema/script.php"); ?>
        </body>

        </html>
        
<?php
} else {
header("Location: ../index.php?paginal=404");
}
} else {
echo '<meta http-equiv="refresh" content="0;URL=login.php" /> ';
}
ob_end_flush();
?>