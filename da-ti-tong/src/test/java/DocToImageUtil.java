
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.ImageType;
import com.spire.pdf.PdfDocument;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DocToImageUtil {

    /**
     * pdf转图片 不关流
     * @param pdf
     * @return
     */
    public static List<InputStream> pdfToImages(InputStream pdf) {
        List<InputStream> imageList = new ArrayList<>();
        PdfDocument document = new PdfDocument();
        document.loadFromStream(pdf);
        int count = document.getPages().getCount();
        if (count > 10) {
            count = 10;
        }
        for (int i = 0; i < count; i++) {
            BufferedImage bufferedImage = document.saveAsImage(i);
            try (ByteArrayOutputStream bao = new ByteArrayOutputStream()){
                ImageIO.write(bufferedImage, "png", bao);
                imageList.add(new ByteArrayInputStream(bao.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imageList;
    }

    public static List<InputStream> docToImages(InputStream doc) {
        List<InputStream> imageList = new ArrayList<>();
        Document document = new Document();
        document.loadFromStream(doc, FileFormat.Auto);
        int count = document.getBuiltinDocumentProperties().getCount();
        if (count > 3) {
            count = 3;
        }
        BufferedImage[] bufferedImages = document.saveToImages(0, count,ImageType.Metafile);
        for (BufferedImage bufferedImage : bufferedImages) {
            try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "png", bao);
                imageList.add(new ByteArrayInputStream(bao.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imageList;
    }

    public static List<InputStream> docStreamToImages(InputStream in, String name) {
        if (name.endsWith(".doc") || name.endsWith(".docx")) {
            return docToImages(in);
        }else if (name.endsWith(".pdf")) {
            return pdfToImages(in);
        }
        throw new BusinessException(ResultCode.IMAGE_PARSE_ERROR);
    }

    private static void deleteRed(BufferedImage bufferedImage) {
        int w = new Color(255, 255, 255).getRGB();
        int minX = bufferedImage.getWidth();
        int minY = bufferedImage.getHeight();
        for (int i = 0; i < minX; i++) {
            for (int j = 0; j < minY; j++) {
                int rgb = bufferedImage.getRGB(i, j);
                int red = (rgb & 0xff0000) >> 16;// 获取color(RGB)中R位
                if (red > 200)
                    bufferedImage.setRGB(i,j,w);
            }
        }
    }

    public static void main(String[] args) {
        File file = new File("/Users/kangxin/Downloads/二级建造师考试真题(全套).doc");
        try (InputStream is = Files.newInputStream(file.toPath())){
            List<InputStream> inputStreams = docStreamToImages(is, file.getName());
            for (int i = 0; i < inputStreams.size(); i++) {
                File f = new File("/Users/kangxin/Downloads/images/" + i + ".png");
                if (!f.exists()) f.createNewFile();
                BufferedImage image = ImageIO.read(inputStreams.get(i));
                BufferedImage subImage = image.getSubimage(0, 20, image.getWidth(), image.getHeight() - 20);
                deleteRed(subImage);
                ImageIO.write(subImage,"png", f);
            }
        }catch (Exception e) {

        }
    }
}
