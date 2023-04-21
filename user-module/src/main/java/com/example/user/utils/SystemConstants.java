package com.example.user.utils;

public class SystemConstants {
    public static final String USER_NICK_NAME_PREFIX = "user_";
    public static final int My_PAGE_SIZE = 5;
    public static final int MAX_PAGE_SIZE = 10;

    public static final String CITY_JSON = "{\n" +
            "  \"provinces\": [\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"北京市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"北京市\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"天津市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"天津市\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"上海市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"上海市\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"重庆市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"重庆市\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"香港特别行政区\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"香港\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"澳门特别行政区\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"澳门\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"石家庄市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"唐山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"秦皇岛市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邯郸市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邢台市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"保定市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"张家口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"承德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"沧州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"廊坊市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"衡水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"辛集市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"晋州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新乐市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"遵化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"迁安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"武安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南宫市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"沙河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"涿州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"定州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安国市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高碑店市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平泉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泊头市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"任丘市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"黄骅市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"河间市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"霸州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"三河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"深州市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"河北省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"太原市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大同市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阳泉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"长治市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"晋城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"朔州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"晋中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"运城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"忻州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临汾市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吕梁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"古交市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"介休市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"永济市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"河津市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"原平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"侯马市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"霍州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"孝义市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汾阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"怀仁市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"山西省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"呼和浩特市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"包头市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乌海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"赤峰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"通辽市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鄂尔多斯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"呼伦贝尔市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"巴彦淖尔市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乌兰察布市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"霍林郭勒市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"满洲里市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"牙克石市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"扎兰屯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"额尔古纳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"根河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丰镇市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乌兰浩特市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿尔山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"二连浩特市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"锡林浩特市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"内蒙古自治区\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"沈阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大连市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鞍山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"抚顺市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"本溪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丹东市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"锦州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"营口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阜新市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"辽阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"盘锦市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铁岭市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"朝阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"葫芦岛市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新民市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"瓦房店市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"庄河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"凤城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"凌海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北镇市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"盖州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大石桥市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"灯塔市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"调兵山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"开原市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北票市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"凌源市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴城市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"辽宁省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"长春市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吉林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"四平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"辽源市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"通化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"白山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"松原市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"白城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"榆树市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"德惠市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"蛟河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桦甸市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"舒兰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"磐石市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"公主岭市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"双辽市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"梅河口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"集安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"洮南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"延吉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"图们市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"敦化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"珲春市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙井市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"和龙市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"扶余市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"吉林省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"哈尔滨市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"齐齐哈尔市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"黑河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大庆市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"伊春市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鹤岗市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"佳木斯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"双鸭山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"七台河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鸡西市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"牡丹江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"绥化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"尚志市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"五常市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"讷河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"五大连池市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"嫩江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铁力市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"同江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"富锦市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"虎林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"密山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"绥芬河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宁安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安达市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"肇东市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海伦市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"穆棱市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"抚远市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"漠河市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"黑龙江省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"南京市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"徐州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"连云港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宿迁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"淮安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"盐城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"扬州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泰州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南通市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"镇江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"常州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"无锡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"苏州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"常熟市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"张家港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"太仓市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昆山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"江阴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"溧阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"扬中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"句容市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丹阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"如皋市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"启东市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高邮市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"仪征市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泰兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"靖江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东台市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邳州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新沂市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"江苏省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"杭州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宁波市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"湖州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"嘉兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"舟山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"绍兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"衢州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"金华市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"台州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"温州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丽水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"建德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"慈溪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"余姚市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平湖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桐乡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"诸暨市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"嵊州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"江山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兰溪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"永康市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"义乌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"温岭市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"瑞安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乐清市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙泉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"玉环市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"浙江省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"合肥市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"芜湖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"蚌埠市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"淮南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"马鞍山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"淮北市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铜陵市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安庆市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"黄山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"滁州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阜阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宿州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"六安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"亳州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"池州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宣城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"巢湖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桐城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"天长市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"明光市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"界首市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宁国市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"广德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"潜山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"无为市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"安徽省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"厦门市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"福州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"三明市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"莆田市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泉州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"漳州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙岩市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宁德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"福清市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邵武市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"武夷山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"建瓯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"永安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"石狮市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"晋江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"漳平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"福安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"福鼎市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"福建省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"南昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"九江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"景德镇市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鹰潭市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新余市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"萍乡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"赣州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"上饶市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"抚州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜春市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吉安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"瑞昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"共青城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"庐山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乐平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"瑞金市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"德兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丰城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"樟树市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"井冈山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"贵溪市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"江西省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"济南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"青岛市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"聊城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"德州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东营市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"淄博市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"潍坊市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"烟台市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"威海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"日照市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临沂市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"枣庄市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"济宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泰安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"滨州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"菏泽市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"胶州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平度市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"莱西市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临清市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乐陵市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"禹城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安丘市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昌邑市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高密市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"青州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"诸城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"寿光市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"栖霞市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"龙口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"莱阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"莱州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"蓬莱市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"招远市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"荣成市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乳山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"滕州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"曲阜市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邹城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新泰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"肥城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邹平市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"山东省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"郑州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"开封市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"洛阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平顶山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鹤壁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新乡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"焦作市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"濮阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"许昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"漯河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"三门峡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"商丘市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"周口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"驻马店市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"信阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"荥阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新郑市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"登封市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新密市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"偃师市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"孟州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"沁阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"卫辉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"辉县市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"长垣市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"林州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"禹州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"长葛市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"舞钢市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"义马市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"灵宝市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"项城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"巩义市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邓州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"永城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汝州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"济源市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"河南省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"武汉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"十堰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"襄阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"荆门市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"孝感市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"黄冈市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鄂州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"黄石市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"咸宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"荆州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"随州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丹江口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"老河口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"枣阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"钟祥市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"京山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汉川市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"应城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安陆市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"广水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"麻城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"武穴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大冶市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"赤壁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"石首市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"洪湖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"松滋市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜都市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"枝江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"当阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"恩施市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"利川市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"仙桃市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"天门市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"潜江市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"湖北省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"长沙市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"衡阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"张家界市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"常德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"益阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"岳阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"株洲市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"湘潭市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"郴州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"永州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邵阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"怀化市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"娄底市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"耒阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"常宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"浏阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"津市市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"沅江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汨罗市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临湘市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"醴陵市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"湘乡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"韶山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"资兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"武冈市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邵东市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"洪江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"冷水江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"涟源市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吉首市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宁乡市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"湖南省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"广州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"深圳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"清远市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"韶关市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"河源市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"梅州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"潮州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汕头市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"揭阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汕尾市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"惠州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东莞市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"珠海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"中山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"江门市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"佛山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"肇庆市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"云浮市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阳江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"茂名市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"湛江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"英德市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"连州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乐昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南雄市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"普宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"陆丰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"恩平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"台山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"开平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"鹤山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"四会市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"罗定市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阳春市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"化州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"信宜市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吴川市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"廉江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"雷州市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"广东省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"南宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桂林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"柳州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"梧州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"贵港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"玉林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"钦州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"防城港市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"崇左市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"百色市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"河池市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"来宾市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"贺州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"岑溪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桂平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北流市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东兴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"凭祥市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"合山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"靖西市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平果市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"荔浦市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"广西壮族自治区\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"海口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"三亚市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"三沙市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"儋州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"文昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"琼海市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"万宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"东方市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"五指山市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"海南省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"成都市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"广元市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"绵阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"德阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"南充市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"广安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"遂宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"内江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乐山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"自贡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泸州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宜宾市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"攀枝花市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"巴中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"达州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"资阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"眉山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"雅安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"崇州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"邛崃市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"都江堰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"彭州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"江油市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"什邡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"广汉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"绵竹市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阆中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"华蓥市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"峨眉山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"万源市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"简阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"西昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"康定市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"马尔康市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"隆昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"射洪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"会理市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"四川省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"贵阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"六盘水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"遵义市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安顺市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"毕节市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铜仁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"清镇市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"赤水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"仁怀市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"凯里市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"都匀市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴义市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"福泉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"盘州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴仁市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"贵州省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"昆明市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"曲靖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"玉溪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"丽江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昭通市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"普洱市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临沧市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"保山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宣威市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"芒市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"瑞丽市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"大理市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"楚雄市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"个旧市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"开远市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"蒙自市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"弥勒市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"景洪市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"文山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"香格里拉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"腾冲市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"水富市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"澄江市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"泸水市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"云南省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"西安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"延安市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铜川市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"渭南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"咸阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"宝鸡市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"汉中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"榆林市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"商洛市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"安康市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"韩城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"华阴市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"兴平市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"彬州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"神木市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"子长市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"陕西省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"兰州市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"嘉峪关市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"金昌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"白银市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"天水市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"酒泉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"张掖市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"武威市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"庆阳市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"平凉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"定西市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"陇南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"玉门市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"敦煌市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"临夏市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"合作市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"华亭市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"甘肃省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"西宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"海东市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"格尔木市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"德令哈市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"玉树市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"茫崖市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"青海省\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"拉萨市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"日喀则市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昌都市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"林芝市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"山南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"那曲市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"西藏自治区\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"银川市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"石嘴山市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吴忠市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"中卫市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"固原市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"灵武市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"青铜峡市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"宁夏回族自治区\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"乌鲁木齐市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"克拉玛依市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"吐鲁番市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"哈密市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"喀什市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿克苏市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"库车市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"和田市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿图什市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿拉山口市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"博乐市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昌吉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阜康市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"库尔勒市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"伊宁市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"奎屯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"塔城市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"乌苏市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿勒泰市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"霍尔果斯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"石河子市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"阿拉尔市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"图木舒克市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"五家渠市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"北屯市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"铁门关市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"双河市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"可克达拉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"昆玉市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"胡杨河市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"新疆维吾尔自治区\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"citys\": [\n" +
            "        {\n" +
            "          \"cityName\": \"台北市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新北市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"桃园市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"台中市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"台南市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"高雄市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"基隆市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"新竹市\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"cityName\": \"嘉义市\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"provinceName\": \"台湾省\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
