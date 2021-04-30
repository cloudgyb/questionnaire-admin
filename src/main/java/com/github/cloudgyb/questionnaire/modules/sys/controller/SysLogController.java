package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysLogEntity;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysLogService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统日志
 *
 * @author Mark
 * @author geng
 */
@Api(tags = "系统日志管理")
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    private final SysLogService sysLogService;

    SysLogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    /**
     * 列表
     */
    @ResponseBody
    @GetMapping("/pageList")
    @RequiresPermissions("sys:log:list")
    public R pageList(@RequestParam Map<String, Object> params) {
        IPage<SysLogEntity> page = sysLogService.queryPage(params);
        return R.ok(page);
    }

    @ResponseBody
    @PostMapping("/clearLog")
    @RequiresPermissions("sys:log:delete")
    public R clearLog() {
        sysLogService.clearLog();
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/delete")
    @RequiresPermissions("sys:log:delete")
    public R delete(@RequestParam Long id) {
        sysLogService.delete(id);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/deleteMany")
    @RequiresPermissions("sys:log:delete")
    public R deleteSelected(@RequestBody Long[] ids) {
        sysLogService.deleteMany(Arrays.asList(ids));
        return R.ok();
    }


}
