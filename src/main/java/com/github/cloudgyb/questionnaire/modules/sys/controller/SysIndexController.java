package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysIndexPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 首页
 *
 * @author cloudgyb
 * 2021/3/16 18:20
 */
@Controller
@Api(tags = "首页")
public class SysIndexController extends AbstractController {
    private final SysIndexPageService sysIndexPageService;

    SysIndexController(SysIndexPageService sysIndexPageService) {
        this.sysIndexPageService = sysIndexPageService;
    }

    @GetMapping("/index")
    @ResponseBody
    @ApiOperation("首页展示数据")
    public R indexPageData() {
        Map<String, Integer> userInfo = sysIndexPageService.userStatsData();
        List<Map<String, Object>> templateInfo = sysIndexPageService.templateStatsByType();
        R ok = R.ok();
        ok.put("userInfo", userInfo);
        ok.put("tInfo", templateInfo);
        return ok;
    }


}
