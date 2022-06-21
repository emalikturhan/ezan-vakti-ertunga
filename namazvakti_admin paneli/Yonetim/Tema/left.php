<div class="default-sidebar">
<nav class="side-navbar box-scroll sidebar-scroll">
<ul class="list-unstyled">
<?php
$site_sahibi = $class->UserControl("sitesahibi",$_SESSION["username"]);
$menu = $class->UserControl("menu",$_SESSION["username"]);
$menucek = $db->prepare("SELECT * FROM yonetim_menuler ORDER BY id ASC");
$menucek->execute();
$menus = explode(",",$menu);
if($site_sahibi == 1){
foreach($menucek as $key){
if($key["sub_menu"] == 0){  
?>
<li><a href="index.php?paginal=<?php echo $key["sayfa"];?>"><i class="<?php echo $key["icon"];?>"></i><span><?php echo $key["menu"];?></span></a></li>   
<?php
} else {
$altmenucek = $db->prepare("SELECT * FROM yonetim_sub_menuler WHERE sub_menu_id=?");
$altmenucek->execute(array($key["id"]));
?>
<li><a href="#dropdown-<?php echo $key["id"]; ?>" aria-expanded="false" data-toggle="collapse"><i class="<?php echo $key["icon"];?>"></i><span><?php echo $key["menu"]; ?></span></a>
<ul id="dropdown-<?php echo $key["id"]; ?>" class="collapse list-unstyled pt-0">
<?php
foreach($altmenucek as $altmenu){
?>
<li><a href="index.php?paginal=<?php echo $altmenu["sayfa"];?>"><?php echo $altmenu["menu"]; ?></a></li>
<?php
}
?>
</ul>
</li>
<?php
}
}    
} else {
foreach($menucek as $key){
if(in_array($key["id"],$menus,TRUE)){
if($key["sub_menu"] == 0){    
?>
<li><a href="index.php?paginal=<?php echo $key["sayfa"];?>"><i class="<?php echo $key["icon"];?>"></i><span><?php echo $key["menu"];?></span></a></li>   
<?php
} else {
$altmenucek = $db->prepare("SELECT * FROM yonetim_sub_menuler WHERE sub_menu_id=?");
$altmenucek->execute(array($key["id"]));
?>
<li><a href="#dropdown-<?php echo $key["id"]; ?>" aria-expanded="false" data-toggle="collapse"><i class="<?php echo $key["icon"];?>"></i><span><?php echo $key["menu"]; ?></span></a>
<ul id="dropdown-<?php echo $key["id"]; ?>" class="collapse list-unstyled pt-0">
<?php
foreach($altmenucek as $altmenu){
?>
<li><a href="index.php?paginal=<?php echo $altmenu["sayfa"];?>"><?php echo $altmenu["menu"]; ?></a></li>
<?php
}
?>
</ul>
</li>
<?php
}
} else {
echo "";    
}
}
}
?>



</ul>
</nav>
</div>