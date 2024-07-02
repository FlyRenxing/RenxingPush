let api = [];
const apiDocListSize = 1
api.push({
    name: 'default',
    order: '1',
    list: []
})
api[0].list.push({
    alias: 'SystemController',
    order: '1',
    link: '系统类',
    desc: '系统类',
    list: []
})
api[0].list[0].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://{{server}}/sys/qqbotlist',
    methodId: '0ee39292a52f9bdc51fd1b71d4b1c024',
    desc: '获取机器人公开列表',
});
api[0].list[0].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://{{server}}/sys/note',
    methodId: 'beaf1903eeaa39f732afdc8ae16085c7',
    desc: '获取所有公告',
});
api[0].list[0].list.push({
    order: '3',
    deprecated: 'false',
    url: 'http://{{server}}/sys/qqUrl',
    methodId: '5c68c418119d3d1931e2e1362a3d3a5d',
    desc: '获取QQ登录URL',
});
api[0].list.push({
    alias: 'UserController',
    order: '2',
    link: '用户相关',
    desc: '用户相关',
    list: []
})
api[0].list[1].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://{{server}}/user/webauthnRegReq',
    methodId: '595caaa858cbe5e187bff848f50b45d2',
    desc: '用户webauthn令牌注册请求',
});
api[0].list[1].list.push({
    order: '2',
    deprecated: 'false',
    url: 'http://{{server}}/user/webauthnRegResp',
    methodId: '359032eb9d977b6221bbf51398c5cafb',
    desc: '用户webauthn令牌注册响应',
});
api[0].list[1].list.push({
    order: '3',
    deprecated: 'false',
    url: 'http://{{server}}/user/webauthnLoginReq',
    methodId: '031d58cd56772fa8882a546fec67fdfe',
    desc: '用户webauthn令牌登录请求',
});
api[0].list[1].list.push({
    order: '4',
    deprecated: 'false',
    url: 'http://{{server}}/user/webauthnLoginResp',
    methodId: '7b531d9c18708998e261c11c138aca9c',
    desc: '用户webauthn令牌登录响应',
});
api[0].list[1].list.push({
    order: '5',
    deprecated: 'false',
    url: 'http://{{server}}/user/login',
    methodId: '6cecd9304c73462525a89cd4dd8af10d',
    desc: '用户登录',
});
api[0].list[1].list.push({
    order: '6',
    deprecated: 'false',
    url: 'http://{{server}}/user/telegramLogin',
    methodId: '2c97e3bbce0703b51ca0129ad4107091',
    desc: 'telegram登录',
});
api[0].list[1].list.push({
    order: '7',
    deprecated: 'false',
    url: 'http://{{server}}/user/telegramQRCodeLogin',
    methodId: '3e778e918abdee88092fcd46fd61f6a8',
    desc: 'telegram二维码登录',
});
api[0].list[1].list.push({
    order: '8',
    deprecated: 'false',
    url: 'http://{{server}}/user/register',
    methodId: '2c546027cae271a73f4b7ed329e6b8c2',
    desc: '注册',
});
api[0].list[1].list.push({
    order: '9',
    deprecated: 'false',
    url: 'http://{{server}}/user/refreshCipher',
    methodId: '3cb2e496824cdd54a689346911cb53ff',
    desc: '重置个人密钥',
});
api[0].list[1].list.push({
    order: '10',
    deprecated: 'false',
    url: 'http://{{server}}/user/profile',
    methodId: 'abc90650208756328a843032aadef10f',
    desc: '获取个人资料',
});
api[0].list[1].list.push({
    order: '11',
    deprecated: 'false',
    url: 'http://{{server}}/user/qq_bot',
    methodId: '4b676ae48aed245efd346a56cd280367',
    desc: '换绑QQ机器人',
});
api[0].list[1].list.push({
    order: '12',
    deprecated: 'false',
    url: 'http://{{server}}/user/ToDayUseCount',
    methodId: 'a3dda6da2ccaba2b6df40c9c0bc8b100',
    desc: '获取当日用户使用次数',
});
api[0].list[1].list.push({
    order: '13',
    deprecated: 'false',
    url: 'http://{{server}}/user/qqGroupWhitelist',
    methodId: 'ee15f5eb41ba59142fbf9c55d1271e15',
    desc: '添加QQ群白名单',
});
api[0].list[1].list.push({
    order: '14',
    deprecated: 'false',
    url: 'http://{{server}}/user/qqGroupWhitelist',
    methodId: '7e58925cb261389f9fe5e3fffa54e78b',
    desc: '获取QQ群白名单列表',
});
api[0].list[1].list.push({
    order: '15',
    deprecated: 'false',
    url: 'http://{{server}}/user/qqGroupWhitelist/{id}',
    methodId: 'efe0625a29d17e36d1cc213f8fd694dc',
    desc: '删除QQ群白名单',
});
api[0].list[1].list.push({
    order: '16',
    deprecated: 'false',
    url: 'http://{{server}}/user/messageCallback',
    methodId: '6ff5b37d8a1ca498a80d1915e264fb54',
    desc: '添加消息回调',
});
api[0].list[1].list.push({
    order: '17',
    deprecated: 'false',
    url: 'http://{{server}}/user/messageCallback',
    methodId: 'ac2a16710c1e325c598d368b1f0a1d5a',
    desc: '获取消息回调列表',
});
api[0].list[1].list.push({
    order: '18',
    deprecated: 'false',
    url: 'http://{{server}}/user/messageCallback/{id}',
    methodId: '419cff40e9128742d48c8d40d34ae7a9',
    desc: '删除消息回调',
});
api[0].list[1].list.push({
    order: '19',
    deprecated: 'false',
    url: 'http://{{server}}/user/logout',
    methodId: '1c420031a918db149d9e534dda0e3161',
    desc: '退出账号',
});
api[0].list.push({
    alias: 'MsgController',
    order: '3',
    link: '消息处理',
    desc: '消息处理',
    list: []
})
api[0].list[2].list.push({
    order: '1',
    deprecated: 'false',
    url: 'http://{{server}}/msg/send/{cipher}',
    methodId: '24bcbd3072d7c9f1eccc5e8e43a645fb',
    desc: '推送消息',
});
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        const search = document.getElementById('search');
        const searchValue = search.value.toLocaleLowerCase();

        let searchGroup = [];
        for (let i = 0; i < api.length; i++) {

            let apiGroup = api[i];

            let searchArr = [];
            for (let i = 0; i < apiGroup.list.length; i++) {
                let apiData = apiGroup.list[i];
                const desc = apiData.desc;
                if (desc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                    searchArr.push({
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        alias: apiData.alias,
                        list: apiData.list
                    });
                } else {
                    let methodList = apiData.list || [];
                    let methodListTemp = [];
                    for (let j = 0; j < methodList.length; j++) {
                        const methodData = methodList[j];
                        const methodDesc = methodData.desc;
                        if (methodDesc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                            methodListTemp.push(methodData);
                            break;
                        }
                    }
                    if (methodListTemp.length > 0) {
                        const data = {
                            order: apiData.order,
                            desc: apiData.desc,
                            link: apiData.link,
                            alias: apiData.alias,
                            list: methodListTemp
                        };
                        searchArr.push(data);
                    }
                }
            }
            if (apiGroup.name.toLocaleLowerCase().indexOf(searchValue) > -1) {
                searchGroup.push({
                    name: apiGroup.name,
                    order: apiGroup.order,
                    list: searchArr
                });
                continue;
            }
            if (searchArr.length === 0) {
                continue;
            }
            searchGroup.push({
                name: apiGroup.name,
                order: apiGroup.order,
                list: searchArr
            });
        }
        let html;
        if (searchValue === '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            let $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiGroups, liClass, display) {
    let html = "";
    if (apiGroups.length > 0) {
        if (apiDocListSize === 1) {
            let apiData = apiGroups[0].list;
            let order = apiGroups[0].order;
            for (let j = 0; j < apiData.length; j++) {
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#' + apiData[j].alias + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="'+display+'">';
                let doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated === 'true') {
                        spanString='<span class="line-through">';
                    } else {
                        spanString='<span>';
                    }
                    html += '<li><a href="#' + doc[m].methodId + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_' + apiGroup.order + '_' + apiGroup.name + '">' + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="'+liClass+'">';
                    html += '<a class="dd" href="#' + apiData[j].alias + '">' + apiGroup.order + '.' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="'+display+'">';
                    let doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                       let spanString;
                        if (doc[m].deprecated === 'true') {
                           spanString='<span class="line-through">';
                       } else {
                           spanString='<span>';
                       }
                        html += '<li><a href="#' + doc[m].methodId + '">' + apiGroup.order + '.' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                   }
                    html += '</ul>';
                    html += '</li>';
                }

                html += '</ul>';
                html += '</li>';
            }
        }
    }
    return html;
}
