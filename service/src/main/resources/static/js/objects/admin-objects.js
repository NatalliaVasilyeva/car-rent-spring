$(function () {
    $(".editbrandbtn").click(function () {
        var currentTds = $(this).closest("tr").find("td");
        var id = $(currentTds).eq(1).text();
        var brandName = $.trim((currentTds).eq(2).text().replace(/(\r\n|\n|\r)/gm, ""));

        $('#edit-brand-id').val(id);
        $('#edit-brand-name').val(brandName);
    })
});


$(function () {
    $(".editcategorydbtn").click(function () {
        var currentTds = $(this).closest("tr").find("td");
        var id = $(currentTds).eq(0).text();
        var categoryName = $.trim((currentTds).eq(1).text().replace(/(\r\n|\n|\r)/gm, ""));
        var categoryPrice = $.trim((currentTds).eq(2).text());

        $('#edit-category-id').val(id);
        $('#edit-category-name').val(categoryName);
        $('#edit-category-price').val(categoryPrice);
    })
});

$(function () {
    $(".editmodelbtn").click(function () {
        var currentTds = $(this).closest("tr").find("td");
        var id = $(currentTds).eq(0).text();
        var modelName = $.trim((currentTds).eq(1).text().replace(/(\r\n|\n|\r)/gm, ""));
        var brandName = $.trim((currentTds).eq(2).text().replace(/(\r\n|\n|\r)/gm, ""));
        var transmission = $.trim((currentTds).eq(3).text().replace(/(\r\n|\n|\r)/gm, ""));
        var engineType = $.trim((currentTds).eq(4).text().replace(/(\r\n|\n|\r)/gm, ""));
        var category = $.trim((currentTds).eq(5).text().replace(/(\r\n|\n|\r)/gm, ""));

        $('#edit-model-id').val(id);
        $('#edit-model-name').val(modelName);
        $('#edit-model-brand').val(brandName);
        $('#edit-model-transmission').val(transmission);
        $('#edit-model-engine').val(engineType);
        $('#edit-model-category').val(category);
    })
});