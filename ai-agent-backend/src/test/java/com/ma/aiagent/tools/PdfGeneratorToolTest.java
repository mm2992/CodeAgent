package com.ma.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfGeneratorToolTest {

    @Test
    void generatePDF() {
        String filename="a.pdf";
        String content="hebust 真好";
        PdfGeneratorTool pdfGeneratorTool=new PdfGeneratorTool();
        final String s = pdfGeneratorTool.generatePDF(filename, content);
        Assertions.assertNotNull(s);
    }
}