package com.gerador.pagamento.service;

import Util.CPFUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;

public class GerarGuia {

    private static final String txid = "TX1234567890";

    public static void gerarGuiaPdf(
            String proprietario,
            String documento,
            String endereco,
            BigDecimal valor,
            LocalDate vencimento,
            String chavePix,
            String nomeRecebedor,
            String cidadeRecebedor
    ) throws Exception {
        String pastaProjeto = "pdfs";
        File dir = new File(pastaProjeto);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String caminho = pastaProjeto + "/guia.pdf";
        BufferedImage qr = gerarQRCode(gerarPayloadPix(chavePix, nomeRecebedor, cidadeRecebedor, valor), 200);
        BufferedImage barcode = gerarQRCode("12345678901234567890", 400);
        documento = CPFUtils.formatarCPF(documento);
        criarPdf(caminho, proprietario, documento, endereco, valor, vencimento,
                "12345678901234567890",
                gerarPayloadPix(chavePix, nomeRecebedor, cidadeRecebedor, valor),
                qr, barcode);
    }

    private static void criarPdf(
            String path,
            String proprietario,
            String documento,
            String endereco,
            BigDecimal valor,
            LocalDate vencimento,
            String codigoBarras,
            String payloadPix,
            BufferedImage qrImg,
            BufferedImage barcodeImg
    ) throws Exception {
        Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(doc, new FileOutputStream(path));
        doc.open();


        Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 11, Font.ITALIC);
        Font negrito = new Font(Font.HELVETICA, 11, Font.BOLD);

        Paragraph pTitulo = new Paragraph("GUIA DE PAGAMENTO (FICTÍCIA)", titulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        pTitulo.setSpacingAfter(12);
        doc.add(pTitulo);


        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{60, 40});


        PdfPCell c1 = new PdfPCell();
        c1.setPadding(8);
        c1.addElement(new Paragraph("Proprietário: " + proprietario, normal));
        c1.addElement(new Paragraph("Documento: " + documento, normal));
        c1.addElement(new Paragraph("Endereço: " + endereco, normal));
        table.addCell(c1);


        PdfPCell c2 = new PdfPCell();
        c2.setPadding(8);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        c2.addElement(new Paragraph("Vencimento: " + df.format(vencimento), negrito));
        c2.addElement(new Paragraph("Valor: R$ " + valor.toPlainString(), negrito));
        table.addCell(c2);

        doc.add(table);

        doc.add(new Paragraph("\n"));

        Paragraph pb = new Paragraph("Código de Barras:", negrito);
        pb.setSpacingAfter(6);
        doc.add(pb);

        try (ByteArrayOutputStream baosQr = new ByteArrayOutputStream();
             ByteArrayOutputStream baosBar = new ByteArrayOutputStream()) {

            ImageIO.write(barcodeImg, "PNG", baosBar);
            Image barcode = Image.getInstance(baosBar.toByteArray());
            barcode.scaleAbsolute(100, 100);
            doc.add(barcode);

            Paragraph pixParagraph = new Paragraph("Código PIX: " + payloadPix, normal);
            pixParagraph.setSpacingBefore(10);
            pixParagraph.setSpacingAfter(6);
            doc.add(pixParagraph);

            Paragraph qrLabel = new Paragraph("QR Code:", negrito);
            qrLabel.setSpacingBefore(10);
            qrLabel.setSpacingAfter(6);
            doc.add(qrLabel);

            ImageIO.write(qrImg, "PNG", baosQr);
            Image qr = Image.getInstance(baosQr.toByteArray());
            qr.scaleAbsolute(150, 150);
            doc.add(qr);
        }

        doc.close();
    }

    public static String gerarPayloadPix(String chave, String nomeRecebedor, String cidade, BigDecimal valor) {

        String payloadFormatIndicator = tlv("00", "01");
        String pointOfInitiation = tlv("01", "12");
        String gui = tlv("00", "br.gov.bcb.pix");
        String chaveField = tlv("01", chave);
        String mai = tlv("26", gui + chaveField);
        String merchantCategoryCode = tlv("52", "0000");
        String transactionCurrency = tlv("53", "986"); // BRL
        String transactionAmount = tlv("54", formatarValor(valor));
        String countryCode = tlv("58", "BR");
        String merchantName = tlv("59", limitar(nomeRecebedor, 25));
        String merchantCity = tlv("60", limitar(cidade, 15));
        String addDataField = tlv("62", tlv("05", limitar(txid, 25))); // TXID

        String semCRC = payloadFormatIndicator + pointOfInitiation + mai + merchantCategoryCode +
                transactionCurrency + transactionAmount + countryCode + merchantName + merchantCity + addDataField +
                "6304";

        String crc = crc16(semCRC).toUpperCase();
        return semCRC + crc;
    }

    private static String tlv(String id, String value) {
        String len = String.format("%02d", value.getBytes(StandardCharsets.UTF_8).length);
        return id + len + value;
    }

    public static String crc16(String payload) {
        int polynomial = 0x1021;
        int result = 0xFFFF;
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i)) & 1) == 1;
                boolean c15 = ((result >> 15) & 1) == 1;
                result <<= 1;
                if (c15 ^ bit) {
                    result ^= polynomial;
                }
                result &= 0xFFFF;
            }
        }
        return String.format("%04X", result);
    }

    private static BufferedImage gerarQRCode(String content, int size) throws Exception {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private static String formatarValor(BigDecimal v) {
        return v.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    private static String limitar(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }

}
