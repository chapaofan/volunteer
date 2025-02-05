package com.volunteer.volunteer.controller.wxController;


import com.volunteer.volunteer.enums.DepartmentEnum;
import com.volunteer.volunteer.model.EnrollPerson;
import com.volunteer.volunteer.model.FormMss;
import com.volunteer.volunteer.service.EnrollPassService;
import com.volunteer.volunteer.service.EnrollPersonService;
import com.volunteer.volunteer.service.FormMssService;
import com.volunteer.volunteer.service.UserInformationService;
import com.volunteer.volunteer.util.EmojiCharacterUtil;
import com.volunteer.volunteer.util.ToolSupport.UniversalResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @description: 报名表控制类
 * @author: maolin
 * @create: 2019-05-03 01:24
 **/

@RestController
@RequestMapping("/wechat")
@Slf4j
public class EnrollController {

    @Resource
    private EnrollPersonService enrollPersonService;

    @Resource
    private EnrollPassService enrollPassService;

    @Resource
    private UserInformationService userInformationService;

    @Resource
    private FormMssService formMssService;

    /**
     * @Description: 报名
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @PostMapping(value = "/enroll/apply")
    public UniversalResponseBody<EnrollPerson> enroll(
            @NotNull @RequestParam("mainId") int mainId,
            @NotNull @RequestParam("realName") String realName,
            @NotNull @RequestParam("sex") String sex,
            @NotNull @RequestParam("telNo") String telNo,
            @NotNull @RequestParam("wechat") String wechat,
            @NotNull @RequestParam("school") String school,
            @NotNull @RequestParam("organization") String organization,
            @NotNull @RequestParam("firstChoice") String firstChoice,
            @RequestParam("secondChoice") String secondChoice,
            @RequestParam("thirdChoice") String thirdChoice,
            @RequestParam("introduction") String intro,
            @RequestParam("formId") String formId
    ) {
        //表情包处理
        String introduction = EmojiCharacterUtil.escape(intro);

        //报名表的存储
        EnrollPerson res = new EnrollPerson();
        res.setMainId(mainId);
        res.setRealName(realName);
        res.setSex(sex);
        res.setTelNo(telNo);
        res.setWechat(wechat);
        res.setSchool(school);
        res.setOrganization(organization);
        res.setFirstChoice(firstChoice);
        res.setSecondChoice(secondChoice);
        res.setThirdChoice(thirdChoice);
        res.setIntroduction(introduction.replace("\n", "。"));
        res.setEnrollStatus("0");

        //System.out.println(res.toString());
        //formId存储
        FormMss formMss = new FormMss();
        formMss.setMainId(mainId);
        formMss.setForm_id(formId);
        Date date = new Date(); //取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 7); //formId过期时间为七天
        date = calendar.getTime();
        formMss.setDeadline(date);

        try {
            if (enrollPersonService.insert(res) && enrollPassService.insertMainId(mainId) && formMssService.saveForm(formMss)) {
                return new UniversalResponseBody<>(0, "success", res);
            } else {
                return new UniversalResponseBody<>(-1, "failed", res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new UniversalResponseBody<>(-1, "failed", null);
        }
    }

    /**
     * @Description: 报名总人数
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @GetMapping(value = "/enroll/total/number")
    public UniversalResponseBody<Map<String, Integer>> enrollTotal() {
        Map<String, Integer> res = new TreeMap<>();
        int total = enrollPersonService.enrollTotal();

        if (total >= 0) {
            res.put("totalNumber", total);
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }


    /**
     * @Description: 各部门报名数据
     * @Param: []
     * @return: UniversalResponseBody
     */
    @GetMapping(value = "/enroll/departments/number")
    public UniversalResponseBody departmentEnrollData() {
        Map<String, Integer> res1 = enrollPersonService.departmentEnrollTotal();
        Map<String, Integer> res2 = enrollPersonService.crossDepartmentTotal();
        Map<String, Map<String, Integer>> res = new TreeMap<>();

        if (res1 != null && res2 != null) {
            res.put("enrollNumber", res1);
            res.put("crossNumber", res2);
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }

    /**
     * @Description: 单个部门招新数据
     * @Param: [department]
     * @return: UniversalResponseBody
     */
    @GetMapping(value = "/enroll/department/data")
    public UniversalResponseBody oneDepartmentEnrollData(@NotNull @RequestParam("department") int departmentCode) {
        String department = DepartmentEnum.getDepartment(departmentCode);
        Map<String, Integer> res = new TreeMap<>();
        Map<String, Integer> res1 = enrollPersonService.oneDepartmentEnrollData(department);
        Map<String, Integer> res2 = enrollPersonService.departmentEnrollDataBySex(department);

        if (res1 != null && res2 != null) {
            res.putAll(res1);
            res.putAll(res2);
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody(-1, "failed");
    }


    /**
     * @Description: 总队面试数据
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @GetMapping(value = "/interview/total/data")
    public UniversalResponseBody<Map<String, Integer>> interviewData() {
        Map<String, Integer> res = enrollPersonService.interviewData();
        if (res != null) {
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }

    /**
     * @Description: 各部门面试数据
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @GetMapping(value = "/interview/departments/data")
    public UniversalResponseBody<Map<String, Map<String, Integer>>> departmentInterviewData() {
        Map<String, Integer> res1 = enrollPersonService.departmentInterviewData();
        Map<String, Integer> res2 = enrollPersonService.notDepartmentInterviewData();
        Map<String, Map<String, Integer>> res = new TreeMap<>();
        if (res1 != null && res2 != null) {
            res.put("interviewedNumber", res1);
            res.put("noInterviewNumber", res2);

            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }


    /**
     * @Description: 单个部门一面数据
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @GetMapping(value = "/interview/first/department/data")
    public UniversalResponseBody<Map<String, Integer>> oneDepartmentInterviewData(@NotNull @RequestParam("department") int departmentCode) {
        String department = DepartmentEnum.getDepartment(departmentCode);
        Map<String, Integer> res = enrollPersonService.oneDepartmentInterviewData(department);
        if (res != null) {
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }


    /**
     * @Description: 单个部门二面面试数据
     * @Param: []
     * @return: UniversalResponseBody<Map < String, Integer>>
     */
    @GetMapping(value = "/interview/second/department/data")
    public UniversalResponseBody<Map<String, Integer>> secondDepartmentInterviewData(@NotNull @RequestParam("department") int departmentCode) {
        String department = DepartmentEnum.getDepartment(departmentCode);
        Map<String, Integer> res = enrollPersonService.secondDepartmentInterviewData(department);
        if (res != null) {
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }

    /**
     * @Description: 查看新生简历/一面扫码签到
     * @Param: []
     * @return: UniversalResponseBody<EnrollPerson>
     */
    @PostMapping(value = "/interview/first/department/resume")
    public UniversalResponseBody<EnrollPerson> checkResume(@RequestParam("mainId") Integer mainId) {
        EnrollPerson res = enrollPersonService.findByMainId(mainId);
        if (res != null) {
            //表情包转换
            res.setIntroduction(EmojiCharacterUtil.reverse(res.getIntroduction()));
            return new UniversalResponseBody<>(0, "success", res);
        } else
            return new UniversalResponseBody<>(-1, "failed", null);
    }


    /**
     * @Description: 一面打分
     * @Param: [mainId, department, score, impression]
     * @return: UniversalResponseBody<EnrollPerson>
     */
    @PostMapping(value = "/interview/first/scoring")
    public UniversalResponseBody updateScoreAndImpression(
            @NotNull @RequestParam("mainId") Integer mainId,
            @NotNull @RequestParam("department") int departmentCode,
            @NotNull @RequestParam("score") String score,
            @RequestParam("impression") String impression) {
        String department = DepartmentEnum.getDepartment(departmentCode);
        if (enrollPersonService.updateScoreAndImpression(mainId, department, score, impression)) {
            return new UniversalResponseBody(0, "success");
        } else
            return new UniversalResponseBody(-1, "failed");
    }


    /**
     * @Description: 二面签到
     * @Param: [mainId, request]
     * @return: UniversalResponseBody
     */
    @PostMapping(value = "/interview/second/presence")
    @ResponseBody
    public UniversalResponseBody secondInterviewPresence(@RequestParam("mainId") int mainId) {
        try {
            if (enrollPersonService.updateStatusByMainId(mainId, 502)) {
                return new UniversalResponseBody(0, "success");
            } else return new UniversalResponseBody(-1, "failed");
        } catch (Exception e) {
            e.printStackTrace();
            return new UniversalResponseBody(-1, "failed");
        }
    }
}
