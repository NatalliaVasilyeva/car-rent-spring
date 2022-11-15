function signOut() {
    $.post("sign-out",
        function () {
            $("#welcome-form").submit()
        }
    )
}