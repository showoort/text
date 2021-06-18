package controller;

import exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;

/**
 *  分析现在有了框架之后的控制层
 *
 *  原来                          现在
 *  继承HttpServlet               没有继承关系
 *  重写方法service/doXXX          方法随便写
 *  方法内有两个参数 req resp       参数随便  普通变量 String int  对象变量 User  集合Map(RequestParam)  原生 req resp
 *  方法还有两个异常 IO Servlet     没有异常抛出
 *  方法返回值void 自己给响应        方法返回值void  String(转发/重定向)  对象domain Map List(@ResponseBody)
 *
 *  耦合度几乎没有啦
 *  所以在服务端想要带走一些数据 也不用原来的方式
 *  request.setAttribute        框架提供好的对象ModelAndView
 *                              普通的Map(接收参数--@RequestParam 带走信息--@ResponseBody)
 *                              Model
 *                              ModelMap
 *                              ModelAndView
 */

@SessionAttributes("username")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    //设计一个用来控制登录的方法
    @RequestMapping("login.do")
    public ModelAndView login(String username,String password){

        ModelAndView mv = new ModelAndView();
        //0.处理中文字符问题----有filter处理啦
        //1.接收请求的信息-----有方法的参数列表
        //2.找到业务层小弟负责处理核心业务逻辑
        String result = userService.login(username,password);
        //3.根据结果处理响应信息
        if("登录成功".equals(result)){
            mv.addObject("username",username);//相当于存入request作用域
            mv.setViewName("welcome.jsp");
            return mv;
        }else{
//            request.setAttribute("result",result);
//            mm.addAttribute("result",result);//相当于上面那一行 值存入request作用域中
            mv.addObject("result",result);
            mv.setViewName("index.jsp");
            return mv;
        }
    }

    //=============================================================

    //1.试一下接收参数的事情
    @RequestMapping("test.do")
    public void test(Model m, ModelMap mm){
//        System.out.println(m.getClass().getName());
//        m.addAttribute("","");
//        mm.addAttribute("","");
    }



    //=============================================================
    @RequestMapping("testException.do")
    public String testException(){
        System.out.println("controller执行啦");
        //1.字符集
        //2.接收参数(参数列表)
        //3.找寻业务层方法处理逻辑------>可能会产生异常
        try {
            String s = null;
            //s.length();//NullPointerException
        } catch (Exception e){
            e.printStackTrace();//打印后台开发人员可以看到    异常记录在文件 日志
            throw new MyException("服务端业务层产生问题啦");
        }
        //4.正常的响应
        return "TestException.jsp";
    }

}

