let form = document.forms[0];

form.addEventListener("submit", function(e) {
	let elements = form.elements;
	let hasErrors = false;
	
	[].forEach.call(elements, element => {
		elements.username.classList.remove("error");
		elements.username.classList.remove("ok");
	});
	
	if (elements.username.value.length < 5) {
		hasErrors = true;
		elements.username.classList.add("error");
	} else {
		elements.username.classList.add("ok");
	}
	
	if (elements.password.value.length < 3) {
		hasErrors = true;
		elements.password.classList.add("error");
	} else {
		elements.username.classList.add("ok");
	}
	
	if (hasErrors) {
		e.preventDefault();
	}
});

form.elements.username.addEventListener("blur", function() {
	if (this.value.length < 5) {
		this.classList.add("error");
	} else {
		this.classList.add("ok");
	}
});

form.elements.password.addEventListener("blur", function() {
	if (this.value.length < 3) {
		this.classList.add("error");
	} else {
		this.classList.add("ok");
	}
});

form.elements.username.addEventListener("focus", function() {
	this.classList.remove("error");
	this.classList.remove("ok");
});

form.elements.password.addEventListener("focus", function() {
	this.classList.remove("error");
	this.classList.remove("ok");
});