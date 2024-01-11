(function(w,d,s,r,k,h,m){
	if(w.performance && w.performance.timing && w.performance.navigation) {
		w[r] = w[r] || function(){(w[r].q = w[r].q || []).push(arguments)};
		h=d.createElement('script');h.async=true;h.setAttribute('src',s+k);
		d.getElementsByTagName('head')[0].appendChild(h);
		(m = window.onerror),(window.onerror = function (b, c, d, f, g) {
		m && m(b, c, d, f, g),g || (g = new Error(b)),(w[r].q = w[r].q || []).push(["captureException",g]);})
	}
})(window,document,'//static.site24x7rum.com/beacon/site24x7rum-min.js?appKey=','s247r','296fdede5bab1bde63cf229da14d46b7');
try{
	var workerEnvironments = {"remote workers":["Liam","John Doe","Emma"], "on-premises workers":["Ross","Henry","Benjamin"], "warehouse workers":["William","Elijah","Olivia"], "accounting workers":["Sophia","Harper","James"], "logistics systems":["Ava","Isabella","Charlotte"]}
	var keys = Object.keys(workerEnvironments);
	var randomWorkerEnvironment = keys[Math.floor(Math.random() * keys.length)];
	s247r('environment',randomWorkerEnvironment);

	var users = workerEnvironments[randomWorkerEnvironment];
	var randomUser = users[Math.floor((Math.random() * users.length))];
	s247r('userId',randomUser);
}catch(e){
	s247r('captureException',e);
}

