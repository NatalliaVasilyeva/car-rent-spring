const CheckFields = () => {
    let login = $("#sign-up-login-input").val();
    let name = $("#sign-up-first-name-input").val();
    let surname = $("#sign-up-surname-input").val();
    let email = $("#sign-up-email-input").val();
    let phone = $("#sign-up-phone-input").val();
    let address = $("#sign-up-address-input").val();
    let birthday = $("#sign-up-birthday-input").val();
    let license_number = $("#sign-up-driver-license-input").val();
    let issue_date = $("#sign-up-issue-date-input").val();
    let expired_date = $("#sign-up-expired-date-input").val();
    let password = $("#sign-up-password-input").val();
    let repeat_password = $("#sign-up-confirm-password-input").val();

    const isValid = validateSignUpForm(login, name, surname, email, phone, address, birthday, license_number, issue_date, expired_date, password, repeat_password);
    if (!isValid) {
        return;
    }
};

function validateSignUpForm(login, name, surname, email, phone, address, birthday, driverLicenseNumber, driverLicenseIssueDate, driverLicenseExpiredDate, password, repeat_password) {
    let errorMessage = {};
    if (!login) {
        errorMessage.login = "Login is empty";
    }
    if (!name) {
        errorMessage.name = "First name is empty";
    }
    if (!surname) {
        errorMessage.surname = "Last name is empty";
    }
    if (!email) {
        errorMessage.email = "Email is empty";
    }
    if (!phone) {
        errorMessage.phone = "Phone number is empty";
    }
    if (!address) {
        errorMessage.address = "Address is empty";
    }
    if (!birthday) {
        errorMessage.birthday = "Birthday is empty";
    }
    if (!driverLicenseNumber) {
        errorMessage.driverLicenseNumber = "Driver license number is empty";
    }
    if (!driverLicenseIssueDate) {
        errorMessage.driverLicenseIssueDate = "Driver license issue date is empty";
    }
    if (!driverLicenseExpiredDate) {
        errorMessage.driverLicenseExpiredDate = "Driver license expire date is empty";
    }
    if (!password) {
        errorMessage.password = "Password is empty";
    }
    if (!repeat_password) {
        errorMessage.repeat_password = "Confirm Password is empty";
    }
    if (password !== repeat_password) {
        errorMessage.password = "Password and Confirm Password do not match";
        errorMessage.repeat_password = "Password and Confirm Password do not match";
    }
    showSignUpErrors(errorMessage.login, errorMessage.name, errorMessage.surname, errorMessage.email, errorMessage.phone,
        errorMessage.address, errorMessage.birthday, errorMessage.driverLicenseNumber, errorMessage.driverLicenseIssueDate, errorMessage.driverLicenseExpiredDate, errorMessage.password, errorMessage.repeat_password);
    return $.isEmptyObject(errorMessage);
}

function showSignUpErrors(loginMsg, nameMsg, surnameMsg, emailMsg, phoneMsg, addressMsg, birthdayMsg, driverNumberMsg, issueDateMsg, expireDateMsg, passwordMsg, repeat_passwordMsg) {
    showOneError($("#sign-up-login-input"), $("#sign-up-login-error-small"), loginMsg);
    showOneError($("#sign-up-first-name-input"), $("#sign-up-first-name-error-small"), nameMsg);
    showOneError($("#sign-up-surname-input"), $("#sign-up-surname-error-small"), surnameMsg);
    showOneError($("#sign-up-email-input"), $("#sign-up-email-error-small"), emailMsg);
    showOneError($("#sign-up-phone-input"), $("#sign-up-phone-error-small"), phoneMsg);
    showOneError($("#sign-up-address-input"), $("#sign-up-address-error-small"), addressMsg);
    showOneError($("#sign-up-birthday-input"), $("#sign-up-birthday-error-small"), birthdayMsg);
    showOneError($("#sign-up-driver-license-input"), $("#sign-up-driver-license-error-small"), driverNumberMsg);
    showOneError($("#sign-up-issue-date-input"), $("#sign-up-issue-date-error-small"), issueDateMsg);
    showOneError($("#sign-up-expired-date-input"), $("#sign-up-expired-date-error-small"), expireDateMsg);
    showOneError($("#sign-up-password-input"), $("#sign-up-password-error-small"), passwordMsg);
    showOneError($("#sign-up-confirm-password-input"), $("#sign-up-confirm-password-error-small"), repeat_passwordMsg);
}

function showOneError(input, error, msg) {
    if (msg) {
        input.addClass("is-invalid");
        error.text(msg);
    } else {
        input.removeClass("is-invalid");
        error.text("");
    }
}

function clearSignUpInput() {
    let logInput = $("#sign-up-login-input");
    let logError = $("#sign-up-login-error-small");
    let nameInput = $("#sign-up-first-name-input");
    let nameError = $("#sign-up-first-name-error-small");
    let lastnameInput = $("#sign-up-surname-input");
    let lastnameError = $("#sign-up-surname-error-small");
    let emailInput = $("#sign-up-email-input");
    let emailError = $("#sign-up-email-error-small");
    let phoneInput = $("#sign-up-phone-input");
    let phoneError = $("#sign-up-phone-error-small");
    let addressInput = $("#sign-up-address-input");
    let addressError = $("#sign-up-address-error-small");
    let birthdayInput = $("#sign-up-birthday-input");
    let birthdayError = $("#sign-up-birthday-error-small");
    let passwordInput = $("#sign-up-password-input");
    let passwordError = $("#sign-up-password-error-small");
    let passwordConfirmInput = $("#sign-up-confirm-password-input");
    let passwordConfirmError = $("#sign-up-confirm-password-error-small");

    logInput.val("");
    nameInput.val("");
    lastnameInput.val("");
    emailInput.val("");
    phoneInput.val("");
    addressInput.val("");
    birthdayInput.val("");
    passwordInput.val("");
    passwordConfirmInput.val("");

    showOneError(logInput, logError, null);
    showOneError(nameInput, nameError, null);
    showOneError(lastnameInput, lastnameError, null);
    showOneError(emailInput, emailError, null);
    showOneError(phoneInput, phoneError, null);
    showOneError(addressInput, addressError, null);
    showOneError(birthdayInput, birthdayError, null);
    showOneError(passwordInput, passwordError, null);
    showOneError(passwordConfirmInput, passwordConfirmError, null);
}