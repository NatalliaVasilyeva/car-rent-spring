const MakeEditableDetails = () => {
    setDetailsInputsReadOnly(false);
    $('#edit-user-details-button-div').hide();
    $('#edit-manage-user-details-buttons-div').show();
};

const MakeUnEditableDetails = () => {
    setDetailsInputsReadOnly(true);
    $('#edit-user-details-button-div').show();
    $('#edit-manage-user-details-buttons-div').hide();
};


function setDetailsInputsReadOnly(isReadOnly) {
    $('#user-details-name-input').prop('readonly', isReadOnly);
    $('#user-details-surname-input').prop('readonly', isReadOnly);
    $('#user-details-address-input').prop('readonly', isReadOnly);
    $('#user-details-phone-input').prop('readonly', isReadOnly);
    $('#user-details-birthday-input').prop('readonly', isReadOnly);

}

const ShowSearchDatesForm = () => {
    const x = document.getElementById("search-dates-form");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}