if (false || typeof window.console === "undefined") {
	window.console = {
		log : function() {
		}
	}
}

var b3 = {
	version : "0.1.0",

	add : function() {
		var p1 = new b3.Tree();
		p1.treeAdd();
	}
}

b3.Tree = function() {
	this.map = {};
};

b3.Tree.prototype = {
	tree : function() {
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "./MainServ?c");
		xhr.onload = function() {
			var treeDoubleAry = eval(xhr.response);
			console.log(treeDoubleAry);
		};
		xhr.send();
	},

	treeAdd : function() {

		var $div = $("#tree"); // 親要素取得
		var $jqObj; // 子要素
		for (var i = 0; i < 5; i++) {
			// jQueryオブジェクトを作成し、CSSクラス、CSSプロパティ、id属性を付与する
			$jqObj = $("<div/>").attr("data-role", "collapsible").attr("id",
					"id" + (i + 1));
			$jqObj.append($("<h2/>").text('地震').append(
					$("<ul/>").attr("data-role", "listview").append(
							$("<li/>").text("aaa"))));
			// jQueryオブジェクトを子要素として追加する
			$div.append($jqObj);
		}
	}
}
function tree() {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "./MainServ?c");
	xhr.onload = function() {
		var treeDoubleAry = eval(xhr.response);
		console.log(treeDoubleAry);
	};
	xhr.send();
}
function threadList(c, n) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "./MainServ?c=" + c + "&t=" + n);
	xhr.onload = function() {
		var treeDoubleAry = eval(xhr.response);
		console.log(treeDoubleAry);
	};
	xhr.send();
}
function thread(url) {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "./MainServ?l=" + url);
	xhr.onload = function() {
		document.getElementById("thread").innerHTML = xhr.response;

	};
	xhr.send();
}
function r_p(ank, res) {
	var div = document.getElementById("r_" + res);
	ank.onclick = null;
	ank.innerHTML = div.innerHTML;
	ank.className = "r_s";
}
function onload() {
	var useragent = navigator.userAgent;
	var isMobileDevice = useragent.indexOf("iPhone") !== -1
			|| useragent.indexOf('Android') !== -1;

	if (isMobileDevice) {

	} else {

	}
}