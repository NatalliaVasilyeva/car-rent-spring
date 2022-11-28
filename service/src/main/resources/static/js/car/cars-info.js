$(function () {
    $(".seecarbtn").click(function () {

        let target = $(this).parent().parent().children('div').eq(0);
        let image = $(this).parent().parent().children('img').attr('src');
        let carId = $(target).children().eq(0).text();
        let brand = $(target).children().eq(1).text();
        let model = $(target).children().eq(2).text();
        let color = $(target).children().eq(3).text();
        let yearOfProduction = $(target).children().eq(4).text();
        let transmission = $(target).children().eq(5).text();
        let engineType = $(target).children().eq(6).text();
        let category = $(target).children().eq(10).text();
        let pricePerDay = $(target).children().eq(11).text();

        $('#see-car-img').attr('src', image);
        $('#see-car-id').val(carId);
        $('#see-car-brand').val(brand);
        $('#see-car-model').val(model);
        $('#see-car-color').val(color);
        $('#see-car-yearOfProduction').val(yearOfProduction);
        $('#see-car-transmission').val(transmission);
        $('#see-car-engineType').val(engineType);
        $('#see-car-category').val(category);
        $('#see-car-pricePerDay').val(pricePerDay).replace(/(\$)/gm, "");
    })
});