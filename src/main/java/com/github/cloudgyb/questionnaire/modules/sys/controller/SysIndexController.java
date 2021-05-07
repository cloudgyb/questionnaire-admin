package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysConfigService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysIndexPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页
 *
 * @author cloudgyb
 * 2021/3/16 18:20
 */
@RestController
@Api(tags = "首页")
public class SysIndexController extends AbstractController {
    private final SysIndexPageService sysIndexPageService;
    private final SysConfigService sysConfigService;

    SysIndexController(SysIndexPageService sysIndexPageService, SysConfigService sysConfigService) {
        this.sysIndexPageService = sysIndexPageService;
        this.sysConfigService = sysConfigService;
    }

    @GetMapping("/index")
    @ApiOperation("首页展示数据")
    public R indexPageData() {
        Map<String, Integer> userInfo = sysIndexPageService.userStatsData();
        List<Map<String, Object>> templateInfo = sysIndexPageService.templateStatsByType();
        R ok = R.ok();
        ok.put("userInfo", userInfo);
        ok.put("tInfo", templateInfo);
        return ok;
    }

    @GetMapping("/system/info")
    @ApiOperation("首页展示数据")
    public R systemInfo() {
        Map<String, String> systemConfig = sysConfigService.getSystemConfig();
        return R.ok(systemConfig);
    }

}
