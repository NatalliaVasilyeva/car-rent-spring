const MakeEditableCategory = () => {
    setCategoryInputsReadOnly(false);
    $('#edit-category-button-div').hide();
    $('#edit-manage-category-buttons-div').show();
};

const MakeUnEditableCategory = () => {
    setCategoryInputsReadOnly(true);
    $('#edit-category-button-div').show();
    $('#edit-manage-category-buttons-div').hide();
};


function setCategoryInputsReadOnly(isReadOnly) {
    $('#category-name-input').prop('readonly', isReadOnly);
    $('#category-price-input').prop('readonly', isReadOnly);
}