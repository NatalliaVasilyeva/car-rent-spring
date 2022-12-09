const MakeEditableAccident = () => {
    setAccidentInputsReadOnly(false);
    $('#edit-accident-button-div').hide();
    $('#edit-manage-accident-buttons-div').show();
    $('#date').hide();
    $('#brand').hide();
    $('#model').hide();
    $('#carNumber').hide();
    $('#userFirstName').hide();
    $('#userLastName').hide();
};

const MakeUnEditableAccident = () => {
    setAccidentInputsReadOnly(true);
    $('#edit-accident-button-div').show();
    $('#edit-manage-accident-buttons-div').hide();
    $('#date').show();
    $('#brand').show();
    $('#model').show();
    $('#carNumber').show();
    $('#userFirstName').show();
    $('#userLastName').show();
};


function setAccidentInputsReadOnly(isReadOnly) {
    $('#accident-description-input').prop('readonly', isReadOnly);
    $('#accident-damage-input').prop('readonly', isReadOnly);
}