if (typeof window.console === "undefined") {
	window.console = {
		log : function() {
		}
	}
}

var b3 = {
	version : "0.1.0",
	tab : {},
	height : 0,
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
		b3.height = h;
		$('#TreeTab').height(h);
		$('#ListTab').height(h);
		$('#ThreadTab').height(h);

		$("#scrollDown").tap(b3.scrollDown);
		$("#scrollUp").tap(b3.scrollUp);
		$("#scrollDown").taphold(b3.scrollDownHold);
		$("#scrollUp").taphold(b3.scrollUpHold);

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
	},

	scrollDown : function() {
		b3.tab.scrollDown();
	},
	scrollUp : function() {
		b3.tab.scrollUp();
	},
	scrollDownHold : function() {
		b3.tab.scrollDownHold();
	},
	scrollUpHold : function() {
		b3.tab.scrollUpHold();
	}
}

b3.Tabs = function(id) {
	this.activeId = id;
	this.tabTops = [];
	this.tabTops[this.activeId] = 0;
}
b3.Tabs.prototype = {
	scrollDown : function() {
		var pos = $('#' + this.activeId).scrollTop() + b3.height;
		$('#' + this.activeId).scrollTop(pos);
		this.tabTops[this.activeId] = $('#' + this.activeId).scrollTop();
	},

	scrollUp : function() {
		var pos = $('#' + this.activeId).scrollTop() - b3.height;
		$('#' + this.activeId).scrollTop(pos);
		this.tabTops[this.activeId] = $('#' + this.activeId).scrollTop();
	},
	scrollDownHold : function() {
		while (true) {
			var posOrg = this.tabTops[this.activeId];
			var pos = posOrg + $('#' + this.activeId).height();
			$('#' + this.activeId).scrollTop(pos);
			this.tabTops[this.activeId] = $('#' + this.activeId).scrollTop();

			if (posOrg === $('#' + this.activeId).scrollTop()) {
				break;
			}
		}
	},

	scrollUpHold : function() {
		while (true) {
			var posOrg = this.tabTops[this.activeId];
			var pos = posOrg - $('#' + this.activeId).height();
			$('#' + this.activeId).scrollTop(pos);
			this.tabTops[this.activeId] = $('#' + this.activeId).scrollTop();
			if (posOrg === 0) {
				break;
			}
		}
	},
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
