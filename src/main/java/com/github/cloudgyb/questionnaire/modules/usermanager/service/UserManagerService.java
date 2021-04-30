package com.github.cloudgyb.questionnaire.modules.usermanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.cloudgyb.questionnaire.common.constant.UserStatusEnum;
import com.github.cloudgyb.questionnaire.common.utils.EncryptUtil;
import com.github.cloudgyb.questionnaire.common.utils.QueryPageBuilder;
import com.github.cloudgyb.questionnaire.common.utils.R;
import com.github.cloudgyb.questionnaire.common.utils.RandomUtil;
import com.github.cloudgyb.questionnaire.datasource.annotation.DataSource;
import com.github.cloudgyb.questionnaire.modules.usermanager.controller.form.UserForm;
import com.github.cloudgyb.questionnaire.modules.usermanager.dao.UserDao;
import com.github.cloudgyb.questionnaire.modules.usermanager.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户管理service
 *
 * @author cloudgyb
 * 2021/4/13 17:02
 */
@Service
@DataSource("app")
public class UserManagerService extends ServiceImpl<UserDao, User> {
    private final UserDao userDao;

    UserManagerService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> list() {
        return userDao.selectList(null);
    }

    public IPage<User> pageList(Map<String, Object> params) {
        String userName = (String) params.get("username");
        boolean isShowDeleteUser = Boolean.parseBoolean((String) params.get("isShowDeleteUser"));
        IPage<User> page = new QueryPageBuilder<User>().getPage(params);
        IPage<User> users = this.page(page, new QueryWrapper<User>()
                .ne(!isShowDeleteUser, "status", UserStatusEnum.DELETED.getStatus())
                .like(StringUtils.hasText(userName), "username", userName
                ));
        maskUserPhone(users.getRecords());
        return users;
    }

    /**
     * 掩码用户手机号
     * 例如18401170923 -> 184****0923
     */
    private void maskUserPhone(List<User> users) {
        if (users == null)
            return;
        for (User user : users) {
            String phone = user.getPhone();
            if (phone == null || phone.length() < 11)
                continue;
            String maskedPhone = String.format("%s****%s", phone.substring(0, 3), phone.substring(7));
            user.setPhone(maskedPhone);
        }
    }

    public R addUser(UserForm userForm) {
        final int i = count(new QueryWrapper<User>()
                .eq("username", userForm.getUsername()));
        if (i > 0) {
            return R.error("用户名已被占用，请换一个吧！");
        }
        User userEntity = new User();
        formToEntity(userForm, userEntity);
        saveOrUpdate(userEntity);
        return R.ok();
    }

    private void formToEntity(UserForm form, User user) {
        final String password = form.getPassword();
        final String passSalt = RandomUtil.randomNumber(5);
        final String encryptPass = EncryptUtil.encryptPassword(password, passSalt);
        user.setUsername(form.getUsername());
        user.setPassword(encryptPass);
        user.setPasswordSalt(passSalt);
        user.setIsVip(0);
        user.setRealName(form.getRealName());
        user.setAge(form.getAge());
        user.setSex(form.getSex());
        user.setEmail(form.getEmail());
        user.setPhone(form.getPhone());
        user.setCreateDate(new Date());
    }

    /**
     * 切换用户的锁定状态
     *
     * @param userId 用户id
     */
    public R switchLockUser(Long userId) {
        User user = userDao.selectById(userId);
        if (user == null || user.getStatus() == UserStatusEnum.DELETED.getStatus())
            return R.error("用户不存在！");
        if (user.getStatus() == UserStatusEnum.NORMAL.getStatus())
            user.setStatus(UserStatusEnum.LOCKED.getStatus());
        else if (user.getStatus() == UserStatusEnum.LOCKED.getStatus())
            user.setStatus(UserStatusEnum.NORMAL.getStatus());
        userDao.updateById(user);
        return R.ok();
    }
}
