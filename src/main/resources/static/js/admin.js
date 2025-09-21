document.querySelectorAll(".dropdown-item").forEach(function(item) {
    item.addEventListener("click", function() {
        // Get the data attributes from the clicked button and the parent row
        const row = this.closest("tr");
        const userId = row.getAttribute("data");
        const action = this.getAttribute("data-action");

        if(action === "tickets"){
            window.location.href = "/tickets?id="+userId;
        }
        else if(action == "delete"){
            window.location.href = "/admin/delete/user/"+userId
        }
    });
});