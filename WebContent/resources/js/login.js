function handleLoginRequest(xhr, status, args) {

    if(args.validationFailed || !args.logged) {
    	PF('BUI').hide();
    }
    else {
    	window.location.replace(args.redirect);
    }

}
