console.log("JS included successfully");

const toggleSideBar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

const confirmDelete = (cId) => {
	swal({
		title: "Are you sure?",
		text: "Once deleted, you will not be able to recover this contact!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				/*swal("Contact has been deleted successfully", {
					icon: "success",
				});*/
				window.location = "/smartcontact/user/delete/" + cId;
			} else {
				swal("Your contact is safe!");
			}
		});
}

const search = () => {

	let query = $("#search-input").val();
	let url = `http://localhost:8080/smartcontact/contact/search/${query}`;
	if (query == "") {
		$(".search-result").hide();
	}
	else {
		fetch(url).then(contacts => {
			console.log(query);
			return contacts.json();
		}).then(data => {
			let text = `<div class='list-group'>`;
			
			data.forEach((contact) => {
				text += `<a href='/smartcontact/user/contactDetail/${contact.cId}' 
						class='list-group-item list-group-item-action'> ${contact.name} </a>`;
			})
			
			text += `</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
		});
	}
}