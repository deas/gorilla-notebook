// comment this out once :infer-externs true is in place
var Mousetrap = {};
Mousetrap.bindGlobal = function() {};

var MathJax = {};
MathJax.Hub.Config = function (cfg) {};
MathJax.Hub.Configured = function() {};
MathJax.Hub.Queue = function(item) {};

// Node/Webpack externs for advanced optimization - bare minimum
// externs inferences should be there soon
// cljsjs/vega has more
var vg = {};
vg.parse.spec = function (spec, callback) {};

