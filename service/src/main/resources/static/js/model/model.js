const MakeEditableModel = () => {
    setModelInputsReadOnly(false);
    $('#edit-model-button-div').hide();
    $('#edit-manage-model-buttons-div').show();
    $('#id').hide();
    $('#brand').hide();
};

const MakeUnEditableModel = () => {
    setModelInputsReadOnly(true);
    $('#edit-model-button-div').show();
    $('#edit-manage-model-buttons-div').hide();
    $('#id').show();
    $('#brand').show();
};


function setModelInputsReadOnly(isReadOnly) {
    $('#model-name-input').prop('readonly', isReadOnly);
    $('#model-transmission-input').prop('readonly', isReadOnly);
    $('#model-engine-type-input').prop('readonly', isReadOnly);
}