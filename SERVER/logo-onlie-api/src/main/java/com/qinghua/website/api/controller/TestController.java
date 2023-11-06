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
                "'id': 4958619729920,\n" +
                "'code': 'TEL2023092000010',\n" +
                "'draft': false,\n" +
                "'title': '【电话】关于希望将墩子移除的问题 ',\n" +
                "'address': '',\n" +
                "'standardAddr': '',\n" +
                "'content': '高新区秦望山路与滨江路东南角处原本是星河国际的工地，现在工地已经撤离了，门卫处原本设置的两个墩子未拿走，存在安全隐患，请部门派人移除。 ',\n" +
                "'createTime': '2023-09-20 08:21:47',\n" +
                "'archived': false,\n" +
                "'involvedPersonnels': [],\n" +
                "'priority': 2,\n" +
                "'creatorId': 20038,\n" +
                "'regionCode': '320281006000',\n" +
                "'regionName': '城东街道',\n" +
                "'sourceCode': '1',\n" +
                "'sourceName': '电话',\n" +
                "'sourceFullName': '江阴-12345-电话',\n" +
                "'atts': [\n" +
                "  {\n" +
                "'id': 15495059619840,\n" +
                "'fileName': '测试数据.rar',\n" +
                "'uploaderId': 19875,\n" +
                "'uploaderName': '签转1',\n" +
                "'fileObjectKey': 'wxctjy://oss/6544ad46c786428c8d3b50fffe745b7f.rar',\n" +
                "'uploadTime': '2023-10-19 10:09:50',\n" +
                "'type': 'http://2.19.221.84:27012/wxctjy/oss/6544ad46c786428c8d3b50fffe745b7f.rar'\n" +
                "  }\n" +
                "],\n" +
                "'todoTasks': [\n" +
                "{\n" +
                "'id': '1704406120238751749',\n" +
                "'beginTime': '2023-09-20 16:04:20',\n" +
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
                "'businessKey': '1704289715970252800',\n" +
                "'tag': 'CL',\n" +
                "'fromSuperior': true,\n" +
                "'creatorId': 7319,\n" +
                "'creatorName': '签转员5',\n" +
                "'eventTitle': '【电话】关于希望将墩子移除的问题 ',\n" +
                "'eventCode': '1704289715970252800',\n" +
                "'creatorOrgName': '江阴市住建局',\n" +
                "'eventId': 4958619729920\n" +
                "}\n" +
                "],\n" +
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
                "'tags': [],\n" +
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
                "'timeSpent': '39天18小时32分钟',\n" +
                "'timeSpentMinutes': 57272,\n" +
                "'remainTime2Complete': '超期：32天17小时7分钟',\n" +
                "'remainTime2CompleteMinutes': -47107,\n" +
                "'dueDays': 5,\n" +
                "'fromSuperior': true,\n" +
                "'processDefinitionKey': 'LN',\n" +
                "'state': 'PENDING',\n" +
                "'returnCount': 0\n" +
                "},\n" +
                "'disposedAtts': [],\n" +
                "'commentList': [{\n" +
                "'id':19496568176640,\n" +
                "'content':'sdfsdfdsfsdf',\n" +
                "'createTime':'2023-10-30 11:10:24',\n" +
                "'creatorId':7301,\n" +
                "'creatorName':'话务员01',\n" +
                "'creatorOrgCode':'320200-JTJ1',\n" +
                "'creatorOrgName':'江阴市',\n" +
                "'deleted':false,\n" +
                "'logs':[{\n" +
                "'id':19496568176641,\n" +
                "'content':'sdfsdfdsfsdf',\n" +
                "'createTime':'2023-10-30 11:10:24',\n" +
                "'creatorId':7301,\n" +
                "'creatorName':'话务员01',\n" +
                "'creatorOrgCode':'320200-JTJ1',\n" +
                "'creatorOrgName':'江阴市',\n" +
                "'operationType':1\n" +
                "}],\n" +
                "}],\n" +
                "'appraises': [],\n" +
                "'commonCode': 'TEL2023092000010',\n" +
                "'latestSubStageList': [],\n" +
                "'taskTagValueList': [\n" +
                "'CL'\n" +
                "],\n" +
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
                "'handleAtts': [],\n" +
                "'raw': {\n" +
                "'appraisement': '',\n" +
                "'appraisementComments': ''\n" +
                "},\n" +
                "'start12345': true,\n" +
                "'lastCompletionComments': '【2023-09-20 16:04】 签转员5，请 江阴市住建局 阅处，并通过本系统及时反馈答复意见。;;;;;',\n" +
                "'endorsementOpinions': '请 江阴市住建局 阅处，并通过本系统及时反馈答复意见。;;;;;',\n" +
                "'lockedLink': {},\n" +
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
