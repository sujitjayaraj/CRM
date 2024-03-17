document.addEventListener('DOMContentLoaded', function() {
    "use strict";

    const treeViewMenu = document.querySelector('.app-menu');

    // Toggle Sidebar
    document.querySelectorAll('[data-toggle="sidebar"]').forEach(function(element) {
        element.addEventListener('click', function(event) {
            event.preventDefault();
            document.querySelector('.app').classList.toggle('sidenav-toggled');
        });
    });

    // Activate sidebar treeview toggle
    document.querySelectorAll("[data-toggle='treeview']").forEach(function(element) {
        element.addEventListener('click', function(event) {
            event.preventDefault();
            const parent = element.parentElement;
            const isExpanded = parent.classList.contains('is-expanded');
            if(isExpanded) {
                treeViewMenu.querySelectorAll("[data-toggle='treeview']").forEach(function(element) {
                    element.parentElement.classList.remove('is-expanded');
                });
            }
            parent.classList.toggle('is-expanded');
        });
    });

    // Set initial active toggle
    document.querySelectorAll("[data-toggle='treeview'].is-expanded").forEach(function(element) {
        element.parentElement.classList.toggle('is-expanded');
    });

    // Activate Bootstrap tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll("[data-toggle='tooltip']"));
    const tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});