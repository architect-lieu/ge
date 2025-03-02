package com.huhuhu.project.utils;

import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author KangXin
 * @version 1.0
 * @date 2022/5/9 17:07
 */
@Slf4j
public class ExcelUtil {

    public static void downLoad(Workbook workBook, String fileName, HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(), "UTF-8"));
            workBook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(ResultCode.DOWNLOAD_EXCEL_ERROR.getMessage(),e);
            throw new BusinessException(ResultCode.DOWNLOAD_EXCEL_ERROR);
        }
    }

    /**
     * 生成下拉列表
     *
     * @param workbook
     * @param firstCol
     * @param lastCol
     */
    public static void selectList(Workbook workbook,int firstCol,int lastCol,String[] data ){
        Sheet sheet = workbook.getSheetAt(0);
        //生成下拉列表
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 90000, firstCol, lastCol);
        //生成下拉框内容
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(data);
        DataValidation validation = dvHelper.createValidation(dvConstraint, cellRangeAddressList);
        //设置错误信息提示
        validation.setShowErrorBox(true);
        //对sheet页生效
        sheet.addValidationData(validation);
    }

    /**
     * Excel 类型枚举
     */
    enum ExcelTypeEnum {
        XLS("xls"), XLSX("xlsx");
        private String value;

        ExcelTypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
