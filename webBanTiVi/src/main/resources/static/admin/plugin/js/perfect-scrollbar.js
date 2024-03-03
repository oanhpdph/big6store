/**
 * Perfect Scrollbar
 */
'use strict';

document.addEventListener('DOMContentLoaded', function () {
    (function () {
        const scroll = document.getElementById('scroll'),
            scroll1 = document.getElementById('accordionOne'),
            horizontalExample = document.getElementById('horizontal-example'),
            horizVertExample = document.getElementById('both-scrollbars-example');

        // Vertical Example
        // --------------------------------------------------------------------
        if (scroll) {
            new PerfectScrollbar(scroll, {
                wheelPropagation: true
            });
        }
        if (scroll1) {
            new PerfectScrollbar(scroll1, {
                wheelPropagation: false
            });
        }


        // Horizontal Example
        // --------------------------------------------------------------------
        if (horizontalExample) {
            new PerfectScrollbar(horizontalExample, {
                wheelPropagation: false,
                suppressScrollY: true
            });
        }

        // Both vertical and Horizontal Example
        // --------------------------------------------------------------------
        if (horizVertExample) {
            new PerfectScrollbar(horizVertExample, {
                wheelPropagation: false
            });
        }
    })();
});
