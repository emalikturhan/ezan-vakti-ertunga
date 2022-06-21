
<script src="assets/vendors/js/base/core.min.js"></script>
<!-- End Vendor Js -->
<!-- Begin Page Vendor Js -->
<script src="assets/vendors/js/nicescroll/nicescroll.min.js"></script>
<script src="assets/vendors/js/app/app.min.js"></script>


<script src="assets/js/toggle.js"></script>
<script src="assets/vendors/js/datatables/datatables.min.js"></script>
<script src="assets/vendors/js/datatables/dataTables.buttons.min.js"></script>
<script src="assets/js/components/tables/tables.js"></script>
<script src="https://cdn.ckeditor.com/4.14.0/standard/ckeditor.js"></script>

<script>
$(document).ready(function() {
    $('#example').DataTable();

    CKEDITOR.replace( 'sayfa_icerik', {
height: 400
});

} );
</script>

