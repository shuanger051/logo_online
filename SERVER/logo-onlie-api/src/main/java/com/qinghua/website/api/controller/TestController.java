package com.qinghua.website.api.controller;

import com.alibaba.fastjson.JSON;
import com.qinghua.website.api.controller.io.TestLoginIO;
import com.qinghua.website.api.controller.io.TestPageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/login")
    public String login(@RequestBody @Validated TestLoginIO testLoginIO){

        log.info(testLoginIO.getAccount());
        log.info(testLoginIO.getPwd());

        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("body","token");
        map.put("msg","操作成功");
        map.put("code",200);
        return JSON.toJSONString(map);
    }

    @PostMapping("/page")
    public String page(@RequestBody @Validated TestPageIO testPageIO){

        log.info(testPageIO.getAuth());
        log.info(testPageIO.getGatewayCode());
        log.info(testPageIO.getTitle());
        log.info(String.valueOf(testPageIO.getMode()));
        log.info(testPageIO.getTaskTag());
        log.info(String.valueOf(testPageIO.getPage()));
        log.info(String.valueOf(testPageIO.getSize()));
        log.info(testPageIO.getClaimantName());
        log.info(testPageIO.getCallNumber());
        log.info(testPageIO.getContent());
        log.info(testPageIO.getCode());
        log.info(String.valueOf(testPageIO.getWorkOrderType()));

        String jsonRes = "{\n" +
                "'success': true,\n" +
                "'msg': '操作成功',\n" +
                "'code': 200,\n" +
                "'body': {\n" +
                "'size': 10,\n" +
                "'number': 0,\n" +
                "'totalElements': 36,\n" +
                "'content': [{\n" +
                "'id': 4958619729920,\n" +
                "'code': 'TEL2023092000010',\n" +
                "'draft': false,\n" +
                "'title': '【电话】关于希望将墩子移除的问题 ',\n" +
                "'address': '',\n" +
                "'standardAddr': '',\n" +
                "'content': '高新区秦望山路与滨江路东南角处原本是星河国际的工地，现在工地已经撤离了，门卫处原本设置的两个墩子未拿走，存在安全隐患，请部门派人移除。 ',\n" +
                "'createTime': '2023-09-20 08:21:47',\n" +
                "'archived': false,\n" +
                "'priority': 2,\n" +
                "'creatorId': 20038,\n" +
                "'regionCode': '320281006000',\n" +
                "'regionName': '城东街道',\n" +
                "'sourceCode': '1',\n" +
                "'sourceName': '电话',\n" +
                "'sourceFullName': '江阴-12345-电话',\n" +
                "'creatorName': '李丹1',\n" +
                "'creatorIdentiyCode': 'ACCEPTOR',\n" +
                "'creatorIdentiyName': '话务员',\n" +
                "'creatorOrgName': '江阴市',\n" +
                "'creatorOrgCode': '320200-JTJ1',\n" +
                "'completed': false,\n" +
                "'occurrenceTime': '2023-09-20 08:19:49',\n" +
                "'quality': false,\n" +
                "'secrecy': false,\n" +
                "'enable': true,\n" +
                "'state': 'UN_CLAIM',\n" +
                "'stageState': 'UN_CLAIM',\n" +
                "'eventState': 'PROCESSING',\n" +
                "'groupId': 'ef7d8c594d7f45139c8f875eb9a9010c',\n" +
                "'retVisit': true,\n" +
                "'sendSMS': true,\n" +
                "'pushUp': true,\n" +
                "'flag': '{}',\n" +
                "'stage': {\n" +
                "'id': '1704406120138088448',\n" +
                "'orgCode': '320200-JTJ1-0U3J',\n" +
                "'beginTime': '2023-09-20 16:04:20',\n" +
                "'orgName': '江阴市住建局',\n" +
                "'name': 'LN',\n" +
                "'round': 0,\n" +
                "'founderId': 7319,\n" +
                "'founderName': '签转员5',\n" +
                "'overdue': true,\n" +
                "'dueDate': '2023-09-27 17:30:00',\n" +
                "'claimDueDate': '2023-09-21 11:05:00',\n" +
                "'urgeCount': 0,\n" +
                "'isWg': false,\n" +
                "'timeSpent': '39天18小时0分钟',\n" +
                "'timeSpentMinutes': 57240,\n" +
                "'remainTime2Complete': '超期：32天16小时34分钟',\n" +
                "'remainTime2CompleteMinutes': -47074,\n" +
                "'remainTime2Claim': '超期：38天22小时59分钟',\n" +
                "'dueDays': 5,\n" +
                "'fromSuperior': true,\n" +
                "'processDefinitionKey': 'LN',\n" +
                "'state': 'PENDING',\n" +
                "'feedbackEfficiency': {\n" +
                "'id': 9960,\n" +
                "'name': '超时未签收',\n" +
                "'group': 'feedback_efficiency',\n" +
                "'code': 'TIMEOUT_NO_CLAIM'\n" +
                "},\n" +
                "'returnCount': 0\n" +
                "},\n" +
                "'commonCode': 'TEL2023092000010',\n" +
                "'taskTagValueList': [\n" +
                "'CL'\n" +
                "],\n" +
                "'taskCreateTime': '2023-09-20 16:04:20',\n" +
                "'taskCreatorName': '签转员5',\n" +
                "'jyClassifyId': 241,\n" +
                "'jyClassifyCode': '020799',\n" +
                "'jyClassifyName': '城乡建设-市容管理-其他市容管理',\n" +
                "'workOrderTypeId': 28,\n" +
                "'workOrderTypeName': '求助',\n" +
                "'businessTypeId': 62,\n" +
                "'businessTypeName': '标准工单',\n" +
                "'callNumber': '13601526993',\n" +
                "'claimantName': '刘',\n" +
                "'claimantSex': 1,\n" +
                "'claimantTypeId': 60,\n" +
                "'claimantTypeName': '个人',\n" +
                "'purpose': '服务对象表示希望移除墩子 ',\n" +
                "'registrantId': 20038,\n" +
                "'registrantName': '李丹1',\n" +
                "'claimantAgeRangeId': 50,\n" +
                "'claimantAgeRangeName': '无法获取',\n" +
                "'claimantContactNumber': '',\n" +
                "'claimantMoodId': 45,\n" +
                "'claimantMoodName': '其他',\n" +
                "'claimantCardTypeId': 2,\n" +
                "'claimantCardTypeName': '其他',\n" +
                "'claimantCardNo': '',\n" +
                "'registrationTime': '2023-09-20 08:21:46',\n" +
                "'themeTypeId': 58,\n" +
                "'themeTypeName': '普通诉求',\n" +
                "'portrait': '',\n" +
                "'comments': '',\n" +
                "'happiness': false,\n" +
                "'dispatcherId': 7319,\n" +
                "'dispatcherName': '签转员5',\n" +
                "'adviceDispatcherName': '签转员5',\n" +
                "'dispatchTime': '2023-09-20 16:04:20',\n" +
                "'isWgEvent': false,\n" +
                "'raw': {\n" +
                "'appraisement': '',\n" +
                "'appraisementComments': ''\n" +
                "},\n" +
                "'officeReplay': false,\n" +
                "'visible': true,\n" +
                "'etype': 'JY'\n" +
                "}\n" +
                "],\n" +
                "'searchCount': false,\n" +
                "'pageable': {\n" +
                "'paged': true,\n" +
                "'pageNumber': 0,\n" +
                "'offset': 0,\n" +
                "'pageSize': 10,\n" +
                "'unpaged': false,\n" +
                "'sort': {\n" +
                "'unsorted': true,\n" +
                "'sorted': false,\n" +
                "'empty': true\n" +
                "}\n" +
                "},\n" +
                "'empty': false,\n" +
                "'sort': {\n" +
                "'unsorted': true,\n" +
                "'sorted': false,\n" +
                "'empty': true\n" +
                "},\n" +
                "'total': 36,\n" +
                "'last': false,\n" +
                "'numberOfElements': 10,\n" +
                "'totalPages': 4,\n" +
                "'current': 0,\n" +
                "'first': true,\n" +
                "'pages': 4\n" +
                "}\n" +
                "}";

        return jsonRes;
    }

    @PostMapping("/completeFeedback")
    public String completeFeedback(){
        return "{\n" +
                "    \"success\": true,\n" +
                "    \"code\": 200}";
    }

    @PostMapping("/detail")
    public String detail(){
        log.info("Detail 請求");
        return "{\n" +
                "'success': true,\n" +
                "'msg': '操作成功',\n" +
                "'code': 200,\n" +
                "'body': {\n" +
                "'id': 21689169952768,\n" +
                "'code': 'TEL2023110510350',\n" +
                "'draft': false,\n" +
                "'title': '【电话ῑ关于\u0FDC继：长江天悦府13巿501室阳台下水管无法排水”的问题',\n" +
                "'address': '澄江街道',\n" +
                "'standardAddr': '澄江街道',\n" +
                "'content': '我是澄江街道毗陵路与花山路交界处长江天悦帿13巿501室精装房的业主，我多次反映了我近期发现阳台下水管没有接翚，现在\u4DBF排水，四楼就漏水，向物业咨询，说是施工时改变了方案，原来的管子锯掉了，也没有说俎么解决排水的问题，住建寿答复称，庿发商说我自行对阳台进行了改翠，庿发商提供了相应的资料，也与我进行了沟通说明ῂ下水管不在空调机位里面，是独立的，是开发商施工的时候下水管没有接翚，导致漏水，现在一排水，四楼就漏水，为了避免邻里纠纷，我濁婆县4楼住户支付了5000元维修费用，希望部门再次协调，要么帮我接通下水管，要么让庿发商承担我的损失⾿',\n" +
                "'createTime': '2023-11-05 12:23:01',\n" +
                "'archived': false,\n" +
                "'involvedPersonnels': [],\n" +
                "'latitude': 31.900292,\n" +
                "'longitude': 120.271256,\n" +
                "'priority': 2,\n" +
                "'creatorId': 20026,\n" +
                "'regionCode': '320281001000',\n" +
                "'regionName': '江阴嶿-澄江街道-',\n" +
                "'sourceCode': '1',\n" +
                "'sourceName': '电话',\n" +
                "'sourceFullName': '江阴-12345-电话',\n" +
                "'atts': [],\n" +
                "'todoTasks': [{\n" +
                "'id': '1721073769162924035',\n" +
                "'beginTime': '2023-11-05 15:55:37',\n" +
                "'name': '处理',\n" +
                "'master': false,\n" +
                "'candidateGroup': '320200-JTJ1-0U3JADMIN',\n" +
                "'taskDefinitionKey': 'CL',\n" +
                "'form': 'CL',\n" +
                "'stageName': 'LN',\n" +
                "'claimable': true,\n" +
                "'operable': true,\n" +
                "'returnable': false,\n" +
                "'assignable': true,\n" +
                "'editable': false,\n" +
                "'completable': true,\n" +
                "'businessKey': '1721020267578413056',\n" +
                "'tag': 'CL',\n" +
                "'fromSuperior': true,\n" +
                "'creatorId': 7320,\n" +
                "'creatorName': '签转6',\n" +
                "'eventTitle': '【电话ῑ关于\u0FDC继：长江天悦府13巿501室阳台下水管无法排水”的问题',\n" +
                "'eventCode': '1721020267578413056',\n" +
                "'creatorOrgName': '江阴市住建局',\n" +
                "'eventId': 21689169952768\n" +
                "}],\n" +
                "'creatorName': '金晨陿',\n" +
                "'creatorIdentiyCode': 'ACCEPTOR',\n" +
                "'creatorIdentiyName': '话务叿',\n" +
                "'creatorOrgName': '江阴嶿',\n" +
                "'creatorOrgCode': '320200-JTJ1',\n" +
                "'completed': false,\n" +
                "'occurrenceTime': '2023-11-05 12:19:24',\n" +
                "'quality': false,\n" +
                "'secrecy': false,\n" +
                "'enable': true,\n" +
                "'state': 'UN_CLAIM',\n" +
                "'stageState': 'UN_CLAIM',\n" +
                "'eventState': 'PROCESSING',\n" +
                "'groupId': '0c078b0167f546dd97f7ca3382550285',\n" +
                "'retVisit': true,\n" +
                "'sendSMS': true,\n" +
                "'pushUp': true,\n" +
                "'tags': [],\n" +
                "'flag': '{}',\n" +
                "'stage': {\n" +
                "'id': '1721073769066455040',\n" +
                "'orgCode': '320200-JTJ1-0U3J',\n" +
                "'beginTime': '2023-11-05 15:55:37',\n" +
                "'orgName': '江阴市住建局',\n" +
                "'name': 'LN',\n" +
                "'round': 0,\n" +
                "'founderId': 7320,\n" +
                "'founderName': '签转6',\n" +
                "'dueDate': '2023-11-10 17:00:00',\n" +
                "'claimDueDate': '2023-11-06 14:30:00',\n" +
                "'urgeCount': 0,\n" +
                "'isWg': false,\n" +
                "'timeSpent': '19小时46分钟',\n" +
                "'timeSpentMinutes': 1186,\n" +
                "'remainTime2Complete': '剩余ﺿ4墿5小时17分钟',\n" +
                "'remainTime2CompleteMinutes': 6077,\n" +
                "'dueDays': 5,\n" +
                "'fromSuperior': true,\n" +
                "'processDefinitionKey': 'LN',\n" +
                "'state': 'PENDING',\n" +
                "'returnCount': 0\n" +
                "},\n" +
                "'disposedAtts': [],\n" +
                "'commentList': [],\n" +
                "'appraises': [],\n" +
                "'selfCompleted': false,\n" +
                "'commonCode': 'TEL2023110510350',\n" +
                "'latestSubStageList': [],\n" +
                "'taskTagValueList': ['CL'],\n" +
                "'jyClassifyId': 261,\n" +
                "'jyClassifyCode': '030102',\n" +
                "'jyClassifyName': '住房保障-房屋建设-房屋质量',\n" +
                "'workOrderTypeId': 28,\n" +
                "'workOrderTypeName': '求助',\n" +
                "'businessTypeId': 62,\n" +
                "'businessTypeName': '标准工单',\n" +
                "'callNumber': '15052197088',\n" +
                "'claimantName': '闿',\n" +
                "'claimantSex': 1,\n" +
                "'claimantTypeId': 60,\n" +
                "'claimantTypeName': '个人',\n" +
                "'purpose': '服务对象表示希望接翚下水管，要么让庿发商承担我的损失⾿',\n" +
                "'registrantId': 20026,\n" +
                "'registrantName': '金晨陿',\n" +
                "'claimantAgeRangeId': 50,\n" +
                "'claimantAgeRangeName': '无法获取',\n" +
                "'claimantContactNumber': '',\n" +
                "'claimantMoodId': 45,\n" +
                "'claimantMoodName': '其他',\n" +
                "'claimantCardTypeId': 2,\n" +
                "'claimantCardTypeName': '其他',\n" +
                "'claimantCardNo': '',\n" +
                "'registrationTime': '2023-11-05 12:23:01',\n" +
                "'themeTypeId': 58,\n" +
                "'themeTypeName': '普翚诉毿',\n" +
                "'portrait': '',\n" +
                "'comments': '',\n" +
                "'happiness': false,\n" +
                "'dispatcherId': 7320,\n" +
                "'dispatcherName': '签转6',\n" +
                "'adviceDispatcherName': '签转2',\n" +
                "'dispatchTime': '2023-11-05 15:55:37',\n" +
                "'isWgEvent': false,\n" +
                "'handleAtts': [],\n" +
                "'raw': {\n" +
                "'派发公示': false,\n" +
                "'appraisement': 'undefined',\n" +
                "'appraisementComments': 'undefined'\n" +
                "},\n" +
                "'start12345': true,\n" +
                "'lastCompletionComments': '⾿2023-11-05 15:55⾿ 签转6，请 江阴市住建局 阅处，并通过本系统及时反馈答复意见\u1F7F;;;;;',\n" +
                "'endorsementOpinions': '譿 江阴市住建局 阅处，并通过本系统及时反馈答复意见\u1F7F;;;;;',\n" +
                "'lockedLink': {},\n" +
                "'publicityState': 0,\n" +
                "'publicity': false,\n" +
                "'way12345': 2,\n" +
                "'etype': 'JY'\n" +
                "}\n" +
                "}";
    }

    @PostMapping("/upload")
    public String upload(){
        return "{\n" +
                "    'success': true,\n" +
                "    'msg': '操作成功',\n" +
                "    'code': 200,\n" +
                "    'body': {\n" +
                "        'name': 'test1.png',\n" +
                "        'key': 'wxctgrid://oss/ad9d444967c44bcda370c8e85916e1d1.png',\n" +
                "        'url': 'https://2.22.128.38:27025/wxctgrid/oss/ad9d444967c44bcda370c8e85916e1d1.png'\n" +
                "    }\n" +
                "}";
    }


}
