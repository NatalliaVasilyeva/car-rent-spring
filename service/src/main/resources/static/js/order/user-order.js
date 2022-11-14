$(function () {
    $(".cancelledbtn").click(function () {
        const currentTds = $(this).closest("tr").find("td");
        const orderId = $(currentTds).eq(0).text();

        $.post("user-cancel-order",
            {
                orderId: orderId,
            }
            // function(){
            //     location.reload();
            )
    })
});

$(function () {
    $(".editbtn").click(function () {
        var currentTds = $(this).closest("tr").find("td");
        var id = $(currentTds).eq(0).text();
        var date = $.trim((currentTds).eq(1).text().replace(/(\r\n|\n|\r)/gm, ""));
        var status = $.trim((currentTds).eq(2).text().replace(/(\r\n|\n|\r)/gm, ""));
        var description = $(currentTds).eq(3).text();
        var startRentalDate = $.trim((currentTds).eq(4).text().replace(/(\r\n|\n|\r)/gm, ""));
        var endRentalDate = $.trim((currentTds).eq(5).text().replace(/(\r\n|\n|\r)/gm, ""));
        var insurance = $(currentTds).eq(6).text();
        var accident = $(currentTds).eq(7).text();
        var sum = $(currentTds).eq(8).text();


        $('#edit-order-id').val(id);
        $('#edit-date').val(date);
        $('#edit-status').val(status);
        $('#edit-description').val(description);
        $('#edit-start-date').val(startRentalDate);
        $('#edit-end-date').val(endRentalDate);
        $('#edit-ensurance').val(insurance);
        $('#edit-accident').val(accident);
        $('#edit-sum').val(sum);
    })
});

$(function () {
    $(".showorderform").click(function () {
        $("#see-car-modal").modal('hide');
        let carId = $("#see-car-id").val();

        $('#create-order-car-id').val(carId);
    })
});