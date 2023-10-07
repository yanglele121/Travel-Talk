package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.SiteBean;
import com.example.travel.bean.TravelPlaceBean;
import com.example.travel.bean.UserBean;
import com.example.travel.service.ArticleService;
import com.example.travel.service.CollectService;
import com.example.travel.service.FollowService;
import com.example.travel.service.UserService;
import com.example.travel.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Math.min;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CollectService collectService;

    public static UserBean coverAllAttribute(Map<String, Object> params, UserBean userBean, boolean isCreate) {

        if (params.get("userName") != null) {
            userBean.setUserName(params.get("userName").toString());
        }
        if (params.get("userPassword") != null) {
            userBean.setUserPassword(params.get("userPassword").toString());
        }
        if (params.get("userAge") != null) {
            userBean.setUserAge(Integer.parseInt(params.get("userAge").toString()));
        }
        if (params.get("userSex") != null) {
            userBean.setUserSex(params.get("userSex").toString());
        }
        if (params.get("userEmail") != null) {
            userBean.setUserEmail(params.get("userEmail").toString());
        }
        if (params.get("userTel") != null) {
            userBean.setUserTel(params.get("userTel").toString());
        }
        if (params.get("userAvatarUrl") != null) {
            userBean.setUserAvatarUrl(params.get("userAvatarUrl").toString());
        } else {
            //在这里设置默认值
            if (isCreate) {
                userBean.setUserAvatarUrl("http://localhost:8080/upload/userAvatarUrl/default.jpg");
            }
        }
        if (params.get("userDesc") != null) {
            userBean.setUserDesc(params.get("userDesc").toString());
        } else {
            if (isCreate) {
                userBean.setUserDesc("这个人很懒，什么都没有写~");
            }
        }
        if (params.get("dataJoined") != null) {
            userBean.setDateJoined(LocalDateTime.parse(params.get("dataJoined").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            if (isCreate) {
                userBean.setDateJoined(LocalDateTime.now());
            }
        }
        if (params.get("lastLogin") != null) {
            userBean.setLastLogin(LocalDateTime.parse(params.get("lastLogin").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            if (isCreate) {
                userBean.setLastLogin(LocalDateTime.now());
            }
        }
        if (params.get("userAddr") != null) {
            userBean.setUserAddr(params.get("userAddr").toString());
        }
        if (params.get("userIp") != null) {
            userBean.setUserIp(params.get("userIp").toString());
        }

        return userBean;
    }

    public static Boolean checkUserBean(UserBean userBean, Boolean isCreated) {
        if (userBean.getUserName() == null || userBean.getUserPassword() == null || userBean.getUserSex() == null || userBean.getUserAge() == null) {
            return false;
        }
        if (isCreated) {
            if (userBean.getDateJoined() == null) {
                userBean.setDateJoined(LocalDateTime.now());
            }
            if (userBean.getLastLogin() == null) {
                userBean.setLastLogin(LocalDateTime.now());
            }
            if (userBean.getUserAvatarUrl() == null) {
                userBean.setUserAvatarUrl("http://localhost:8080/upload/userAvatarUrl/default.jpg");
            }
        }
        return true;
    }

    // 将可公开个人信息以UserVo返回
    public static UserVo AssertUserVo(UserBean userBean) {
        UserVo userVo = new UserVo(userBean.getUserNid(), userBean.getUserName(),
                userBean.getUserAge(), userBean.getUserSex(),
                userBean.getUserAvatarUrl(), userBean.getUserDesc(), userBean.getUserAddr());
        return userVo;
    }

    @GetMapping("/CurrentUser")
    public Result CurrentUser(@RequestHeader("Authorization") String token) {
//        System.out.println(token);
        return userService.getUserByToken(token);
    }

    @RequestMapping("/getUserList")
    public Result getUserList(@RequestBody Pageparam pageparam) {

        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<UserBean> userList = userService.getUserList();

        if (userList.isEmpty()) {
            return Result.fail(505, "用户列表为空");
        }

        PageInfo<UserBean> pageInfo = new PageInfo<>(userList);

        return Result.success(pageInfo);
    }

    @PostMapping("/getRelatedUserListByKeyWord")
    public Result getRelatedUserListByKeyWord(@RequestBody Map<String,Object> params){
        String keyWord = params.get("keyWord").toString();
        int pageNum = Integer.parseInt(params.get("pageNum").toString());
        int pageSize = Integer.parseInt(params.get("pageSize").toString());

        List<UserBean> list = userService.getUserListByName(keyWord);

        List<UserVo> resList = new ArrayList<>();
        int startIdx = (pageNum-1)*pageSize;
        for(int i=startIdx;i<min(startIdx+pageSize,list.size());i++) {
            resList.add(AssertUserVo(list.get(i)));
        }
        Map<String,Object> resmap=new HashMap<>();
        List<Integer>fanCounts=new ArrayList<>();
        List<Integer>articleCounts=new ArrayList<>();
        manualPageVo manualPageVo = new manualPageVo(resList,list.size());
        resmap.put("manualPageVo",manualPageVo);
        for (UserVo userVo:resList) {
            fanCounts.add(followService.fanCounts(userVo.getUserNid()));
            articleCounts.add(articleService.ArticleCount(userVo.getUserName()));
        }
        resmap.put("fanCounts",fanCounts);
        resmap.put("articleCounts",articleCounts);
        return Result.success(resmap);
    }

    @PostMapping("/getUserListBySearchWord")
    public Result getUserListBySearchWord(@RequestBody PageparamAndSearchWord pageparamAndSearchWord) {

        PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());

        String searchWord = pageparamAndSearchWord.getSearchword();
        String value = pageparamAndSearchWord.getValue();
        List<UserBean> resList = new ArrayList<>();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        switch (value) {
            case "选项1":
                //按姓名搜索
                resList = userService.getUserListByName(searchWord);
                break;
            case "选项2":
                //按手机号搜索
                if (!pattern.matcher(searchWord).matches()) {
                    return Result.fail(533, "用户ID不为数字");
                }
                UserBean user = userService.getUserByNid(Integer.parseInt(searchWord));
                if (user == null) {
                    return Result.fail(534, "用户不存在");
                }
                resList.add(user);
                break;
            case "选项3":
                //按性别搜索
                resList = userService.getUserListBySex(searchWord);
                break;
            case "选项4":
                //按地址搜索
                resList = userService.getUserListByAddr(searchWord);

                break;
        }
//        System.out.println("4"+resList);
        if (resList.isEmpty()) {
            return Result.fail(505, "用户列表为空");
        }

        PageInfo<UserBean> pageInfo = new PageInfo<>(resList);

        return Result.success(pageInfo);
    }

    // 根据用户名获取用户id
    @GetMapping(value = "/UserByName")
    public Result getUserByName(@RequestParam Map<String, Object> params) {
        String username = params.get("userName").toString();
        UserBean userBean = userService.getUserByName(username);
        if (userBean != null) {
            return Result.success(AssertUserVo(userBean));
        }
        return Result.fail(500, "未查询到用户");
    }

    @PostMapping("/UsersByNames")
    public Result getUserByNameList(@RequestBody Map<String, Object> userList) {
        List<String> names = (List<String>) userList.get("nameList");
        List<UserVo> userVos = new ArrayList<>();
        for (String name : names) {
            userVos.add(AssertUserVo(userService.getUserByName(name)));
        }
        return Result.success(userVos);
    }

    @GetMapping("/footprint/{id}")
    public Result FootPrint(@PathVariable("id") Integer id) {
        List<TravelPlaceBean> cityList = userService.FootPrint(id);
        return Result.success(cityList);
    }

    @PostMapping("/footprint/{id}")
    public Result AddFootPrint(@PathVariable("id") Integer id, @RequestBody Map<String, Object> cityName) {
        String city = cityName.get("cityName").toString();
        String time = cityName.get("travelTime").toString();
        Integer status = userService.AddFootPrint(id, city, time);
        if (status > 0) {
            return Result.success("添加成功！");
        }
        return Result.fail(548, "添加失败!");
    }

    @GetMapping(value = "/getUserByNid/{id}")
    public Result getUserByNid(@PathVariable("id") Integer id, @RequestParam Map<String, Object> params) {
        Boolean isme = Boolean.valueOf(params.get("isme").toString());
        UserBean userBean = userService.getUserByNid(id);
        if (userBean == null) {
            return Result.fail(404, "系统中不存在该用户");
        }
        if (isme) {
            return Result.success(userBean);
        }
        UserVo userVo = AssertUserVo(userBean);
        return Result.success(userVo);
    }
    @GetMapping("/getUserTravelDiary/{id}")
    public Result getUserTravelDiary(@PathVariable("id") Integer id) {
        //获取三个数值：
        List<Integer>list = new ArrayList<>();
        //我去过：我的足迹：
        list.add(userService.FootPrint(id).size());
        //我谈过：我写过的文章数量：
        list.add(articleService.getArticleListByAuthor(id).size());
        //我想过：我收藏过的景点数量：
        list.add(collectService.getCollectArticleListByUserId(id).size());
        return Result.success(list);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Result addUser(@RequestBody UserBean userBean) {


        UserBean user = userService.getUserByName(userBean.getUserName());

        if (user != null) {

            return Result.fail(506,"用户名重复！");
        }
        if(!checkUserBean(userBean,true))
        {
            return Result.fail(507,"重要信息为空！");
        }

        int status = userService.addUser(userBean);


        if (status == 0) {
            return Result.fail(508,"数据库用户插入失败！");
        }
        return Result.success("用户添加成功");
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Result updateUser(@RequestBody UserBean updatedUser) {
        if (!checkUserBean(updatedUser, false)) {
            return Result.fail(502, "重要信息为空请仔细检查");
        }

        String userName = updatedUser.getUserName();
        Integer userId=updatedUser.getUserNid();

        //先判掉重名的情况
        UserBean search_userBean = userService.getUserByName(userName);
        if (search_userBean != null && !Objects.equals(search_userBean.getUserNid(), updatedUser.getUserNid())) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        String oldUserName=userService.getUserByNid(userId).getUserName();
        List<ArticleBean> articleBeanList=articleService.getArticleListByAuthorName(oldUserName);
        for(int i=0;i<articleBeanList.size();i++)
        {
            ArticleBean articleBean=articleBeanList.get(i);
            articleBean.setArticleAuthor(userName);
            articleService.updateArticle(articleBean);
        }
        int status = userService.updateUser(updatedUser);
        if (status == 0) {
            return Result.fail(508, "更新用户失败");
        }
        return Result.success("更新用户信息成功");
    }

    @RequestMapping(value = "/updateUserByAdmin", method = RequestMethod.POST)
    public ModelAndView updateUserByAdmin(@RequestParam Map<String, Object> params) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        String userNamePrev = params.get("userNamePrev").toString();
        String userName = params.get("userName").toString();

        //先判掉重名的情况
        UserBean search_userBean = userService.getUserByName(userName);
        if (!userNamePrev.equals(userName) && search_userBean != null) {
            modelAndView.addObject("status", "failed");
            return modelAndView;
        }

        UserBean userBean = search_userBean;
        userBean = coverAllAttribute(params, userBean, false);

        int status = userService.updateUser(userBean);
        List<UserBean> userBeanRet = userService.getUserList();
        modelAndView.addObject("userList", userBeanRet);
        modelAndView.addObject("status", "success");
        if (status == 0) {
            modelAndView.addObject("status", "failed");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleteUserByName", method = RequestMethod.POST)
    public ModelAndView deleteUserByName(@RequestParam Map<String, Object> params) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        String userName = params.get("userName").toString();
        List<UserBean> userList = userService.getUserList();
        int preSize = userList.size();
        int status = userService.deleteUserByName(userName);
        userList = userService.getUserList();

        modelAndView.addObject("userList", userList);
        modelAndView.addObject("status", "success");
        if (userList.size() == preSize) {
            modelAndView.addObject("status", "failed");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleteUserByNid", method = RequestMethod.POST)
    public ModelAndView deleteUserByNid(@RequestParam Map<String, Object> params) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        int userNid = Integer.parseInt(params.get("userNid").toString());
        List<UserBean> userList = userService.getUserList();
        int preSize = userList.size();
        int status = userService.deleteUserByNid(userNid);
        userList = userService.getUserList();

        modelAndView.addObject("userList", userList);
        modelAndView.addObject("status", "success");
        if (preSize == userList.size()) {
            modelAndView.addObject("status", "failed");
        }
        return modelAndView;
    }

    @PostMapping(value = "/deleteUserByNid/{id}")
    public Result deleteUserByNid(@PathVariable("id") Integer id) {

        UserBean user = userService.getUserByNid(id);
        if (user != null) {
            int status = userService.deleteUserByNid(id);
            if (status == 0) {
                return Result.fail(510, "删除用户失败！");
            }
            return Result.success("删除用户成功");
        } else {
            return Result.fail(509, "用户不存在！");
        }


    }

    @PostMapping(value = "/deleteUserByNids")
    public Result deleteUserByNids(@RequestBody List<UserBean> userList) {

        for (UserBean userBean : userList) {

            UserBean user = userService.getUserByNid(userBean.getUserNid());

            if (user == null) {
                return Result.fail(509, "用户不存在！");
            }

            int status = userService.deleteUserByNid(userBean.getUserNid());


        }
        return Result.success("删除用户成功");

    }
    @GetMapping("/getUserAddr")
    public Result getUserAddr(){
        List<Map<String,Object>>resList = userService.getUserAddr();
        return Result.success(resList);
    }

    @GetMapping("/getUserToAddr")
    public Result getUserToAddr(){
        List<Map<String,Object>>resList = userService.getUserToAddr();
        return Result.success(resList);
    }

    @GetMapping("/getUserSexRate")
    public Result getUserSexRate(){
        List<Map<String,Object>>resList = userService.getUserSexRate();
        return Result.success(resList);
    }

    @GetMapping("/RecentLogin")
    public Result RecentLogin(){
        LocalDateTime curdate = LocalDateTime.now();
        Map<String,Object>resMap = new HashMap<>();
        List<Integer>login_count=new ArrayList<>();
        List<Integer>sign_count=new ArrayList<>();
        List<String> date = new ArrayList<>();
        for(int i=6;i>=0;i--){
            LocalDateTime newdate = curdate.minusDays(i);
            date.add(newdate.format(DateTimeFormatter.ofPattern("MM-dd")));
            Integer year = newdate.getYear();
            Integer month = newdate.getMonthValue();
            Integer day = newdate.getDayOfMonth();
            login_count.add(userService.getLoginCount(year,month,day));
            sign_count.add(userService.getSignCount(year,month,day));
        }
        resMap.put("login_count",login_count);
        resMap.put("date",date);
        resMap.put("sign_count",sign_count);
        return Result.success(resMap);
    }
}
