$(function () {
    $(".addaccidentbtn").click(function () {
        var currentTds = $(this).closest("tr").find("td");
        var id = $(currentTds).eq(0).text();
        var startRentalDate = $.trim((currentTds).eq(4).text().replace(/(\r\n|\n|\r)/gm, ""));
        var endRentalDate = $.trim((currentTds).eq(5).text().replace(/(\r\n|\n|\r)/gm, ""));

        $('#add-accident-order-id').val(id);
        $('#add-accident-order-start-id').val(startRentalDate);
        $('#add-accident-order-end-id').val(endRentalDate);
    })
});