angular.module("lc.atwho", []).directive("lcAtwho", ["$parse", "$filter", "emojis", "$rootScope", "bus", "$translate", "lcData", "LinkModuleService", "$http", "$q", function(a, e, t, n, i, s, l, r, o, c) {
	return {
		require: "^ngModel",
		scope: !0,
		link: function(l, o, c) {
			var d = a(c.atwhoKeydown),
				m = {
					emojis: !0,
					member: !0,
					channel: !0,
					entity: !0
				};
			c.lcAtwho && (m = $.extend(m, l.$eval(c.lcAtwho))), m.emojis && o.atwho({
				at: ":",
				data: t,
				show_the_at: !0,
				tpl: "<li data-value=':${name}:'>" + "<img width='24' height='24' src='" + appGlobal.config.cdnRoot + "image/emoji/${name}.png' class='emoji' /> ${name}</li>",
				callbacks: {
					keydown: function(a, e) {
						d && !e && l.$apply(function() {
							d(l, {
								$event: a
							})
						})
					}
				}
			}), m.entity && r.bindAtWho(o, c.channels && m.channel), c.atwhoMembers && m.member && l.$watch(c.atwhoMembers, function(a, t) {
				var l = _.reject(a, function(a) {
					return a.uid === n.global.me.uid
				});
				if (l = i.member.filterBot(l, !0), _.isEmpty(l)) return void o.atwho({
					at: "@",
					data: []
				});
				_.forEach(l, function(a) {
					0 === a.state ? a.lc_state = '<span class="fa fa-circle lc-state offline"></span>' : 1 === a.state ? a.lc_state = '<span class="fa fa-circle lc-state online"></span>' : 2 === a.state ? a.lc_state = '<span class="fa fa-minus-circle lc-state busy"></span>' : 3 === a.state && (a.lc_state = '<span class="fa fa-dot-circle-o lc-state away"></span>')
				}), c.hasGroup && l.push({
					name: "group",
					display_name: s.instant("atwho.GROUP"),
					display_name_pinyin: "all"
				});
				var r = ["<li data-value='@${name} '>", "<div title='${display_name}' class='lc-avatar lc-avatar-30'>", "<span class='lc-avatar-def m-l-10 m-r-5' style='background-color:${bgColor};'><div>${shortName}</div></span></div>&nbsp;", "${display_name}", " <small>(${name})</small>", "</li>"].join(""),
					d = '<li data-value="@${display_name} " title="@${display_name}" class="channel">@${display_name} <small>(' + s.instant("atwho.NOTICE_ALL") + ")</small></li>";
				o.atwho({
					at: "@",
					limit: 1e4,
					header_tpl: '<div class="header">' + s.instant("atwho.MEMBER") + "<small>↑&nbsp;↓&nbsp;" + s.instant("atwho.NAVIGATION") + "</small></div>",
					data: l,
					tpl: ["<li data-value='@${name} '>", "<a href title='${display_name}' class='lc-avatar lc-avatar-30'>", "<img src='" + appGlobal.config.box.avatarUrl + "${avatar}' class='m-l-10'/></a>&nbsp; ", "${display_name}", " <small>(${name})</small>", "</li>"].join(""),
					search_key: "display_name",
					callbacks: {
						filter: function(a, e, t) {
							if (_.isEmpty(a)) return e;
							if (_.isEmpty(e)) return e;
							var n = a.toLowerCase();
							return _.filter(e, function(a) {
								return a.display_name.toLowerCase().indexOf(n) > -1 || a.display_name_pinyin.toLowerCase().indexOf(n) > -1 || a.name.toLowerCase().indexOf(n) > -1
							})
						},
						sorter: function(a, e, t) {
							return e
						},
						tpl_eval: function(a, t) {
							var n = null;
							if ("group" === t.name) n = d.replace(/\$\{([^\}]*)\}/g, function(a, e, n) {
								return t[e]
							});
							else if (t.avatar && "default_avatar.png" != t.avatar && "default.png" != t.avatar) n = a.replace(/\$\{([^\}]*)\}/g, function(a, e, n) {
								return a.indexOf("avatar") > 0 ? t[e].replace(".", "_40x40.") : t[e]
							});
							else {
								var i = t.display_name.substring(0, 1).toLocaleUpperCase();
								t.firstText = i, t.shortName = e("avatarShortName")(t.display_name), t.bgColor = e("avatarBgColor")(t.display_name)["background-color"], n = r.replace(/\$\{([^\}]*)\}/g, function(a, e, n) {
									return t[e]
								})
							}
							return n
						}
					}
				})
			}, !0), c.channels && m.channel && l.$watch(c.channels, function(a) {
				return _.isEmpty(a) ? void o.atwho({
					at: "#g:",
					data: []
				}) : void o.atwho({
					at: "#g:",
					limit: 1e4,
					data: a,
					tpl: '<li data-value="#${name} " title="#${name}" class="channel"><i class="lcfont lc-link-group"></i>${name}</li>',
					header_tpl: "<div class='header'>" + s.instant("atwho.GROUP") + "<small>↑&nbsp;↓&nbsp;" + s.instant("atwho.NAVIGATION") + "</small></div>",
					callbacks: {
						filter: function(a, e, t) {
							return _.isEmpty(a) ? e : _.isEmpty(e) ? e : _.filter(e, function(e) {
								return e.name_pinyin.toLowerCase().indexOf(a.toLowerCase()) > -1 || e.name.toLowerCase().indexOf(a.toLowerCase()) > -1
							})
						},
						sorter: function(a, e, t) {
							return e
						}
					}
				})
			}, !0)
		}
	}
}]).directive("lcCommand", ["lcData", "$translate", "MessageDataContext", function(a, e, t) {
	return {
		require: "^ngModel",
		link: function(n, i, s) {
			n.$watch(s.lcCommand, function(a, t) {
				var n = _.sortBy(a, function(a) {
					return a.name
				});
				n = _.filter(n, function(a) {
					return !a.service_id || a.service_id && a.show_in_list
				});
				var s = "<li data-value='${name}' class='command'>${name} ${usage_hint} <span class='desc'>" + e.instant("atwho.CUSTOM") + "${desc}</span></li>";
				i.atwho({
					at: "/",
					data: n,
					show_the_at: !0,
					limit: 1e4,
					tpl: "<li data-value='${name}' class='command'>${name} ${usage_hint} <span class='desc'>${desc}</li>",
					header_tpl: "<div class='header'>" + e.instant("atwho.COMMAND") + "<small>↑&nbsp;↓&nbsp;" + e.instant("atwho.NAVIGATION") + "</small></div>",
					callbacks: {
						matcher: function(a, e) {
							var t, n, i = !0;
							return a = a.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&"), i && (a = "(?:^|\\s)" + a), n = new RegExp(a + "([A-Za-z0-9_+-.]*)$|" + a + "([^\-\ÿ]*)$", "gi"), t = n.exec(e), t ? t[2] || t[1] : null
						},
						filter: function(a, e) {
							if (_.isEmpty(a)) return e;
							if (_.isEmpty(e)) return e;
							var t = _.filter(e, function(e) {
								var t = e.usage_hint.toLowerCase(),
									n = (e.name.toLowerCase(), t.indexOf(a.toLowerCase()) > -1 || e.name.toLowerCase().indexOf(a.toLowerCase()) > -1);
								return n
							});
							return t
						},
						tpl_eval: function(a, e) {
							return (e.desc && !e.type ? s : a).replace(/\$\{([^\}]*)\}/g, function(a, t, n) {
								return e[t]
							})
						},
						sorter: function(a, e, t) {
							return e
						}
					}
				})
			}, !0), n.$on("$addCommand", function(e, n) {
				n.isShow ? a.integration.getCommand(n.service_id).success(function(a) {
					var e = _.find(t.data.commands, function(a) {
						return a.service_id == n.service_id
					});
					if (e) for (var i = 0; i < t.data.commands.length; i++) t.data.commands[i].service_id === a.data.service_id && (t.data.commands[i] = a.data);
					else t.data.commands.push(a.data)
				}).error(function(a) {}) : _.remove(t.data.commands, function(a) {
					return a.service_id == n.service_id
				})
			})
		}
	}
}]);