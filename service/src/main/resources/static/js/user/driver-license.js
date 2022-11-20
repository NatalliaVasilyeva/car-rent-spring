const MakeEditableDetails = () => {
    setLicenseInputsReadOnly(false);
    $('#edit-driver-license-details-button-div').hide();
    $('#edit-manage-driver-license-buttons-div').show();
};

const MakeUnEditableDetails = () => {
    setLicenseInputsReadOnly(true);
    $('#edit-driver-license-details-button-div').show();
    $('#edit-manage-driver-license-buttons-div').hide();
};


function setLicenseInputsReadOnly(isReadOnly) {
    $('#driver-license-driverLicenseNumber-input').prop('readonly', isReadOnly);
    $('#driver-license-driverLicenseIssueDate-input').prop('readonly', isReadOnly);
    $('#driver-license-driverLicenseExpiredDate-input').prop('readonly', isReadOnly);
}

const ShowSearchByUserIdForm = () => {
    const x = document.getElementById("search-user-id-form");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

const ShowSearchByNumberForm = () => {
    const x = document.getElementById("search-number-form");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}