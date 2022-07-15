package com.metabubble.BWC.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.metabubble.BWC.common.R;
import com.metabubble.BWC.entity.Admin;
import com.metabubble.BWC.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;



    /**
     * 管理员登录
     * @param request 方便获取员工信息
     * @param admin 前端传入的员工信息
     * @return 返回登陆的信息
     */
    @PostMapping("/login")
    public R<Admin> login(HttpServletRequest request, @RequestBody Admin admin) {
        log.info("这是获取的信息{}{}",admin.getEmail(),admin.getPassword());
        String password = admin.getPassword();
        //将页面提交的密码进行MD5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据email查数据库
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getEmail, admin.getEmail());
        Admin adminMsg = adminService.getOne(queryWrapper);
        //如果没有查询==》登陆失败
        if (adminMsg == null) {
            return R.error("用户名或密码错误");
        }
        //密码错误==》登陆失败
        if (!adminMsg.getPassword().equals(password)) {
            return R.error("用户名或密码失败");
        }
        //查看员工状态；0==》禁用
        if (adminMsg.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //登陆成功，id存入session
        request.getSession().setAttribute("admin", adminMsg.getId());

        return R.success(adminMsg);
    }

    /**
     * 员工退出登录
     * @param request 方便删除员工信息
     * @return 返回退出信息
     */
    @DeleteMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //删除session中的账户信息
        request.getSession().removeAttribute("admin");
        return R.success("退出成功");
    }
}


