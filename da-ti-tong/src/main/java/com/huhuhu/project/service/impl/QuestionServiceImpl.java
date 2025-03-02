package com.huhuhu.project.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.baidu.aip.util.Base64Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.common.baidu.ImageTextRecognitionService;
import com.huhuhu.project.common.baidu.response.AccurateBasicResponse;
import com.huhuhu.project.common.cache.JvmCache;
import com.huhuhu.project.common.constant.QuestionTypeConstant;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.*;
import com.huhuhu.project.enums.*;
import com.huhuhu.project.excel.*;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.form.params.SearchByBase64Params;
import com.huhuhu.project.mapper.*;
import com.huhuhu.project.service.ChapterService;
import com.huhuhu.project.service.PaperQuestionService;
import com.huhuhu.project.service.PaperService;
import com.huhuhu.project.service.QuestionService;
import com.huhuhu.project.utils.ExcelUtil;
import com.huhuhu.project.utils.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private static final String REGEX = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， ·、？]";

    private final ChapterService chapterService;

    private final PaperService paperService;

    private final PaperQuestionService paperQuestionService;

    private final CategoryMapper categoryMapper;

    private final UserCollectMapper userCollectMapper;

    private final ErrorQuestionRecordMapper errorQuestionRecordMapper;

    private final ImageTextRecognitionService imageTextRecognitionService;

    private final DocDownloadRecordMapper docDownloadRecordMapper;

    @Override
    @Transactional
    public Boolean add(Question question) {
        Set<Long> paperIds = question.getPaperIds();
        List<PaperQuestion> questions = new ArrayList<>();
        this.save(question);
        if (CollUtil.isNotEmpty(paperIds)) {
            for (Long paperId : paperIds) {
                PaperQuestion paperQuestion = new PaperQuestion();
                paperQuestion.setQuestionId(question.getQuestionId());
                paperQuestion.setPaperId(paperId);
                questions.add(paperQuestion);
            }
        }
        return paperQuestionService.saveBatch(questions);
    }

    @Override
    @Transactional
    public Boolean modify(Question question) {
        Set<Long> paperIds = question.getPaperIds();
        List<PaperQuestion> questions = new ArrayList<>();
        // 再添加
        if (CollUtil.isNotEmpty(paperIds)) {
            paperQuestionService.remove(Wrappers.lambdaQuery(PaperQuestion.class)
                    .eq(PaperQuestion::getQuestionId, question.getQuestionId()));
            for (Long paperId : paperIds) {
                PaperQuestion paperQuestion = new PaperQuestion();
                paperQuestion.setQuestionId(question.getQuestionId());
                paperQuestion.setPaperId(paperId);
                questions.add(paperQuestion);
            }
        }
        paperQuestionService.saveBatch(questions);
        return this.updateById(question);
    }

    @Override
    public Page<Question> pageList(QuestionPageParams questionPageParams) {
        String questionTitle = questionPageParams.getQuestionTitle();
        String questionTypeCode = questionPageParams.getQuestionTypeCode();
        String questionTypeName = questionPageParams.getQuestionTypeName();
        String difficulty = questionPageParams.getDifficulty();
        Long categoryId = questionPageParams.getCategoryId();
        Long paperId = questionPageParams.getPaperId();
        Page<Question> page;
        if (paperId != null) {
            // 连下表
            page = this.baseMapper.pageListByPaperId(Page.of(questionPageParams.getPage(), questionPageParams.getSize()),
                    questionPageParams);
        } else {
            boolean errFlag = questionPageParams.getHighFrequencyError() != null && questionPageParams.getHighFrequencyError();
            Set<Long> errIds = null;
            if (errFlag) {
                List<ErrorQuestionRecord> errorQuestionRecordList = errorQuestionRecordMapper.selectList(Wrappers.<ErrorQuestionRecord>query()
                                .select("question_id as questionId,count(1) as num")
                                .groupBy("question_id")
                                .orderByDesc("num desc")
                        .lambda()
                        .eq(ErrorQuestionRecord::getStatus, QuestionStatus.ERROR.getCode()).last(String.format("limit %d,%d",
                                (questionPageParams.getPage() - 1) * questionPageParams.getSize(), questionPageParams.getSize())));
                if (CollUtil.isNotEmpty(errorQuestionRecordList)) {
                    errIds = errorQuestionRecordList.stream().map(ErrorQuestionRecord::getQuestionId).collect(Collectors.toSet());
                }
                if (CollUtil.isEmpty(errIds)) {
                    return Page.of(questionPageParams.getPage(), questionPageParams.getSize());
                }
            }
            LambdaQueryWrapper<Question> queryWrapper = Wrappers.lambdaQuery(Question.class)
                    .eq(StrUtil.isNotBlank(difficulty), Question::getDifficulty, difficulty)
                    .eq(StrUtil.isNotBlank(questionTypeCode), Question::getQuestionTypeCode, questionTypeCode)
                    .like(StrUtil.isNotBlank(questionTitle), Question::getQuestionTitle, questionTitle)
                    .eq(StrUtil.isNotBlank(questionTypeName), Question::getQuestionTypeName, questionTypeName)
                    .eq(categoryId != null, Question::getCategoryId, categoryId)
                    .eq(errFlag, Question::getQuestionId, errIds)
                    .orderByDesc(Question::getCreateTime);
            page = this.page(Page.of(questionPageParams.getPage(), questionPageParams.getSize()), queryWrapper);
        }
        fillQuestionData(page);
        handlerCustomerData(page);
        return page;
    }

    private void handlerCustomerData(Page<Question> page) {
        if (!SystemConstant.CUSTOMER.equals(TokenUtil.currentLoginUserType())) {
            return;
        }
        List<Question> records = page.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            List<Long> questionIds = records.stream().map(Question::getQuestionId).collect(Collectors.toList());
            Long userId = TokenUtil.currentLoginUserId();
            // 查询收藏集
            List<UserCollect> userCollectList = userCollectMapper.selectList(Wrappers.lambdaQuery(UserCollect.class)
                    .select(UserCollect::getQuestionId)
                    .eq(UserCollect::getUserId, userId)
                    .in(UserCollect::getQuestionId, questionIds));
            // 查询错题集，是否做错过
            List<ErrorQuestionRecord> questionRecords = errorQuestionRecordMapper
                    .selectList(Wrappers.lambdaQuery(ErrorQuestionRecord.class)
                            .in(ErrorQuestionRecord::getQuestionId, questionIds)
                            .eq(ErrorQuestionRecord::getUserId, userId));
            // 设置收藏标志
            Map<Long, UserCollect> collectMap = userCollectList.stream().collect(Collectors.toMap(UserCollect::getQuestionId, Function.identity()));
            for (Question record : records) {
                record.setCollectFlag(collectMap.get(record.getQuestionId()) != null);
            }
            // 设置标志
            Map<Long, ErrorQuestionRecord> errorQuestionRecordMap = questionRecords.stream()
                    .collect(Collectors.toMap(ErrorQuestionRecord::getQuestionId, Function.identity(), (old, n) -> n));
            for (Question record : records) {
                ErrorQuestionRecord questionRecord = errorQuestionRecordMap.get(record.getQuestionId());
                if (questionRecord != null)
                    record.setErrorHistoryFlag(questionRecord.getStatus().equals(QuestionStatus.ERROR.getCode()));
            }
        }
    }

    private void fillQuestionData(Page<Question> page) {
        List<Question> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return;
        }
        List<Long> idList = records.stream().map(Question::getQuestionId).collect(Collectors.toList());
        // 查询所属试卷集
        List<PaperQuestion> paperQuestions = paperQuestionService.list(Wrappers.lambdaQuery(PaperQuestion.class).in(PaperQuestion::getQuestionId, idList));
        Map<Long, Paper> cachePaper = new HashMap<>();
        Map<Long, Category> cacheCategory = new HashMap<>();
        Map<Long, List<PaperQuestion>> chapterGroup = paperQuestions.stream().collect(Collectors.groupingBy(PaperQuestion::getQuestionId));
        List<ErrorQuestionRecord> errorQuestionRecords = errorQuestionRecordMapper.selectList(Wrappers.lambdaQuery(ErrorQuestionRecord.class).eq(ErrorQuestionRecord::getUserId, TokenUtil.currentLoginUserId())
                .in(ErrorQuestionRecord::getQuestionId, idList));
        Map<Long, ErrorQuestionRecord> errorQuestionRecordMap = new HashMap<>();
        if (CollUtil.isNotEmpty(errorQuestionRecords)) {
            errorQuestionRecordMap = errorQuestionRecords.stream().collect(Collectors.toMap(ErrorQuestionRecord::getQuestionId, Function.identity(), (o1, o2) -> o2));
        }
        for (Question record : records) {
            // 设置分类数据
            Category category = cacheCategory.get(record.getCategoryId());
            if (category == null) {
                category = categoryMapper.selectById(record.getCategoryId());
                record.setSubject(category);
                cacheCategory.put(record.getCategoryId(), category);
            } else {
                record.setSubject(category);
            }
            List<PaperQuestion> questions = chapterGroup.get(record.getQuestionId());
            if (CollUtil.isEmpty(questions)) {
                continue;
            }
            Set<Long> pIds = questions.stream().map(PaperQuestion::getPaperId).collect(Collectors.toSet());
            record.setPaperIds(pIds);
            List<Paper> paperList = new ArrayList<>();
            for (PaperQuestion pQuestion : questions) {
                Paper paper = cachePaper.get(pQuestion.getPaperId());
                if (paper == null) {
                    Paper p = paperService.getById(pQuestion.getPaperId());
                    paperList.add(p);
                    cachePaper.put(pQuestion.getPaperId(), p);
                } else {
                    paperList.add(paper);
                }
            }
            record.setPaperList(paperList);
            record.setErrorQuestionRecord(errorQuestionRecordMap.get(record.getQuestionId()));
        }
    }

    @Override
    public Question detail(Long id) {
        Question question = this.getById(id);
        if (question == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return question;
    }

    @Override
    public Map<String, Map<String, Object>> typeStatistic(Long categoryId) {
        List<String> types = Arrays.stream(QuestionTypeEnum.values()).map(QuestionTypeEnum::getName).collect(Collectors.toList());
        List<String> difficulty = Arrays.stream(QuestionDifficultyEnum.values()).map(QuestionDifficultyEnum::getName).collect(Collectors.toList());
        Map<String, Object> typeStatistic = this.baseMapper.typeStatistic(categoryId, types);
        Map<String, Object> difficultyStatistic = this.baseMapper.difficultyStatistic(categoryId, difficulty);
        Map<String, Map<String, Object>> result = new HashMap<>();
        result.put("题型练习", typeStatistic);
        result.put("难度练习", difficultyStatistic);
        return result;
    }

    @Override
    public Map<String, List<Map<String, Object>>> paperStatistic(Long categoryId, Boolean trueQuestionChapterFlag) {
        // 查询分类下的章节 todo 可以做缓存
        List<Chapter> chapters = chapterService.listByCategoryId(categoryId, trueQuestionChapterFlag);
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        // 统计章节下的试题集
        chapters.forEach(chapter -> {
            // 获取章节下的试题集
            List<Paper> papers = paperService.list(Wrappers.lambdaQuery(Paper.class)
                    .select(Paper::getPaperId, Paper::getPaperName)
                    .eq(Paper::getChapterId, chapter.getChapterId()));
            if (CollUtil.isEmpty(papers)) {
                return;
            }
            Map<Long, String> collect = papers.stream().collect(Collectors.toMap(Paper::getPaperId, Paper::getPaperName, (o1, o2) -> o2));
            List<Long> paperIds = papers.stream().map(Paper::getPaperId).collect(Collectors.toList());
            // 统计试题集下的题数
            List<Map<String, Object>> maps = paperQuestionService.listMaps(Wrappers.query(new PaperQuestion()).select("paper_id as paperId, count(1) as num")
                    .lambda()
                    .in(PaperQuestion::getPaperId, paperIds)
                    .groupBy(PaperQuestion::getPaperId));
            for (Map<String, Object> map : maps) {
                map.put("paperName", collect.get((Long) map.get("paperId")));
            }
            result.put(chapter.getChapterName(), maps);
        });
        return result;
    }

    @Override
    public Page<Question> imageSearch(MultipartFile file, int page, int size) throws IOException {
        AccurateBasicResponse accurateBasicResponse = imageTextRecognitionService.accurateBasicRequest(file.getBytes());
        List<AccurateBasicResponse.WordsResult> wordsResult = accurateBasicResponse.getWordsResult();
        List<String> keywords = wordsResult.stream().map(AccurateBasicResponse.WordsResult::getWords).collect(Collectors.toList());
        if (CollUtil.isEmpty(keywords)) {
            return Page.of(page, size);
        }
        Page<Question> questionPage = this.baseMapper.searchByKeyWordList(Page.of(page, size), keywords);
        handlerCustomerData(questionPage);
        fillQuestionData(questionPage);
        return questionPage;
    }


    @Override
    public Page<Question> imageSearchByBase64(SearchByBase64Params searchByBase64Params) {
        // 校验搜题次数
        checkSearchTimes();
        String eCode = searchByBase64Params.getECode();
        AccurateBasicResponse accurateBasicResponse = imageTextRecognitionService.accurateBasicRequest(Base64Util.decode(eCode));
        List<AccurateBasicResponse.WordsResult> wordsResult = accurateBasicResponse.getWordsResult();
        List<String> keywords = wordsResult.stream().map(item -> item.getWords().replaceAll(REGEX, ""))
                .filter(item -> item.length() < 20 && StrUtil.isNotBlank(item))
                .limit(20).peek(String::trim).collect(Collectors.toList());
        if (CollUtil.isEmpty(keywords)) {
            return Page.of(searchByBase64Params.getPage(), searchByBase64Params.getSize());
        }
        Page<Question> questionPage = this.baseMapper.searchByKeyWordList(Page.of(searchByBase64Params.getPage(), searchByBase64Params.getSize()), keywords);
        handlerCustomerData(questionPage);
        fillQuestionData(questionPage);
        // 记录
        DocDownloadSearchRecord docDownloadSearchRecord = new DocDownloadSearchRecord();
        docDownloadSearchRecord.setDocId(-1L);
        docDownloadSearchRecord.setType(DownloadSearchRecordEnum.SEARCH.getDesc());
        docDownloadSearchRecord.setUserId(TokenUtil.currentLoginUserId());
        docDownloadSearchRecord.setKeyword(keywords);
        docDownloadRecordMapper.insert(docDownloadSearchRecord);
        return questionPage;
    }

    private void checkSearchTimes() {
        Map<String, Object> claims = TokenUtil.getClaims();
        Object vipFlag = claims.get("vipFlag");
        if (vipFlag == null) vipFlag = UserEnum.IDENTITY_USER.getCode();
        Integer flag = (Integer) vipFlag;
        // 计算月时间
        DateTime currentTime = new DateTime();
        currentTime.setMutable(false);
        DateTime lastMonth = currentTime.offset(DateField.DAY_OF_MONTH, -DateTime.now().dayOfMonth() + 1);
        // 用户搜题的总次数
        Long searchCount = docDownloadRecordMapper.selectCount(Wrappers.lambdaQuery(DocDownloadSearchRecord.class)
                .eq(DocDownloadSearchRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(DocDownloadSearchRecord::getType, DownloadSearchRecordEnum.SEARCH.getDesc())
                .between(DocDownloadSearchRecord::getCreateTime, lastMonth, currentTime));

        UserEnum userEnum = UserEnum.getByCode(flag);
        VipConfig vipConfig = JvmCache.getVipConfig(userEnum.getDesc());
        if (vipConfig != null) {
            Integer monthSearchQuestionNum = vipConfig.getMonthSearchQuestionNum();
            // 是否到达上限
            if (monthSearchQuestionNum != -1 && searchCount >= monthSearchQuestionNum) {
                throw new BusinessException(ResultCode.MONTH_MAX_SEARCH_ERROR);
            }
        }
    }

    @Override
    public void exportQuestionExcel(QuestionPageParams questionPageParams, HttpServletResponse response) {
        Page<Question> questionPage = this.pageList(questionPageParams);
        List<Question> records = questionPage.getRecords();
        List<QuestionExcel> questionExcels = new ArrayList<>();
        for (Question record : records) {
            QuestionExcel questionExcel = BeanUtil.copyProperties(record, QuestionExcel.class);
            CategoryExcel categoryExcel = BeanUtil.copyProperties(record.getSubject(), CategoryExcel.class);
            List<Paper> paperList = record.getPaperList();
            List<PaperExcel> paperExcels = BeanUtil.copyToList(paperList, PaperExcel.class);
            if (CollUtil.isEmpty(paperExcels)) {
                PaperExcel p = new PaperExcel();
                p.setPaperId("-");
                p.setPaperName("-");
                ChapterExcel chapterExcel = new ChapterExcel();
                chapterExcel.setChapterId("-");
                chapterExcel.setChapterName("-");
                p.setChapterExcel(chapterExcel);
                paperExcels = new ArrayList<>();
                paperExcels.add(p);
            } else {
                Set<Long> cpIds = paperList.stream().map(Paper::getChapterId).collect(Collectors.toSet());
                List<Chapter> chapterList = chapterService.list(Wrappers.lambdaQuery(Chapter.class).in(Chapter::getChapterId, cpIds));
                Map<String, ChapterExcel> excelMap = BeanUtil.copyToList(chapterList, ChapterExcel.class).stream()
                        .collect(Collectors.toMap(ChapterExcel::getChapterId, Function.identity()));
                for (PaperExcel paperExcel : paperExcels) {
                    ChapterExcel chapterExcel = excelMap.get(paperExcel.getChapterId().toString());
                    if (chapterExcel == null) {
                        chapterExcel = new ChapterExcel();
                    }
                    chapterExcel.setChapterId("-");
                    chapterExcel.setChapterName("-");
                    paperExcel.setChapterExcel(chapterExcel);
                }
            }
            List<String> options = record.getOptions();
            if (CollUtil.isEmpty(options)) {
                options = Collections.singletonList("-");
            }
            List<QuestionExcel.Option> optionList = new ArrayList<>();
            for (String option : options) {
                QuestionExcel.Option o = new QuestionExcel.Option();
                o.setOption(option);
                optionList.add(o);
            }

            List<String> rightOptions = record.getRightOptions();
            if (CollUtil.isEmpty(rightOptions)) {
                rightOptions = Collections.singletonList("-");
            }
            List<QuestionExcel.RightOption> rightOptionList = new ArrayList<>();
            for (String rightOption : rightOptions) {
                QuestionExcel.RightOption r = new QuestionExcel.RightOption();
                r.setRightOption(rightOption);
                rightOptionList.add(r);
            }
            questionExcel.setOptionList(optionList);
            questionExcel.setRightOptionList(rightOptionList);
            questionExcel.setCategoryExcel(categoryExcel);
            questionExcel.setPaperExcels(paperExcels);
            questionExcels.add(questionExcel);
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setHeaderColor(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex());
        Workbook sheets = ExcelExportUtil.exportExcel(exportParams, QuestionExcel.class, questionExcels);
        ExcelUtil.downLoad(sheets, "试题集", response);
    }

    @Override
    @Transactional
    public void importQuestionExcel(MultipartFile file, Long categoryId, Long subjectId, Long paperId) {
        if (file == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "文件不能为空～！");
        }
        ImportParams importParams = new ImportParams();
        try (InputStream inputStream = file.getInputStream()) {
            List<QuestionTemplate> questionExcelList = ExcelImportUtil.importExcel(inputStream, QuestionTemplate.class, importParams);
            questionExcelList = questionExcelList.stream().filter(item -> item.getQuestionType() != null).collect(Collectors.toList());
            List<Question> questionList = new ArrayList<>();
            for (QuestionTemplate questionExcel : questionExcelList) {
                Question question = new Question();
                question.setQuestionTitle(questionExcel.getQuestionTitle());
                question.setQuestionTypeName(questionExcel.getQuestionType());
                String options = questionExcel.getOptions();
                String[] ops = options.split("###");
                List<String> opList = Arrays.asList(ops);
                question.setOptions(opList);
                String rightOptions = questionExcel.getRightOptions();
                String[] opRightList = rightOptions.split("###");
                List<String> rightRightList = Arrays.asList(opRightList);
                question.setRightOptions(rightRightList);
                question.setDifficulty(question.getDifficulty());
                question.setRightAnswer(questionExcel.getRightAns());
                question.setAnalysis(questionExcel.getAnalysis());
                question.setCategoryId(categoryId);
                question.setCategoryPid(subjectId);
                questionList.add(question);
            }
            if (CollUtil.isNotEmpty(questionList)) {
                this.saveBatch(questionList);
            }

            if (paperId != null) {
                List<PaperQuestion> paperQuestions = new ArrayList<>();
                for (Question question : questionList) {
                    PaperQuestion paperQuestion = new PaperQuestion();
                    paperQuestion.setQuestionId(question.getQuestionId());
                    paperQuestion.setPaperId(paperId);
                    paperQuestions.add(paperQuestion);
                }
                paperQuestionService.saveBatch(paperQuestions);
            }
        } catch (Exception e) {
            if (e instanceof BusinessException)
                throw (BusinessException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exportQuestionTemplate(HttpServletResponse response) {
        ClassPathResource classPathResource = new ClassPathResource("excel-template/题目模板.xlsx");
        try (InputStream inputStream = classPathResource.getStream()){
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            ExcelUtil.selectList(workbook, 1,1, QuestionTypeConstant.QUESTION_TYPE);
            ExcelUtil.selectList(workbook, 5,5, QuestionTypeConstant.DIFF_TYPE);
            List<Category> categories = categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getParentFlag, Boolean.FALSE));
            if (CollUtil.isNotEmpty(categories)) {
                XSSFSheet sheet = workbook.getSheetAt(1);
                for (int i = 1; i < categories.size(); i++) {
                    XSSFRow row = sheet.createRow(i);
                    XSSFCell id = row.createCell(0);
                    id.setCellValue(categories.get(i).getCategoryId());
                    XSSFCell name = row.createCell(1);
                    name.setCellValue(categories.get(i).getCategoryName());
                }
            }
            ExcelUtil.downLoad(workbook, "题目模板", response);
        }catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.DOWNLOAD_EXCEL_ERROR);
        }

    }


}
