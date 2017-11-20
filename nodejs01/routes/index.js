
/*
 * GET home page.
 */

var ms01 = Object.keys(require('module')._cache);
var ms02 = Object.keys(require.cache);  // # lists full filenames of loaded non-core modules

console.log("ms01 : " + ms01);
console.log("ms02 : " + ms02);

exports.index = function(req, res){
  res.render('index', { title: 'Express',
	  modules01 : ms01.toString().replace(/,/g, '\n'),
	  modules02 : ms02.toString().replace(/,/g, '\n'),
	  });
};