package com.wide.exporters;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;

public class PDFExporter {

    public static final String PDF_TEMP = System.getProperty("java.io.tmpdir") + "/wide_export.pdf";
    private final Map<Exercise, String> exercises;

    public PDFExporter(Map<Exercise, String> exercises) {
        this.exercises = exercises;
    }

    public boolean exportExercises() {
        boolean ret = true;
        try {
            createPdf(generateHTML(), PDF_TEMP);
        } catch (IOException | DocumentException e) {
            ret = false;
        }

        return ret;
    }

    private String generateHTML() {
        StringBuffer buf = new StringBuffer();
        for (Exercise e : this.exercises.keySet()) {
            buf.append("<h1>" + e.getTitle() + "</h1>");
            buf.append("<b>Author:</b>" + e.getAuthor() + "</br>");
            buf.append("<b>Book:</b>" + e.getBookTitle() + "</br>");
            buf.append("<b>Publisher:</b>" + e.getPublisher() + "</br>");
            String tags = "";
            String desc = "";
            for (Feature f : e.getFeatures()) {
                if (FeatureFactory.TAGS.equals(f.getName())) {
                    tags = f.getValue();
                }
                if (FeatureFactory.EXERCISE_TEXT.equals(f.getName())) {
                    desc = f.getValue();
                }
            }
            buf.append("<b>Tags:</b>" + tags + "</br>");
            buf.append("<br/><hr/>");
            buf.append(desc.replaceAll("\\$(?<a>[^$]*)\\$", "<img src=\"http://latex.codecogs.com/gif.latex?" + "${a}" + "\"/>"));
            buf.append("<br/><a href=\"" + this.exercises.get(e) + "\">Go to exercise</a><br/><br/><hr/>");
        }

        return buf.toString();
    }

    private void createPdf(final String html, String file) throws IOException, DocumentException {
        // Clean up the HTML
        final Tidy tidy = new Tidy();
        tidy.setFixBackslash(false);
        tidy.setMakeClean(true);
        tidy.setXHTML(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);

        final PipedInputStream in = new PipedInputStream();
        final PipedOutputStream out = new PipedOutputStream(in);
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            org.w3c.dom.Document doc = tidy.parseDOM(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), null);
                            NodeList list = doc.getElementsByTagName("img");
                            for (int i = 0; i < list.getLength(); i++) {
                                if (list.item(i).getAttributes().getNamedItem("src").getNodeValue().contains("latex.codecogs.com")) {
                                    ((Element) list.item(i)).setAttribute("height", "15%");
                                }
                            }
                            tidy.pprint(doc, out);
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, in);
        document.close();
        in.close();
    }
}
