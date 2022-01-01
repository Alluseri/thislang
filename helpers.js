// These are functions I quickly wrote to generate code :)
// You didn't expect me to write all of it manually, did you?

function simplestHW() {
  var bin = "";
  var t = "Hello, world!";
  for (var x in t) {
      var c = t[x].charCodeAt(0);
      for (var i = 0;i < c;i++) {
          bin += "3";
      }
      bin += "81";
  }
  return bin;
}

function effectiveHW() {
  var bin = "";
  var t = "Hello, world!";
  var last = 0;
  for (var x in t) {
      var c = t[x].charCodeAt(0);
      if (last < c) {
          while (last < c) {
              last++;
              bin += "3";
          }
      } else if (last > c) {
          while (last > c) {
              last--;
              bin += "4";
          }
      }
      last = c;
      bin += "8";
  }
  return bin;
}

function buildLoop(target, index, subtract) {
    var opcode = "7";
    var targetlen = target.toString().length.toString();
    var indexlen = index.toString().length.toString();
    var opcode2 = subtract ? "4" : "3";
    return opcode2 + opcode + targetlen + indexlen + target.toString() + index.toString();
}

function advancedHW() {
  var bin = "";
  var t = "Hello, world!";
  for (var x in t) {
      bin += buildLoop(t[x].charCodeAt(0),bin.length,false) + "81";
  }
  return bin;
}

function advancedEffectiveHW() {
  var bin = "";
  var t = "Hello, world!";
  var last = 0;
  for (var x in t) {
      var cc = t[x].charCodeAt(0);
      if (last == cc) bin += "8";
      else bin += buildLoop(cc,bin.length,cc < last) + "8";
      last = cc;
  }
}

function buildFinisher(target, cval) {
    var build = "";
    if (target > cval) {
        while (target > cval) {
            cval++;
            build += "3";
        }
    } else if (target < cval) {
        while (target < cval) {
            cval--;
            build += "4";
        }
    }
    return build;
}

function bestHW() {
  var bin = "";
  var t = "Hello, world!";
  var last = 0;
  for (var x in t) {
      var cc = t[x].charCodeAt(0);
      if (last == cc) bin += "8";
      else {
          var lp = buildLoop(cc,bin.length,cc < last);
          var fn = buildFinisher(cc,last);
          var shortest = lp.length < fn.length ? lp : fn;
          console.log(cc < last);
          bin += shortest + "8";
      }
      last = cc;
  } 
}
