const MakeEditableInfo = () => {
        setInfoInputsReadOnly(false);
        $('#edit-user-info-button-div').hide();
        $('#edit-manage-user-info-buttons-div').show();
    };

const MakeEditableDetails = () => {
    setDetailsInputsReadOnly(false);
    $('#edit-user-details-button-div').hide();
    $('#edit-manage-user-details-buttons-div').show();
};

const MakeEditableLicenses = () => {
    setLicenseInputsReadOnly(false);
    $('#edit-license-button-div').hide();
    $('#edit-manage-license-buttons-div').show();
};

const MakeUnEditableInfo = () => {
    setInfoInputsReadOnly(true);
    $('#edit-user-info-button-div').show();
    $('#edit-manage-user-info-buttons-div').hide();
};

const MakeUnEditableDetails = () => {
    setDetailsInputsReadOnly(true);
    $('#edit-user-details-button-div').show();
    $('#edit-manage-user-details-buttons-div').hide();
};

const MakeUnEditableLicenses = () => {
    setLicenseInputsReadOnly(true);
    $('#edit-license-button-div').show();
    $('#edit-manage-license-buttons-div').hide();
};

function setInfoInputsReadOnly(isReadOnly) {
    $('#user-info-email-input').prop('readonly', isReadOnly);
    $('#user-info-login-input').prop('readonly', isReadOnly);

}

function setDetailsInputsReadOnly(isReadOnly) {
    $('#user-details-name-input').prop('readonly', isReadOnly);
    $('#user-details-surname-input').prop('readonly', isReadOnly);
    $('#user-details-address-input').prop('readonly', isReadOnly);
    $('#user-details-phone-input').prop('readonly', isReadOnly);
    $('#user-details-birthday-input').prop('readonly', isReadOnly);

}

function setLicenseInputsReadOnly(isReadOnly) {
    $('#user-license-name-input').prop('readonly', isReadOnly);
    $('#user-license-issue-date-input').prop('readonly', isReadOnly);
    $('#user-license-expired-date-input').prop('readonly', isReadOnly);
}