(function($) {
  "use strict"; // Start of use strict
  const chartColours = ['rgba(255, 99, 132, 0.2)','rgba(54, 162, 235, 0.2)','rgba(255, 206, 86, 0.2)','rgba(75, 192, 192, 0.2)','rgba(153, 102, 255, 0.2)','rgba(255, 159, 64, 0.2)'];
    const chartConfig = {
                            type: 'bar',
                            data: {
                                labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
                                datasets: [{
                                    label: 'Bookings per day',
                                    data: [12, 19, 3, 5, 2, 3],
                                    backgroundColor: [
                                        'rgba(255, 99, 132, 0.2)',
                                        'rgba(54, 162, 235, 0.2)',
                                        'rgba(255, 206, 86, 0.2)',
                                        'rgba(75, 192, 192, 0.2)',
                                        'rgba(153, 102, 255, 0.2)',
                                        'rgba(255, 159, 64, 0.2)'
                                    ],
                                    borderColor: [
                                        'rgba(255, 99, 132, 1)',
                                        'rgba(54, 162, 235, 1)',
                                        'rgba(255, 206, 86, 1)',
                                        'rgba(75, 192, 192, 1)',
                                        'rgba(153, 102, 255, 1)',
                                        'rgba(255, 159, 64, 1)'
                                    ],
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true
                                    }
                                },
                                responsive: true,
                                maintainAspectRatio: false
                            }
                           };

            var pieChartConfig = {
                       type: 'doughnut',
                       data: {
                         datasets: [{
                         backgroundColor: ['rgba(255, 99, 132, 0.2)','rgba(54, 162, 235, 0.2)','rgba(255, 206, 86, 0.2)','rgba(75, 192, 192, 0.2)','rgba(153, 102, 255, 0.2)','rgba(255, 159, 64, 0.2)'],
                           hoverBorderColor: "rgba(234, 236, 244, 1)",
                         }],
                       },
                       options: {
                         maintainAspectRatio: false,
                         responsive: true,
                         tooltips: {
                           backgroundColor: "rgb(255,255,255)",
                           bodyFontColor: "#858796",
                           borderColor: '#dddfeb',
                           borderWidth: 1,
                           xPadding: 15,
                           yPadding: 15,
                           displayColors: false,
                           caretPadding: 10,
                         },
                         legend: {
                           display: false
                         },
                         cutoutPercentage: 80,
                       },
                     };




  // Toggle the side navigation
  $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    };
  });

 $.get( "/bookings/graph", function( data ) {
    if(data.graph){
      const ctx = document.getElementById('myBarChart').getContext('2d');
      const piectx = document.getElementById('destinationPie').getContext('2d');
      const piectx2 = document.getElementById('sourcePie').getContext('2d');
        var labels = Object.keys(data.graph);
        var chartdata = Object.values(data.graph);
        chartConfig.data.labels= labels;
        chartConfig.data.datasets[0].data = chartdata;
        new Chart(ctx, chartConfig);
        renderBarChart(data['top_destinations'], piectx);
        renderBarChart(data['top_sources'], piectx2);
    }
 });

 function renderBarChart(chartData, ctx){
    var elem = Object.values(chartData);
    var colours = chartColours.slice(0, elem.length);
    console.log(elem);
    console.log(chartData);
    console.log(colours);
    new Chart(ctx,            {
                                              type: 'doughnut',
                                              data: {
                                                labels: Object.keys(chartData),
                                                datasets: [{
                                                data: elem,
                                                backgroundColor: colours,
                                                hoverBorderColor: "rgba(234, 236, 244, 1)",
                                                }],
                                              },
                                              options: {
                                                maintainAspectRatio: false,
                                                responsive: true,
                                                tooltips: {
                                                  backgroundColor: "rgb(255,255,255)",
                                                  bodyFontColor: "#858796",
                                                  borderColor: '#dddfeb',
                                                  borderWidth: 1,
                                                  xPadding: 15,
                                                  yPadding: 15,
                                                  displayColors: false,
                                                  caretPadding: 10,
                                                },
                                                legend: {
                                                  display: false
                                                },
                                                cutoutPercentage: 80,
                                              },
                                            });
 }


  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function() {
    if ($(window).width() < 768) {
      $('.sidebar .collapse').collapse('hide');
    };
    
    // Toggle the side navigation when window is resized below 480px
    if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
      var e0 = e.originalEvent,
        delta = e0.wheelDelta || -e0.detail;
      this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });

})(jQuery); // End of use strict
