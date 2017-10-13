<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>

<body>
<h2>Hello World!</h2>
<br>
<pre id='output'>
javascript testing output :
</pre>

<script>
var o = function(s) {
	console.log("append w/o new line : " + s);
	var html = $('#output').html();
	$('#output').html( html + s);
}

var p = function(s) {
	console.log("append new line : " + s);
	o(s + "\n");
}

p("hello, world");
o("hello again");
p(", world");

</script>
</body>
</html>
