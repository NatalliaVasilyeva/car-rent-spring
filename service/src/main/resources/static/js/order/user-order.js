const MakeEditableOrder = () => {
    setOrderInputsReadOnly(false);
    $('#edit-order-button-div').hide();
    $('#cancelled-car-button-div').hide();
    $('#edit-manage-order-buttons-div').show();
    $('#date').hide();
    $('#user').hide();
    $('#status').hide();
    $('#sum').hide();
};

const MakeUnEditableOrder = () => {
    setOrderInputsReadOnly(true);
    $('#edit-order-button-div').show();
    $('#cancelled-car-button-div').show();
    $('#edit-manage-order-buttons-div').hide();
    $('#date').show();
    $('#user').show();
    $('#status').show();
    $('#sum').show();
};


function setOrderInputsReadOnly(isReadOnly) {
    $('#order-date-input').prop('readonly', isReadOnly);
    $('#order-start-date-input').prop('readonly', isReadOnly);
    $('#order-end-date-input').prop('readonly', isReadOnly);
    $('#order-insurance-input').prop('readonly', isReadOnly);
}

$(function () {
    $(".showorderform").click(function () {
        $("#see-car-modal").modal('hide');
        let carId = $("#see-car-id").val();

        $('#create-order-car-id').val(carId);
    })
});