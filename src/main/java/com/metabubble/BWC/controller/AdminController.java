package com.metabubble.BWC.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.metabubble.BWC.common.CheckCodeUtil;
import com.metabubble.BWC.common.R;
import com.metabubble.BWC.entity.Admin;
import com.metabubble.BWC.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/checkCodeGen")
    public void checkCode(HttpServletRequest request,HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        //生成验证码
        ServletOutputStream os = response.getOutputStream();
        //验证码信息：宽度100，高度50，生成到response传输到页面，长度为4
        String checkCode = CheckCodeUtil.outputVerifyImage(100,50,os,4);
        //存入session对象
        session.setAttribute("checkCodeGen",checkCode);
    }


    /**
     * 管理员登录
     * @param request 获取验证码
     * @param map 客户端传入的员工信息
     * @return 返回登陆的信息
     */
    @PostMapping("/login")
    public R<Admin> login(HttpServletRequest request, @RequestBody Map map)
            throws Exception{
        //获取邮箱
        String email = map.get("email").toString();
        //获取提交的验证码
        String checkCode = map.get("checkCode").toString();
//        String checkCode = request.getParameter("checkCode");
        //获取程序生成的验证码
        HttpSession session = request.getSession();
        String checkCodeGen = (String)session.getAttribute("checkCodeGen");
        //比对验证码
        if (!checkCodeGen.equals(checkCode)){
            //不允许注册
            return R.error("验证码错误");
        }
        //将页面提交的密码进行MD5加密处理
        String password = map.get("password").toString();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据email查数据库
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getEmail, email);
        Admin adminMsg = adminService.getOne(queryWrapper);
        //如果没有查询==》登陆失败
        if (adminMsg == null) {
            return R.error("用户名或密码错误");
        }
        //密码错误==》登陆失败
        if (!adminMsg.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
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

    public void test() {}
}


