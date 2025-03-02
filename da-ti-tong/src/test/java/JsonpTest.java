import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.DaTiTongApplication;
import com.huhuhu.project.common.baidu.ImageTextRecognitionService;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.entity.Chapter;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.entity.QuestionType;
import com.huhuhu.project.enums.QuestionTypeEnum;
import com.huhuhu.project.service.CategoryService;
import com.huhuhu.project.service.ChapterService;
import com.huhuhu.project.service.QuestionService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@SpringBootTest(classes = DaTiTongApplication.class)
public class JsonpTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JdbcTemplate template;

    @Test
    public void createTableDDL() {
        String execute = template.execute((StatementCallback<String>) stmt -> {
            ResultSet resultSet = stmt.executeQuery("show create table dtt_paper;");
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String table = resultSet.getString("Table");
                String createTable = resultSet.getString("Create Table");
                sb.append("table:").append(table).append("\n").append("=====").append(createTable);
            }
            return sb.toString();
        });
        System.out.println(execute);
    }

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        try {
            Files.lines(Paths.get("/Users/kangxin/Desktop/test/question.txt")).forEach(sb::append);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Document document = Jsoup.parse(sb.toString());
        // 获取分类
        Elements elements = document.getElementsByClass("menu_box");
        for (Element element : elements) {
            Elements elementsByClass = element.getElementsByClass("li flex row-between");
            Element menuMoreBox = element.getElementById("menuMoreBox");
            for (int i = 0; i < elementsByClass.size(); i++) {
                Elements oneCategory = elementsByClass.get(i).getElementsByTag("h3");
                Category one = new Category();
                one.setCategoryName(oneCategory.text());
                categoryService.add(one);
                System.out.println(oneCategory.text());
                Elements moreBoxElementsByClass = menuMoreBox.getElementsByClass("more_li hide");
                Element subCategory = moreBoxElementsByClass.get(i);
                Elements towCategories = subCategory.getElementsByTag("h3");
                for (int k = 0; k < towCategories.size(); k++) {
                    Element towCategory = towCategories.get(k);
                    Category tow = new Category();
                    tow.setParentId(one.getCategoryId());
                    tow.setCategoryName(towCategory.text());
                    categoryService.add(tow);
                    System.out.println("\t" + towCategory.text());
                    Element nav_list_box = subCategory.getElementsByClass("nav_list_box").get(k);
                    Elements strong = nav_list_box.getElementsByTag("strong");
                    List<Long> cIds = new ArrayList<>();
                    for (Element s : strong) {
                        Category three = new Category();
                        three.setParentId(tow.getCategoryId());
                        three.setCategoryName(s.text());
                        categoryService.add(three);
                        System.out.println("\t\t" + s.text());
                        cIds.add(three.getCategoryId());
                    }

                    Elements as = nav_list_box.getElementsByTag("a");
                    for (int d = 0; d < as.size(); d++) {
                        try {
                            Element s = as.get(d);
                            Document doc = Jsoup.parse(new URL(s.attr("href").replace("_0","_1")), 3000);
                            Elements tree = doc.getElementsByClass("tree_row");
                            for (Element e : tree) {
                                Elements str = e.getElementsByTag("strong");
                                Chapter chapter = new Chapter();
                                chapter.setChapterName(str.text());
                                chapter.setCategoryId(cIds.get(d));
                                chapter.setTrueQuestionChapterFlag(Boolean.FALSE);
                                chapterService.add(chapter);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        }
//         获取章节
//         获取试题集
//         获取问题
    }


//    public static void main(String[] args) throws MalformedURLException {
//        StringBuilder sb = new StringBuilder();
//        try {
//            Files.lines(Paths.get("/Users/kangxin/Desktop/test/question.txt")).forEach(sb::append);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Document document = Jsoup.parse(sb.toString());
//        // 获取分类
//        Elements elements = document.getElementsByClass("menu_box");
//        for (Element element : elements) {
//            Elements elementsByClass = element.getElementsByClass("li flex row-between");
//            Element menuMoreBox = element.getElementById("menuMoreBox");
//            for (int i = 0; i < elementsByClass.size(); i++) {
//                Elements oneCategory = elementsByClass.get(i).getElementsByTag("h3");
//                System.out.println(oneCategory.text());
//                Elements moreBoxElementsByClass = menuMoreBox.getElementsByClass("more_li hide");
//                Element subCategory = moreBoxElementsByClass.get(i);
//                Elements towCategories = subCategory.getElementsByTag("h3");
//                for (int k = 0; k < towCategories.size(); k++) {
//                    Element towCategory = towCategories.get(k);
//                    System.out.println("\t" + towCategory.text());
//                    Element nav_list_box = subCategory.getElementsByClass("nav_list_box").get(k);
//                    Elements strong = nav_list_box.getElementsByTag("a");
//                    for (Element s : strong) {
//                        System.out.println(s.attr("href").replace("_0","_1"));
//                        try {
//                            Document doc = Jsoup.parse(new URL(s.attr("href").replace("_0","_1")), 3000);
////                            System.out.println(doc);
//                            Elements tree = doc.getElementsByClass("tree_row");
//                            for (Element e : tree) {
//                                Elements str = e.getElementsByTag("strong");
//                                System.out.println(str.text());
//                            }
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        }
//    }

    private static Random random = new Random();
    @Test
    public void test02() {
        File root = new File("/Users/kangxin/Desktop/test/QuestionData.tar/QuestionData");
        File[] children = root.listFiles();
        List<Category> categoryList = categoryService.list(Wrappers.lambdaQuery(Category.class).eq(Category::getLevel, 3));
        List<Question> questions = new ArrayList<>();
        for (File child : children) {
            StringBuilder sb = new StringBuilder();
            try {
                Files.lines(Paths.get(child.getAbsolutePath())).forEach(sb::append);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JSONArray objects = JSON.parseArray(sb.toString());
            for (Object object : objects) {
                JSONObject json = JSON.parseObject(object.toString());
                Question question = new Question();
                question.setQuestionTitle(json.getString("title"));
                JSONArray answer = json.getJSONArray("answer");
                JSONArray choseList = json.getJSONArray("choseList");
                question.setQuestionTypeName(answer.size() > 1 ? QuestionTypeEnum.DUO_XUAN.getName() : QuestionTypeEnum.DAN_XUAN.getName());
                List<String> options = JSON.parseArray(choseList.toJSONString(), JSONObject.class).stream().map(item -> item.getString("item")).collect(Collectors.toList());
                question.setOptions(options);
                List<String> rightOptions = JSON.parseArray(answer.toJSONString(), String.class);
                rightOptions.replaceAll(s -> options.get(s.charAt(0) - 'A'));
                question.setRightOptions(rightOptions);
                String[] diff = {"简单" ,"中等" ,"困难"};
                question.setDifficulty(diff[random.nextInt(3)]);
                question.setCategoryId(categoryList.get(random.nextInt(categoryList.size())).getCategoryId());
                questions.add(question);
            }
        }
        questionService.saveBatch(questions);
    }

    @Autowired
    private ImageTextRecognitionService imageTextRecognitionService;

    @Test
    public void testBaiDu() throws IOException {
        BufferedImage image = ImageIO.read(new URL("https://pic3.zhimg.com/v2-01ba4e3055163db88f8a82dc69b7c2a7_r.jpg?source=1940ef5c"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", bos);
        bos.close();
        org.json.JSONObject jsonObject = imageTextRecognitionService.basicGeneral(bos.toByteArray());
        System.out.println(jsonObject);
    }
}
