const MakeEditableCar = () => {
    setCarInputsReadOnly(false);
    $('#edit-car-button-div').hide();
    $('#edit-manage-car-buttons-div').show();
    $('#image').show();
    $('#old_image').hide();
    $('#transmission').hide();
    $('#engineType').hide();
    $('#vin').hide();
    $('#price').hide();
};

const MakeUnEditableCar = () => {
    setCarInputsReadOnly(true);
    $('#edit-car-button-div').show();
    $('#edit-manage-car-buttons-div').hide();
    $('#image').hide();
    $('#transmission').show();
    $('#engineType').show();
    $('#vin').show();
    $('#price').show();
};


function setCarInputsReadOnly(isReadOnly) {
    $('#car-model-input').prop('readonly', isReadOnly);
    $('#car-category-input').prop('readonly', isReadOnly);
    $('#car-color-input').prop('readonly', isReadOnly);
    $('#car-year-input').prop('readonly', isReadOnly);
    $('#car-number-input').prop('readonly', isReadOnly);
    $('#car-repaired-input').prop('readonly', isReadOnly);
}