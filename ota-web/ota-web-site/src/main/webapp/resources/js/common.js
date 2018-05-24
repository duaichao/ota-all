function getDocHeight(doc) {
    doc = doc || document;
    // stackoverflow.com/questions/1145850/
    var body = doc.body, html = doc.documentElement;
    var height = Math.max( body.scrollHeight, body.offsetHeight, 
        html.clientHeight, html.scrollHeight, html.offsetHeight );
    return height;
}

function setIframeHeight(id) {
    var ifrm = document.getElementById(id);
    var doc = ifrm.contentDocument? ifrm.contentDocument: 
        ifrm.contentWindow.document;
    ifrm.style.visibility = 'hidden';
    ifrm.style.height = "10px"; // reset to minimal height ...
    // IE opt. for bing/msn needs a bit added or scrollbar appears
    ifrm.style.height = getDocHeight( doc ) + 4 + "px";
    ifrm.style.visibility = 'visible';
}

function idcard(val,len){//身份证验证
	/*
		var aCity={
			11:"北京",
			12:"天津",
			13:"河北",
			14:"山西",
			15:"内蒙古",
			21:"辽宁",
			22:"吉林",
			23:"黑龙江 ",
			31:"上海",
			32:"江苏",
			33:"浙江",
			34:"安徽",
			35:"福建",
			36:"江西",
			37:"山东",
			41:"河南",
			42:"湖北 ",
			43:"湖南",
			44:"广东",
			45:"广西",
			46:"海南",
			50:"重庆",
			51:"四川",
			52:"贵州",
			53:"云南",
			54:"西藏 ",
			61:"陕西",
			62:"甘肃",
			63:"青海",
			64:"宁夏",
			65:"新疆",
			71:"台湾",
			81:"香港",
			82:"澳门",
			91:"国外 "
		};
	*/
    var aCity={
        11:"\u5317\u4eac",
        12:"\u5929\u6d25",
        13:"\u6cb3\u5317",
        14:"\u5c71\u897f",
        15:"\u5185\u8499\u53e4",
        21:"\u8fbd\u5b81",
        22:"\u5409\u6797",
        23:"\u9ed1\u9f99\u6c5f ",
        31:"\u4e0a\u6d77",
        32:"\u6c5f\u82cf",
        33:"\u6d59\u6c5f",
        34:"\u5b89\u5fbd",
        35:"\u798f\u5efa",
        36:"\u6c5f\u897f",
        37:"\u5c71\u4e1c",
        41:"\u6cb3\u5357",
        42:"\u6e56\u5317 ",
        43:"\u6e56\u5357",
        44:"\u5e7f\u4e1c",
        45:"\u5e7f\u897f",
        46:"\u6d77\u5357",
        50:"\u91cd\u5e86",
        51:"\u56db\u5ddd",
        52:"\u8d35\u5dde",
        53:"\u4e91\u5357",
        54:"\u897f\u85cf ",
        61:"\u9655\u897f",
        62:"\u7518\u8083",
        63:"\u9752\u6d77",
        64:"\u5b81\u590f",
        65:"\u65b0\u7586",
        71:"\u53f0\u6e7e",
        81:"\u9999\u6e2f",
        82:"\u6fb3\u95e8",
        91:"\u56fd\u5916 "
    } ,
    iSum=0 , info="" , sBirthday="" , d;
    IsIDCard = /^[1-9](\d{14}|\d{16}[\dxX])$/;
    if(!IsIDCard.test(val)) return false;
    val=val.replace(/[xX]$/i,"a");
    if(aCity[parseInt(val.substr(0,2))]==null)return false;
    if(len && val.length!=len) return false;

    if(val.length==18){
        for(var i=17;i>=0;i--){
            iSum+=(Math.pow(2,i)%11)*parseInt(val.charAt(17-i),11);
        }
        if(iSum%11!=1) return false;
        sBirthday=val.substr(6,4)+"/"+Number(val.substr(10,2))+"/"+Number(val.substr(12,2));
    }
    else {
        sBirthday="19"+val.substr(6,2)+"/"+Number(val.substr(8,2))+"/"+Number(val.substr(10,2));
    }

    d=new Date(sBirthday);
    if(sBirthday!=(d.getFullYear()+"/"+(d.getMonth()+1)+"/"+d.getDate()))return false;

    return true;
}