package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.login.wx.dto.WxPhoneNumDto;
import com.huhuhu.project.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.dto.CustomerForm;
import com.huhuhu.project.form.dto.SmLoginDto;
import com.huhuhu.project.form.params.CustomerPageParams;
import com.huhuhu.project.form.params.MinAppAuthPhoneParam;
import com.huhuhu.project.form.params.VipActiveParams;
import com.huhuhu.project.vo.CustomerVo;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface CustomerService extends IService<Customer> {

    /**
     * 发送短信
     * @param phone
     * @return
     */
    boolean sendSm(String phone);

    /**
     * 用户短信登录
     * @param smLoginDto
     * @return
     */
    CustomerVo smLogin(SmLoginDto smLoginDto);

    /**
     * 更新用户信息
     * @param customerForm
     * @return
     */
    Boolean modify(CustomerForm customerForm);

    /**
     * 小程序登录
     * @param code
     * @return
     */
    CustomerVo minAppLogin(String code);

    /**
     * 手机号授权
     * @param minAppAuthPhoneParam
     * @return
     */
    Boolean minAppPhone(MinAppAuthPhoneParam minAppAuthPhoneParam);

    /**
     * 分页查询用户
     * @param customerPageParams
     * @return
     */
    Page<CustomerVo> customerList(CustomerPageParams customerPageParams);

    /**
     * 用户详情
     * @param id
     * @return
     */
    CustomerVo detail(Long id);

    /**
     * 校验用户vip
     * @param userId
     * @return
     */
    Boolean checkUserVipIsExp(Long userId);

    /**
     * 会员数据统计
     * @return
     */
    Map<String, Long> vipStatistic();

    /**
     * 激活vip
     * @param params
     * @return
     */
    Boolean vipActive(VipActiveParams params);

    void export(HttpServletResponse response, CustomerPageParams customerPageParams);

    WxPhoneNumDto userPhoneNum(String code);
}
