package com.github.cloudgyb.questionnaire.modules.sys.controller;

import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserEntity;
import com.github.cloudgyb.questionnaire.modules.sys.entity.SysUserTokenEntity;
import com.github.cloudgyb.questionnaire.modules.sys.form.SysLoginForm;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysCaptchaService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysUserService;
import com.github.cloudgyb.questionnaire.modules.sys.service.SysUserTokenService;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 登录相关
 *
 * @author Mark
 */
@Api(tags = "系统用户登录")
@RestController
public class SysLoginController extends AbstractController {
    private final SysUserService sysUserService;
    private final SysUserTokenService sysUserTokenService;
    private final SysCaptchaService sysCaptchaService;

    public SysLoginController(SysUserService sysUserService,
                              SysUserTokenService sysUserTokenService,
                              SysCaptchaService sysCaptchaService) {
        this.sysUserService = sysUserService;
        this.sysUserTokenService = sysUserTokenService;
        this.sysCaptchaService = sysCaptchaService;
    }

    /**
     * 验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public R login(@RequestBody SysLoginForm form) throws IOException {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error("验证码不正确");
        }

        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }
        //生成token，并保存到数据库
        SysUserTokenEntity token = sysUserTokenService.createToken(user.getUserId());
        return R.ok(Map.of("token",token,"user",user));
    }


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout() {
        sysUserTokenService.logout(getUserId());
        return R.ok();
    }

}
