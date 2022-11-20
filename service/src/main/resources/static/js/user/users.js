const ChangeRole = (input) => {
    const action = document.getElementById("change-role-form").getAttribute("action").toString();
    const newAction = action.replace(":", input);
    document.getElementById("change-role-form").setAttribute("action", newAction);
};