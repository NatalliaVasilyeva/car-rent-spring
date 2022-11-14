$(function () {
    $(".seecarbtn").click(function () {

        let target = $(this).parent().parent().children('div').eq(0);
        let image = $(this).parent().parent().children('img').attr('src');
        let carId = $(target).children().eq(0).text();
        let brand = $(target).children().eq(1).text();
        let model = $(target).children().eq(2).text();
        let transmission = $(target).children().eq(3).text();
        let engineType = $(target).children().eq(4).text();
        let color = $(target).children().eq(5).text();
        let yearOfProduction = $(target).children().eq(6).text();
        let pricePerDay = $(target).children().eq(8).text();

        $('#see-car-img').attr('src', image);
        $('#see-car-id').val(carId);
        $('#see-car-brand').val(brand);
        $('#see-car-model').val(model);
        $('#see-car-transmission').val(transmission);
        $('#see-car-engineType').val(engineType);
        $('#see-car-color').val(color);
        $('#see-car-yearOfProduction').val(yearOfProduction);
        $('#see-car-pricePerDay').val(pricePerDay).replace(/(\$)/gm, "");
    })
});

$(function () {
    $(".editcarbtn").click(function () {
        let target = $(this).parent().parent().children('div').eq(0);
        let image = $(this).parent().parent().children('img').attr('src');
        let carId = $(target).children().eq(0).text();
        let brand = $(target).children().eq(1).text();
        let model = $(target).children().eq(2).text();
        let color = $(target).children().eq(3).text();
        let yearOfProduction = $(target).children().eq(4).text();
        let carNumber = $(target).children().eq(5).text();
        let vin = $(target).children().eq(6).text();
        let isRepaired = $(target).children().eq(7).text();
        let category = $(target).children().eq(8).text();

        $('#edit-car-img').attr('src', image);
        $('#edit-car-id-admin').val(carId);
        $('#edit-car-brand-admin').val(brand);
        $('#edit-car-model-admin').val(model);
        $('#edit-car-color-admin').val(color);
        $('#edit-car-year-admin').val(yearOfProduction);
        $('#edit-car-number-admin').val(carNumber);
        $('#edit-car-vin-admin').val(vin);
        $('#edit-car-repaired-admin').val(isRepaired);
        $('#edit-car-category-admin').val(category);
    })
});

$(function () {
    $(".deletebtn").click(function () {
        let target = $(this).parent().parent().children('div').eq(0);
        let carId = $(target).children().eq(0).text();
        console.log(carId)

        $('#delete-car-id-admin').val(carId);
    })
});