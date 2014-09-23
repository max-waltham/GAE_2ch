if (typeof window.console === "undefined") {
	window.console = {
		log : function() {
		}
	}
}

var b3 = {
	version : "0.1.0",
	tab : {},
	currentDat : '',
	loadingObj : {
		text : "Loading...",
		textVisible : true,
		theme : "z",
		html : ""
	},
	bodyOnload : function() {
		$.mobile.loading("show", b3.loadingObj);
		var h = $(window).height() - 40;
		$('#TreeTab').height(h);
		$('#ListTab').height(h);
		$('#ThreadTab').height(h)
		$("#tabs").tabs("option", "active", 0);
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "./tree_i.jsp");
		xhr.onload = function() {
			$.mobile.loading("hide");
			$("#TreeTab").html(xhr.response).trigger("create");
		};
		xhr.send();
		b3.tab = new b3.Tabs("TreeTab");
	},

	active : function(id) {
		b3.tab.active(id);
	},

	listUp : function(c, t) {
		$.mobile.loading("show", b3.loadingObj);
		b3.tab.active("ListTab");
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "./thread_i.jsp?c=" + c + "&t=" + t);
		xhr.onload = function() {
			$.mobile.loading("hide");
			$("#ListTab").html(xhr.response);

		};
		xhr.send();
	},

	getThread : function(dat) {
		if (dat == b3.currentDat) {
			b3.tab.active("ThreadTab", false);
			return;
		}
		b3.tab.active("ThreadTab", true);
		$.mobile.loading("show", b3.loadingObj);
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "./threadContents_i.jsp?dat=" + dat);
		xhr.onload = function() {
			$.mobile.loading("hide");
			$("#ThreadTab").html(xhr.response);
		};
		xhr.send();
		b3.currentDat = dat;
	}

}

b3.Tabs = function(id) {
	this.activeId = id;
	this.tabTops = [];
}
b3.Tabs.prototype = {
	active : function(id, top) {
		if (this.activeId === id) {
			// console.log(this.activeId, id)
			return;
		}
		this.tabTops[this.activeId] = $('#' + this.activeId).scrollTop();
		$('#' + this.activeId).css('display', 'none');
		$('#' + this.activeId + 'B').removeClass('ui-btn-active');
		$('#' + id + 'B').addClass('ui-btn-active');
		$('#' + id).css('display', '');
		if (!top) {
			$('#' + id).scrollTop(this.tabTops[id]);
		}
		this.activeId = id;
	},

}

function r_p(ank, res) {
	var div = document.getElementById("r_" + res);
	ank.onclick = null;
	ank.innerHTML = div.innerHTML;
	ank.className = "r_s";
}
