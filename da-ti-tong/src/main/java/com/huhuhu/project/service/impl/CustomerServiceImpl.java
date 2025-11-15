package com.huhuhu.project.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.cache.JvmCache;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.login.wx.dto.WxLoginDto;
import com.huhuhu.project.common.login.wx.dto.WxPhoneInfo;
import com.huhuhu.project.common.login.wx.dto.WxPhoneNumDto;
import com.huhuhu.project.common.login.wx.dto.WxUserInfoDto;
import com.huhuhu.project.common.login.wx.wxservice.WxLoginService;
import com.huhuhu.project.common.sms.SmsService;
import com.huhuhu.project.entity.*;
import com.huhuhu.project.enums.UserEnum;
import com.huhuhu.project.excel.CustomerExcel;
import com.huhuhu.project.form.dto.CustomerForm;
import com.huhuhu.project.form.dto.SmLoginDto;
import com.huhuhu.project.form.params.CustomerPageParams;
import com.huhuhu.project.form.params.MinAppAuthPhoneParam;
import com.huhuhu.project.form.params.VipActiveParams;
import com.huhuhu.project.mapper.*;
import com.huhuhu.project.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.service.OrganizationService;
import com.huhuhu.project.utils.AesCbcUtils;
import com.huhuhu.project.utils.ExcelUtil;
import com.huhuhu.project.utils.TokenUtil;
import com.huhuhu.project.vo.CustomerVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    private final SmsService smsService;

    private final WxLoginService wxLoginService;

    private final DocDownloadRecordMapper docDownloadRecordMapper;

    private final OrganizationService organizationService;

    private final OrgUserMapper orgUserMapper;

    private final ErrorQuestionRecordMapper errorQuestionRecordMapper;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public boolean sendSm(String phone) {
        // 生成随机数
        long code = RandomUtils.nextLong(100000, 999999);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", code);
        // 查询用户是否存在
        Customer cus = this.getOne(Wrappers.lambdaQuery(Customer.class)
                .eq(Customer::getMobilephone, phone));
        DateTime codeCreateTime = new DateTime();
        if (cus == null) {
            cus = new Customer();
            cus.setMsCode((int) code);
            cus.setMobilephone(phone);
            cus.setMsCodeTime(codeCreateTime);
            // todo 真实支付的时候改成普通会员
            cus.setVipFlag(UserEnum.IDENTITY_SUPER_VIP.getCode());
            cus.setIntegral(0);
            cus.setSource(UserEnum.SOURCE_APP.getDesc());
            cus.setNickName(String.format("user_%s", phone));
            this.save(cus);
        } else {
            Integer msCode = cus.getMsCode();
            if (msCode != null) {
                DateTime msCodeTime = new DateTime(cus.getMsCodeTime());
                msCodeTime.setMutable(false);
                DateTime time = msCodeTime.offset(DateField.MINUTE, SystemConstant.SMS_CODE_MIN_TIME);
                // 如果还在最小限制时间内，需要等待再发送
                if (codeCreateTime.before(time)) {
                    throw new BusinessException(ResultCode.SEND_SM_WARMING);
                }
            }
            LambdaUpdateWrapper<Customer> updateWrapper = Wrappers.lambdaUpdate(Customer.class)
                    .set(Customer::getMsCode, code).set(Customer::getMsCodeTime, codeCreateTime)
                    .eq(Customer::getUserId, cus.getUserId());
            this.update(updateWrapper);
        }
        return smsService.sendMessage(phone, map);
    }

    @Override
    public CustomerVo smLogin(SmLoginDto smLoginDto) {
        String phone = smLoginDto.getPhone();
        Integer code = smLoginDto.getCode();
        // 查找用户比较验证码
        Customer cus = this.getOne(Wrappers.lambdaQuery(Customer.class)
                .eq(Customer::getMobilephone, phone));
        if (cus == null) {
            throw new BusinessException(ResultCode.SM_IS_ERROR);
        }
        DateTime msCodeTime = new DateTime(cus.getMsCodeTime());
        DateTime time = msCodeTime.offset(DateField.MINUTE, SystemConstant.SM_LIVE_MAX_TIME);
        if (new DateTime().after(time)) {
            throw new BusinessException(ResultCode.SM_IS_EXPIRE);
        }
        Integer msCode = cus.getMsCode();
        if (!code.equals(msCode)) {
            throw new BusinessException(ResultCode.SM_IS_ERROR);
        }
        // 更新
        cus.setLastLoginTime(new Date());
        // 校验用户vip是否过期
        if (checkUserVipIsExp(cus.getUserId())) {
            cus.setVipFlag(UserEnum.IDENTITY_USER.getCode());
        }
        this.updateById(cus);
        CustomerVo customerVo = new CustomerVo();
        BeanUtil.copyProperties(cus, customerVo);
        customerVo.setToken(TokenUtil.createToken(cus));
        return customerVo;
    }

    @Override
    @Transactional
    public Boolean modify(CustomerForm customerForm) {
        Customer customer = customerForm.build();
        return this.updateById(customer);
    }

    @Override
    @Transactional
    public CustomerVo minAppLogin(String code) {
        WxLoginDto wxLoginDto = wxLoginService.login(code);
        // 处理登陆，根据openid查询用户
        String openid = wxLoginDto.getOpenid();
        String unionid = wxLoginDto.getUnionid();
        Customer customer = this.getOne(Wrappers.lambdaQuery(Customer.class).eq(Customer::getOpenid, openid));
        if (customer == null) {
            customer = new Customer();
            customer.setIntegral(0);
            // todo 真实支付的时候换成普通会员
            customer.setVipFlag(UserEnum.IDENTITY_SUPER_VIP.getCode());
            customer.setOpenid(openid);
            customer.setUnionId(unionid);
            customer.setSessionKey(wxLoginDto.getSessionKey());
            // 获取微信用户的手机号
            // 获取微信的用户信息
            customer.setSource(UserEnum.SOURCE_MIN_APP.getDesc());
            customer.setNickName("岸上人");
//            WxUserInfoDto wxUserInfoDto = wxLoginService.wxUserInfo(openid);
//            customer.setNickName(wxUserInfoDto.getNickname());
//            customer.setHeadPicture(wxUserInfoDto.getHeadImgUrl());
//            this.save(customer);
//            customer = this.getOne(Wrappers.lambdaQuery(Customer.class).eq(Customer::getOpenid, openid));
        }
        customer.setLastLoginTime(new Date());
        customer.setSessionKey(wxLoginDto.getSessionKey());
        // 校验用户vip是否过期
        if (customer.getUserId() != null && checkUserVipIsExp(customer.getUserId())) {
            customer.setVipFlag(UserEnum.IDENTITY_USER.getCode());
        }
        this.saveOrUpdate(customer);
        String token = TokenUtil.createToken(customer);
        CustomerVo customerVo = new CustomerVo();
        customerVo.setMinAppNewUserFlag(Boolean.FALSE);
        BeanUtil.copyProperties(customer, customerVo);
        customerVo.setToken(token);
        return customerVo;
    }

    @Override
    public Boolean checkUserVipIsExp(Long userId) {
        Customer customer = this.getById(userId);
        if (UserEnum.IDENTITY_USER.getCode().equals(customer.getVipFlag())) {
            return true;
        }
        // 判断是否过期
        Date vipExpirationTime = customer.getVipExpirationTime();
        if (vipExpirationTime == null) {
            return false;
        }
        return new Date().after(vipExpirationTime);
    }

    @Override
    public Map<String, Long> vipStatistic() {
        Map<String, Object> claims = TokenUtil.getClaims();
        Integer vipFlag = (Integer) claims.get("vipFlag");
        UserEnum userEnum = UserEnum.getByCode(vipFlag);
        VipConfig vipConfig = JvmCache.getVipConfig(userEnum.getDesc());
        
        // 如果VIP配置不存在，返回默认值
        if (vipConfig == null) {
            Map<String, Long> result = new HashMap<>();
            result.put("downloadCount", 0L);
            result.put("searchCount", 0L);
            return result;
        }
        
        // 查询用户的搜题次数
        // 查询用户的下载次数
        // 计算月时间
        DateTime currentTime = new DateTime();
        currentTime.setMutable(false);
        DateTime lastTime = currentTime.offset(DateField.DAY_OF_MONTH, -DateTime.now().dayOfMonth() + 1);
        // 用户下载的总次数
        Map<String, BigDecimal> statisticNum = docDownloadRecordMapper.statisticNumByType(lastTime, currentTime, TokenUtil.currentLoginUserId());
        long downloadCount = vipConfig.getMonthDownloadDocNum() - statisticNum.get("downloadCount").intValue();
        long searchCount = vipConfig.getMonthSearchQuestionNum() - statisticNum.get("searchCount").intValue();
        Map<String, Long> result = new HashMap<>();
        result.put("downloadCount", downloadCount > 0 ? downloadCount : 0);
        result.put("searchCount", searchCount > 0 ? searchCount : 0);
        return result;
    }

    @Override
    @Transactional
    public Boolean vipActive(VipActiveParams params) {
        String activeCode = params.getActiveCode();
        Organization organization = organizationService.getOne(Wrappers.lambdaQuery(Organization.class)
                .eq(Organization::getActiveCode, activeCode));
        if (organization == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "激活码无效！");
        }
        Date vipExpirationTime = organization.getVipExpirationTime();
        if (new Date().after(vipExpirationTime)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "激活码无效！");
        }
        Customer customer = this.getById(TokenUtil.currentLoginUserId());
        customer.setVipFlag(organization.getVipFlag());
        customer.setVipExpirationTime(vipExpirationTime);
        this.updateById(customer);

        OrgUser orgUser = new OrgUser();
        orgUser.setUserId(TokenUtil.currentLoginUserId());
        orgUser.setOrgId(organization.getId());
        orgUserMapper.insert(orgUser);
        return Boolean.TRUE;
    }

    @Override
    public void export(HttpServletResponse response, CustomerPageParams customerPageParams) {
        String nickName = customerPageParams.getNickName();
        String mobilephone = customerPageParams.getMobilephone();
        Integer vipFlag = customerPageParams.getVipFlag();
        String source = customerPageParams.getSource();
        Date startTime = customerPageParams.getStartTime();
        Date endTime = customerPageParams.getEndTime();
        Page<Customer> page = this.page(Page.of(customerPageParams.getPage(), customerPageParams.getSize()),
                Wrappers.lambdaQuery(Customer.class)
                        .eq(StrUtil.isNotBlank(source), Customer::getSource, source)
                        .eq(vipFlag != null, Customer::getVipFlag, vipFlag)
                        .between(startTime != null && endTime != null, Customer::getCreateTime, startTime, endTime)
                        .like(StrUtil.isNotBlank(mobilephone), Customer::getMobilephone, mobilephone)
                        .like(StrUtil.isNotBlank(nickName), Customer::getNickName, nickName)
                        .orderByDesc(Customer::getCreateTime));
        List<Customer> records = page.getRecords();
        List<CustomerExcel> excelList = new ArrayList<>();
        if (CollUtil.isNotEmpty(records)) {
            for (Customer record : records) {
                CustomerExcel customerExcel = BeanUtil.copyProperties(record, CustomerExcel.class);
                UserEnum userEnum = UserEnum.getByCode(record.getVipFlag());
                if (userEnum != null) {
                    customerExcel.setFlag((!(UserEnum.IDENTITY_USER == userEnum)) ? "是" : "否");
                    customerExcel.setVipType(userEnum.getDesc());
                }
                excelList.add(customerExcel);
            }
            Set<Long> userIds = records.stream().map(Customer::getUserId).collect(Collectors.toSet());
            List<ErrorQuestionRecord> errorQuestionRecordList = errorQuestionRecordMapper.selectList(Wrappers.<ErrorQuestionRecord>query()
                    .lambda().in(ErrorQuestionRecord::getUserId, userIds));
            Map<Long, List<ErrorQuestionRecord>> e = errorQuestionRecordList.stream()
                    .collect(Collectors.groupingBy(ErrorQuestionRecord::getUserId));
            for (CustomerExcel customerExcel : excelList) {
                List<ErrorQuestionRecord> errorQuestionRecords = e.get(customerExcel.getUserId());
                if (CollUtil.isNotEmpty(errorQuestionRecords)) {
                    Set<Long> cIds = errorQuestionRecords.stream().map(ErrorQuestionRecord::getCategoryId).collect(Collectors.toSet());
                    List<String> categoryNames = categoryMapper.selectList(Wrappers.<Category>lambdaQuery().select(Category::getCategoryName).in(Category::getCategoryId, cIds))
                            .stream().map(Category::getCategoryName).collect(Collectors.toList());
                    customerExcel.setSubjects(String.join(",", categoryNames));
                }
            }
        }
        ExportParams exportParams = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, CustomerExcel.class, excelList);
        ExcelUtil.downLoad(workbook, "用户信息", response);
    }

    @Override
    public WxPhoneNumDto userPhoneNum(String code) {
        return wxLoginService.userPhoneNum(code);
    }

    @Override
    @Transactional
    public Boolean minAppPhone(MinAppAuthPhoneParam minAppAuthPhoneParam) {
        // 手机号解密
        String sessionKey = minAppAuthPhoneParam.getSessionKey();
        String decrypt = AesCbcUtils.decrypt(minAppAuthPhoneParam.getEncryptedData(), sessionKey, minAppAuthPhoneParam.getIv(), "UTF-8");
        if (decrypt == null) {
            throw new BusinessException(ResultCode.AUTH_PHONE_ERROR);
        }
        WxPhoneInfo wxPhoneInfo = JSON.parseObject(decrypt, WxPhoneInfo.class);
        // 查询是否存在当前用户
        String phoneNumber = wxPhoneInfo.getPhoneNumber();
        Customer customer = this.getOne(Wrappers.lambdaQuery(Customer.class).eq(Customer::getMobilephone, phoneNumber));
        if (customer != null) {
            // 来源于小程序就返回真
            if (UserEnum.SOURCE_MIN_APP.getDesc().equals(customer.getSource())) {
                return Boolean.TRUE;
            } else if (StringUtils.isBlank(customer.getOpenid())) {
                // 来源于APP并且openid是空的则更新用户的openid
                this.update(Wrappers.lambdaUpdate(Customer.class)
                        .eq(Customer::getMobilephone, phoneNumber)
                        .set(Customer::getOpenid, minAppAuthPhoneParam.getOpenId())
                        .set(Customer::getUnionId, minAppAuthPhoneParam.getUnionId())
                );
                return Boolean.TRUE;
            }
        }
        // 不存在就新增用户
        String openid = minAppAuthPhoneParam.getOpenId();
        String unionid = minAppAuthPhoneParam.getUnionId();
        // 解密微信的用户信息
        String userDecrypt = AesCbcUtils.decrypt(minAppAuthPhoneParam.getEncryptedDataUserInfo(), sessionKey, minAppAuthPhoneParam.getIvUserInfo(), "UTF-8");
        if (userDecrypt == null) {
            throw new BusinessException(ResultCode.AUTH_PHONE_ERROR);
        }
        WxUserInfoDto wxUserInfoDto = JSON.parseObject(userDecrypt, WxUserInfoDto.class);
        customer = new Customer();
        customer.setIntegral(0);
        customer.setVipFlag(UserEnum.IDENTITY_USER.getCode());
        customer.setOpenid(openid);
        customer.setUnionId(unionid);
        customer.setNickName("user_" + phoneNumber);
        customer.setHeadPicture(wxUserInfoDto.getAvatarUrl());
        customer.setMobilephone(phoneNumber);
        customer.setSource(UserEnum.SOURCE_MIN_APP.getDesc());
        return this.save(customer);
    }

    @Override
    public Page<CustomerVo> customerList(CustomerPageParams customerPageParams) {
        String nickName = customerPageParams.getNickName();
        String mobilephone = customerPageParams.getMobilephone();
        Integer vipFlag = customerPageParams.getVipFlag();
        String source = customerPageParams.getSource();

        LambdaQueryWrapper<Customer> queryWrapper = Wrappers.lambdaQuery(Customer.class)
                .eq(StrUtil.isNotBlank(source), Customer::getSource, source)
                .eq(vipFlag != null, Customer::getVipFlag, vipFlag)
                .like(StrUtil.isNotBlank(mobilephone), Customer::getMobilephone, mobilephone)
                .like(StrUtil.isNotBlank(nickName), Customer::getNickName, nickName).orderByDesc(Customer::getCreateTime);

        Page<Customer> page = this.page(Page.of(customerPageParams.getPage(), customerPageParams.getSize()), queryWrapper);
        List<Customer> records = page.getRecords();
        List<CustomerVo> customerVos = BeanUtil.copyToList(records, CustomerVo.class);
        Page<CustomerVo> result = new Page<>(page.getCurrent(), page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(customerVos);
        result.setTotal(page.getTotal());
        if (CollUtil.isNotEmpty(records)) {
            Set<Long> userIds = records.stream().map(Customer::getUserId).collect(Collectors.toSet());
            List<ErrorQuestionRecord> errorQuestionRecordList = errorQuestionRecordMapper.selectList(Wrappers.<ErrorQuestionRecord>query()
                    .lambda().in(ErrorQuestionRecord::getUserId, userIds));
            Map<Long, List<ErrorQuestionRecord>> e = errorQuestionRecordList.stream()
                    .collect(Collectors.groupingBy(ErrorQuestionRecord::getUserId));
            for (CustomerVo customer : customerVos) {
                List<ErrorQuestionRecord> errorQuestionRecords = e.get(customer.getUserId());
                if (CollUtil.isNotEmpty(errorQuestionRecords)) {
                    Set<Long> cIds = errorQuestionRecords.stream().map(ErrorQuestionRecord::getCategoryId).collect(Collectors.toSet());
                    List<String> categoryNames = categoryMapper.selectList(Wrappers.<Category>lambdaQuery().select(Category::getCategoryName).in(Category::getCategoryId, cIds))
                            .stream().map(Category::getCategoryName).collect(Collectors.toList());
                    customer.setSubjects(String.join(",", categoryNames));
                }
            }
        }
        return result;
    }

    @Override
    public CustomerVo detail(Long id) {
        Customer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return BeanUtil.copyProperties(customer, CustomerVo.class);
    }
}
