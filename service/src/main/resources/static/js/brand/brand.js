const MakeEditableBrands = () => {
    setBrandInputsReadOnly(false);
    $('#edit-brand-button-div').hide();
    $('#edit-manage-brand-buttons-div').show();
};

const MakeUnEditableBrands = () => {
    setLicenseInputsReadOnly(true);
    $('#edit-brand-button-div').show();
    $('#edit-manage-brand-buttons-div').hide();
};


function setBrandInputsReadOnly(isReadOnly) {
    $('#brand-name-input').prop('readonly', isReadOnly);
}

const ShowSearchByNameForm = () => {
    const x = document.getElementById("search-name-form");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

const ShowSearchByNamesForm = () => {
    const x = document.getElementById("search-names-form");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}