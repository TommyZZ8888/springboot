;function alertMsg() {
    console.log('ss')
    $('#notice-box').css('display', 'flex').animate({ right: '20px' },{
        easing: 'swing',
        duration: 1000,
        complete: function () {
            setTimeout(() => {
                $(this).hide().css('right', '-250px')
            }, 10000)
        }
    })
}


